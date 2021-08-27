package mc.jabber.core.asm

import codes.som.anthony.koffee.MethodAssembly
import codes.som.anthony.koffee.assembleClass
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.labels.KoffeeLabel
import codes.som.anthony.koffee.modifiers.final
import codes.som.anthony.koffee.modifiers.public
import codes.som.anthony.koffee.sugar.ClassAssemblyExtension.init
import mc.jabber.Global
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.data.CardinalData
import mc.jabber.core.math.Cardinal
import mc.jabber.core.math.Vec2I
import mc.jabber.util.byteArray
import org.objectweb.asm.Label
import org.objectweb.asm.tree.LabelNode
import java.io.File

/**
 * Compiles a circuit board into a runtime class for performance
 */
object CircuitCompiler {
    private var className = ""
    private val locationAccess = mutableSetOf<Vec2I>()

    fun compileCircuit(board: CircuitBoard): CompiledCircuit {
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
                field(private, processName(it), ChipProcess::class)
            }

            // Implement version
            method(public + final, "getVersion", int) {
                ldc(Global.EXPECTED_CACHE_VERSION)
                ireturn
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

                // Populate data and storage with empties
                places.forEach {
                    if (board[it]?.isInput == true) emptyCardinalData() else aconst_null
                    putData(it)
                    aconst_null
                    putStorage(it)
                }
                // Populate processes with instances
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

            method(public + final, "simulate", returnType = void) {
                // Step through
                board.forEach { vec2I, process ->
                    val exit = LabelNode(Label())

                    aload_0
                    getfield(self, processName(process), ChipProcess::class)

                    if (process.isInput) {
                        aload_0
                        getfield(self, processName(process), ChipProcess::class)
                        emptyCardinalData()
                    } else {
                        getData(vec2I)
                        ifnull(exit)
                        getData(vec2I)
                    }

                    makeVec2I(vec2I)
                    aload_0
                    getfield(self, "s", HashMap::class)
                    invokevirtual(
                        ChipProcess::class,
                        "receive",
                        returnType = CardinalData::class,
                        parameterTypes = arrayOf(
                            CardinalData::class,
                            Vec2I::class,
                            HashMap::class
                        )
                    )

                    if (!DirBitmask.NONE.matches(process.sendDirections)) {
                        dup
                        ifnull(exit)
                    }
                    unpackProcessConnection(process, Cardinal.UP, vec2I, board)
                    unpackProcessConnection(process, Cardinal.DOWN, vec2I, board)
                    unpackProcessConnection(process, Cardinal.LEFT, vec2I, board)
                    unpackProcessConnection(process, Cardinal.RIGHT, vec2I, board)

                    +KoffeeLabel(this, exit)
                    +KoffeeLabel(this, exit)
                }

                places.forEach {
                    getStorage(it)
                    putData(it)
                }
                _return
            }
        }

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
