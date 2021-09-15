package mc.jabber.core.circuit

import mc.jabber.core.asm.CircuitCompiler
import mc.jabber.core.asm.CompiledCircuit
import mc.jabber.core.data.ExecutionContext
import mc.jabber.util.warn
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
        }
    }
}
