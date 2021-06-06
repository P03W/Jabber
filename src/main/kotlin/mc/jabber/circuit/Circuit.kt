package mc.jabber.circuit

import mc.jabber.data.CardinalData
import mc.jabber.data.CircuitBoard
import mc.jabber.data.CircuitType

class CircuitManager(val type: CircuitType, sizeX: Int, sizeY: Int) {
    val board = CircuitBoard(sizeX, sizeY)

    fun runInput(value: CardinalData<*>) {
        TODO("Simulate board")
    }
}
