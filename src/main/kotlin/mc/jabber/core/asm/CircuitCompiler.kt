package mc.jabber.core.asm

import codes.som.anthony.koffee.MethodAssembly
import codes.som.anthony.koffee.assembleClass
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.modifiers.final
import codes.som.anthony.koffee.modifiers.public
import codes.som.anthony.koffee.sugar.ClassAssemblyExtension.init
import mc.jabber.Global
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.data.CardinalData
import mc.jabber.core.math.Vec2I
import mc.jabber.minecraft.items.ChipItem
import mc.jabber.util.byteArray
import net.minecraft.util.Identifier
import java.io.File

object CircuitCompiler {
    var className = ""

    fun compileCircuit(board: CircuitBoard): CompiledCircuit {
        val hash = board.hashCode().toString()
        val name = "JabberCircuit\$$hash"

        className = "mc/jabber/core/asm/runtime/$name"

        val compiled = assembleClass(public + final, className, 60, interfaces = listOf(CompiledCircuit::class)) {
            val processes = mutableSetOf<ChipProcess>()
            val places = mutableListOf<Vec2I>()

            // Always present data
            field(private, "ec", CardinalData::class)
            field(private, "s", HashMap::class)

            // Processes
            board.forEach { vec2I, process ->
                // Don't create a data field for chips that don't take input, we'll throw it out
                if (!process.receiveDirections.matches(DirBitmask.NONE)) {
                    places.add(vec2I)
                    field(private, "d\$${vec2I.x}\$${vec2I.y}", CardinalData::class)
                }

                // For storing instances for fast lookup
                processes.add(process)
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
                putfield(className, "ec", CardinalData::class)
                //endregion

                //region Hashmap/chip storage
                aload_0
                new(HashMap::class)
                dup
                invokespecial(HashMap::class, "<init>", returnType = void, parameterTypes = arrayOf())
                putfield(className, "s", HashMap::class)
                //endregion

                // Populate data with empties
                places.forEach {
                    aload_0
                    emptyCardinalData()
                    putfield(className, "d\$${it.x}\$${it.y}", CardinalData::class)
                }
                // Populate processes with instances
                processes.forEach {
                    aload_0
                    lookupChipProcess(it)
                    putfield(className, "p\$${it.id.path}", ChipProcess::class)
                }

                // Generate the initial state
                board.forEach { vec2I, process ->
                    aload_0
                    getfield(className, "p\$${process.id.path}", ChipProcess::class)
                    invokevirtual(ChipProcess::class, "makeInitialStateEntry", "()Lmc/jabber/core/data/serial/NbtTransformable;")
                }
                _return
            }

            method(public + final, "simulate", returnType = void) {
                // Generate input
                board.forEachInput { vec2I, process ->
                    aload_0
                    getfield(className, "p\$${process.id.path}", ChipProcess::class)
                    emptyCardinalData()
                    makeVec2I(vec2I)
                    aload_0
                    getfield(className, "s", HashMap::class)
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

    private fun MethodAssembly.makeVec2I(vec2I: Vec2I) {
        new(Vec2I::class)
        dup
        ldc(vec2I.x)
        ldc(vec2I.y)
        invokespecial(
            Vec2I::class,
            "<init>",
            returnType = void,
            parameterTypes = arrayOf(int, int)
        )
    }

    private fun MethodAssembly.lookupChipProcess(process: ChipProcess) {
        getstatic(Global::class, "PROCESS_ITEM_MAP", HashMap::class)
        new(Identifier::class)
        dup
        ldc(process.id.toString())
        invokespecial(
            Identifier::class,
            "<init>",
            returnType = void,
            parameterTypes = arrayOf(String::class)
        )
        invokevirtual(HashMap::class, "get", "(Ljava/lang/Object;)Ljava/lang/Object;")
        checkcast(ChipItem::class)
        invokevirtual(ChipItem::class, "getProcess", "()Lmc/jabber/core/chips/ChipProcess;")
        checkcast(ChipProcess::class)
    }

    private fun MethodAssembly.emptyCardinalData() {
        aload_0
        getfield(className, "ec", CardinalData::class)
    }
}
