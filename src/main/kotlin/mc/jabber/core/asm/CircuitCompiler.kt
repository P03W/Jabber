package mc.jabber.core.asm

import codes.som.anthony.koffee.MethodAssembly
import codes.som.anthony.koffee.assembleClass
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.labels.KoffeeLabel
import codes.som.anthony.koffee.modifiers.final
import codes.som.anthony.koffee.modifiers.public
import codes.som.anthony.koffee.sugar.ClassAssemblyExtension.init
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.CircuitDataStorage
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Cardinal
import mc.jabber.core.math.Vec2I
import mc.jabber.util.byteArray
import org.objectweb.asm.Label
import org.objectweb.asm.tree.LabelNode
import java.io.File
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * Compiles a circuit board into a runtime class for performance
 */
object CircuitCompiler {
    private var className = ""
    private val locationAccess = mutableSetOf<Vec2I>()

    @OptIn(ExperimentalTime::class)
    fun compileCircuit(board: CircuitBoard): CompiledCircuit {
        val startTime = System.nanoTime()

        val hash = board.longHashCode().toString().replace("-", "M")
        val name = "JabberCircuit\$$hash"
        locationAccess.clear()

        className = "mc/jabber/core/asm/runtime/$name"

        val compiled = assembleClass(public + final, className, 60, interfaces = listOf(CompiledCircuit::class)) {
            val processes = mutableSetOf<ChipProcess>()
            val places = mutableListOf<Vec2I>()

            // Always present data
            field(private, "ec", CardinalData::class)
            field(
                private, "s", HashMap::class,
                signature = "Ljava/util/HashMap<Lmc/jabber/core/math/Vec2I;Lmc/jabber/core/data/serial/NbtTransformable;>;"
            )

            // Processes
            board.forEach { vec2I, process ->
                // For storing instances for fast lookup
                processes.add(process)

                // Don't create a data field for chips that don't take input, we'll throw it out implicitly
                if (!process.receiveDirections.matches(DirBitmask.NONE)) {
                    places.add(vec2I)
                    field(private, dataName(vec2I), CardinalData::class)
                    field(private, storageName(vec2I), CardinalData::class)
                }
            }

            // Make sure to iterate on the set to prevent duplicates
            processes.forEach {
                // Define a field to keep the processes in
                field(private, processName(it), ChipProcess::class)
            }

            method(public + final, "getChipStorage", returnType = HashMap::class) {
                // Get the field and return it
                aload_0
                getfield(self, "s", HashMap::class)
                areturn
            }

            method(public + final, "getChipState", returnType = CircuitDataStorage::class) {
                // Init a data storage of the size of the chip
                new(CircuitDataStorage::class)
                dup
                ldc(board.sizeX)
                ldc(board.sizeY)
                invokespecial(
                    CircuitDataStorage::class,
                    "<init>",
                    returnType = void,
                    parameterTypes = arrayOf(
                        int,
                        int
                    )
                )
                astore_1

                // For every data location
                places.forEach { vec2i ->
                    // Load it, make a vec2i pointing to it, and store it
                    aload_1
                    makeVec2I(vec2i)
                    getData(vec2i)
                    invokevirtual(
                        CircuitDataStorage::class,
                        "set",
                        "(Lmc/jabber/core/math/Vec2I;Lmc/jabber/core/data/CardinalData;)V"
                    )
                }
                aload_1
                areturn
            }

            init(public) {
                // Always present
                //region Empty CardinalData
                aload_0
                new(CardinalData::class)
                dup
                repeat(4) { aconst_null }
                invokespecial(
                    CardinalData::class,
                    "<init>",
                    returnType = void,
                    parameterTypes = arrayOf(
                        java.lang.Long::class,
                        java.lang.Long::class,
                        java.lang.Long::class,
                        java.lang.Long::class
                    )
                )
                putfield(self, "ec", CardinalData::class)
                //endregion
                //region Hashmap/chip storage
                aload_0
                new(HashMap::class)
                dup
                invokespecial(HashMap::class, "<init>", returnType = void, parameterTypes = arrayOf())
                putfield(self, "s", HashMap::class)
                //endregion

                // Populate data and storage with empties if input, null otherwise
                places.forEach {
                    if (board[it]?.isInput == true) emptyCardinalData() else aconst_null
                    putData(it)
                    aconst_null
                    putStorage(it)
                }

                // Populate process fields with instances using runtime lookup
                processes.forEach {
                    aload_0
                    lookupChipProcess(it)
                    putfield(className, processName(it), ChipProcess::class)
                }
                _return
            }

            method(public + final, "setup", returnType = void) {
                // Generate the initial state
                board.forEach { vec2i, process ->
                    aload_0
                    getfield(self, processName(process), ChipProcess::class)
                    invokevirtual(
                        ChipProcess::class,
                        "makeInitialStateEntry",
                        "()Lmc/jabber/core/data/serial/NbtTransformable;"
                    )
                    dup
                    // Store it if we didn't get null
                    val conditional = LabelNode(Label())
                    ifnull(conditional)
                    aload_0
                    getfield(self, "s", HashMap::class)
                    swap
                    makeVec2I(vec2i)
                    swap
                    invokevirtual(HashMap::class, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")
                    +KoffeeLabel(this, conditional)
                }
                _return
            }

            method(public + final, "simulate", parameterTypes = arrayOf(ExecutionContext::class), returnType = void) {
                // Step through
                board.forEach { vec2I, process ->
                    if (process.isInput) {
                        aload_0
                        getfield(self, processName(process), ChipProcess::class)
                        emptyCardinalData()
                    } else {
                        aload_0
                        getfield(self, processName(process), ChipProcess::class)
                        getData(vec2I)
                        ifnull(L["exit"])
                        getData(vec2I)
                    }

                    makeVec2I(vec2I)
                    aload_0
                    getfield(self, "s", HashMap::class)
                    aload_1
                    invokevirtual(
                        ChipProcess::class,
                        "receive",
                        returnType = CardinalData::class,
                        parameterTypes = arrayOf(
                            CardinalData::class,
                            Vec2I::class,
                            HashMap::class,
                            ExecutionContext::class
                        )
                    )

                    if (!DirBitmask.NONE.matches(process.sendDirections)) {
                        dup
                        ifnull(L["exit"])
                    }
                    unpackProcessConnection(process, Cardinal.UP, vec2I, board)
                    unpackProcessConnection(process, Cardinal.DOWN, vec2I, board)
                    unpackProcessConnection(process, Cardinal.LEFT, vec2I, board)
                    unpackProcessConnection(process, Cardinal.RIGHT, vec2I, board)
                    +L["exit"]
                }

                places.forEach {
                    getStorage(it)
                    putData(it)
                }

                _return
            }

            method(public + final, "stateFrom", parameterTypes = arrayOf(Map::class, CircuitDataStorage::class), returnType = void) {
                aload_1
                invokeinterface(Map::class, "keySet", "()Ljava/util/Set;")
                invokeinterface(Set::class, "iterator", "()Ljava/util/Iterator;")
                astore(4)

                +L["unpackMapIter"]
                aload(4)
                invokeinterface(Iterator::class, "hasNext", "()Z")
                ifeq(L["escapeUnpack"])

                aload_0
                getfield(self, "s", HashMap::class)

                aload(4)
                invokeinterface(Iterator::class, "next", "()Ljava/lang/Object;")
                checkcast(Vec2I::class)
                dup

                aload_1
                swap
                invokeinterface(Map::class, "get", "(Ljava/lang/Object;)Ljava/lang/Object;")
                checkcast(NbtTransformable::class)

                invokevirtual(HashMap::class, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")
                pop

                goto(L["unpackMapIter"])
                +L["escapeUnpack"]
                _return
            }
        }

        val endTime = System.nanoTime()
        println("Compiled circuit $name in ${Duration.nanoseconds(endTime-startTime)}")

        val dir = File("jabber/debug/")
        dir.mkdirs()
        val fileOutput = dir.resolve("$name.class")
        fileOutput.createNewFile()
        fileOutput.outputStream().write(compiled.byteArray())

        val loaded = JabberClassLoader.defineClass(compiled).constructors[0].newInstance()
        return loaded as CompiledCircuit
    }

    private fun processName(process: ChipProcess): String {
        return "Chip\$${process.id.path}"
    }

    private fun dataName(vec2I: Vec2I): String {
        return "Data${vec2I.x}\$${vec2I.y}"
    }

    private fun storageName(vec2I: Vec2I): String {
        return "Queue${vec2I.x}\$${vec2I.y}"
    }

    //region Get/Put Data and Storage
    private fun MethodAssembly.putData(vec2I: Vec2I) {
        aload_0
        swap
        putfield(className, dataName(vec2I), CardinalData::class)
    }

    private fun MethodAssembly.getData(vec2I: Vec2I) {
        aload_0
        getfield(className, dataName(vec2I), CardinalData::class)
    }

    private fun MethodAssembly.putStorage(vec2I: Vec2I) {
        aload_0
        swap
        putfield(className, storageName(vec2I), CardinalData::class)
    }

    private fun MethodAssembly.getStorage(vec2I: Vec2I) {
        aload_0
        getfield(className, storageName(vec2I), CardinalData::class)
    }
    //endregion

    private fun MethodAssembly.emptyCardinalData() {
        aload_0
        getfield(className, "ec", CardinalData::class)
    }

    /**
     * Generates the simulation code for a chip in a single direction by getting the chip in that direction, seeing if the send/receive lines up, and finally adding the bytecode to copy that data
     *
     * Should already be a [CardinalData] on the top of the stack
     */
    private fun MethodAssembly.unpackProcessConnection(
        sender: ChipProcess,
        cardinal: Cardinal,
        pos: Vec2I,
        board: CircuitBoard
    ) {
        if (cardinal.mask.matches(sender.sendDirections)) {
            val offset = pos + cardinal
            if (board.isInBounds(offset)) {
                val chip = board[offset]
                if (chip != null) {
                    if (cardinal.mask.matches(chip.receiveDirections)) {
                        val isFirstWrite = locationAccess.add(offset)
                        val getOp = when (cardinal) {
                            Cardinal.UP -> "getUp"
                            Cardinal.DOWN -> "getDown"
                            Cardinal.LEFT -> "getLeft"
                            Cardinal.RIGHT -> "getRight"
                        }

                        dup
                        if (isFirstWrite) {
                            getstatic(Cardinal::class, cardinal.name, Cardinal::class)
                            invokevirtual(CardinalData::class, "only", "(Lmc/jabber/core/math/Cardinal;)Lmc/jabber/core/data/CardinalData;")
                            putStorage(offset)
                        } else {
                            getStorage(offset)
                            swap
                            invokevirtual(CardinalData::class, getOp, "()Ljava/lang/Long;")
                            getstatic(Cardinal::class, cardinal.name, Cardinal::class)
                            swap
                            invokestatic(
                                CardinalData::class,
                                "with",
                                "(Lmc/jabber/core/data/CardinalData;Lmc/jabber/core/math/Cardinal;Ljava/lang/Long;)Lmc/jabber/core/data/CardinalData;"
                            )
                            putStorage(offset)
                        }
                    }
                }
            }
        }
    }
}
