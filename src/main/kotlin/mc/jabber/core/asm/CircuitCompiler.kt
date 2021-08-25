package mc.jabber.core.asm

import codes.som.anthony.koffee.assembleClass
import codes.som.anthony.koffee.insns.jvm._return
import codes.som.anthony.koffee.insns.jvm.getstatic
import codes.som.anthony.koffee.insns.jvm.invokevirtual
import codes.som.anthony.koffee.insns.jvm.ldc
import codes.som.anthony.koffee.modifiers.final
import codes.som.anthony.koffee.modifiers.public
import codes.som.anthony.koffee.sugar.ClassAssemblyExtension.init
import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.util.byteArray
import java.io.File
import java.io.PrintStream

object CircuitCompiler {
    fun compileCircuit(board: CircuitBoard): CompiledCircuit {
        val hash = board.longHashCode().toString()
        val name = "JabberCircuit\$$hash"
        val compiled = assembleClass(public + final, "mc/jabber/core/asm/$name", 60, interfaces = listOf(CompiledCircuit::class)) {
            init(public) {
                getstatic(System::class, "out", PrintStream::class)
                ldc("Constructed!")
                invokevirtual(PrintStream::class, "println", void, String::class)
                _return
            }

            method(public + final, "simulate", returnType = void) {
                getstatic(System::class, "out", PrintStream::class)
                ldc("Simulated!")
                invokevirtual(PrintStream::class, "println", void, String::class)
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
}
