package mc.jabber.core.circuit

import mc.jabber.core.asm.CircuitCompiler
import mc.jabber.core.asm.CompiledCircuit
import mc.jabber.core.data.ExecutionContext
import mc.jabber.util.warn
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class CircuitManager(
    val context: ExecutionContext?,
    sizeX: Int,
    sizeY: Int,
    _initialBoard: CircuitBoard = CircuitBoard(sizeX, sizeY)
) {
    var board = _initialBoard
        private set

    private lateinit var compiledCircuit: CompiledCircuit

    var didSetup by Delegates.vetoable(false) { _: KProperty<*>, _: Boolean, new: Boolean ->
        return@vetoable new
    }

    fun setup() {
        compiledCircuit = CircuitCompiler.compileCircuit(board)
        compiledCircuit.setup()
        didSetup = true
    }

    fun simulate() {
        if (didSetup) {
            compiledCircuit.simulate(context)
        } else {
            "Tried to simulate a board that has not been setup!".warn()
            Throwable().stackTrace.toList().warn()
        }
    }

    fun writeNbt(nbt: NbtCompound): NbtCompound {
        nbt.putInt("x", board.sizeX)
        nbt.putInt("y", board.sizeY)

        nbt.put("e", NbtList().apply {
            board.forEach { vec2I, chipProcess ->
                add(NbtCompound().apply {
                    putString("${vec2I.x}*${vec2I.y}", chipProcess.id.path)
                })
            }
        })

        nbt.put("so", NbtCompound().apply {
            compiledCircuit.getChipStorage().forEach { (vec2I, transform) ->
                put("${vec2I.x}*${vec2I.y}", transform.toNbt())
            }
        })

        nbt.put("st", NbtCompound().apply {
            compiledCircuit.getChipState().forEach { vec2I, cardinalData ->
                put("${vec2I.x}*${vec2I.y}", cardinalData.toNbt())
            }
        })

        return nbt
    }
}
