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
import mc.jabber.core.math.Vec2I
import mc.jabber.util.byteArray
import org.objectweb.asm.Label
import org.objectweb.asm.tree.LabelNode
import java.io.File

/**
 * Compiles a circuit board into a runtime class for performance
 */
object CircuitCompiler {
    var className = ""

    fun compileCircuit(board: CircuitBoard): CompiledCircuit {
        val hash = board.longHashCode().toString().replace("-", "M")
        val name = "JabberCircuit\$$hash"

        className = "mc/jabber/core/asm/runtime/$name"

        val compiled = assembleClass(public + final, className, 60, interfaces = listOf(CompiledCircuit::class)) {
            val processes = mutableSetOf<ChipProcess>()
            val inputPlaces = mutableListOf<Vec2I>()
            val places = mutableListOf<Vec2I>()

            // Always present data
            field(private, "ec", CardinalData::class)
            field(private, "s", HashMap::class,
                signature = "Ljava/util/HashMap<Lmc/jabber/core/math/Vec2I;Lmc/jabber/core/data/serial/NbtTransformable;>;"
            )

            // Processes
            board.forEach { vec2I, process ->
                // For storing instances for fast lookup
                processes.add(process)

                // Don't create a data field for chips that don't take input, we'll throw it out
                if (!process.receiveDirections.matches(DirBitmask.NONE)) {
                    places.add(vec2I)
                    field(private, "d\$${vec2I.x}\$${vec2I.y}", CardinalData::class)
                } else if (process.isInput) {
                    places.add(vec2I)
                    field(private, "d\$${vec2I.x}\$${vec2I.y}", CardinalData::class)
                }
            }

            // Make sure to iterate on the set to prevent duplicates
            processes.forEach {
                field(private, "p\$${it.id.path}", ChipProcess::class)
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

                // Populate data with empties
                places.forEach {
                    aload_0
                    emptyCardinalData()
                    putfield(self, "d\$${it.x}\$${it.y}", CardinalData::class)
                }
                // Populate processes with instances
                processes.forEach {
                    aload_0
                    lookupChipProcess(it)
                    putfield(className, "p\$${it.id.path}", ChipProcess::class)
                }
                _return
            }

            method(public + final, "setup", returnType = void) {
                // Generate the initial state
                board.forEach { vec2i, process ->
                    aload_0
                    getfield(self, "p\$${process.id.path}", ChipProcess::class)
                    invokevirtual(ChipProcess::class, "makeInitialStateEntry", "()Lmc/jabber/core/data/serial/NbtTransformable;")
                    dup
                    val conditional = LabelNode(Label())
                    ifnonnull(conditional)
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
                    if (process.isInput) {
                        aload_0
                        getfield(self, "p\$${process.id.path}", ChipProcess::class)
                        emptyCardinalData()
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
                    } else {
                        aload_0
                        getfield(self, "p\$${process.id.path}", ChipProcess::class)
                        aload_0
                        getfield(self, "d\$${vec2I.x}\$${vec2I.y}", CardinalData::class)
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
                    }
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

    private fun MethodAssembly.emptyCardinalData() {
        aload_0
        getfield(className, "ec", CardinalData::class)
    }
}