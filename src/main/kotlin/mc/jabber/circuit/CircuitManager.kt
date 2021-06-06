package mc.jabber.circuit

import mc.jabber.data.CardinalData
import mc.jabber.data.CircuitBoard
import mc.jabber.data.CircuitType
import mc.jabber.math.Cardinal
import mc.jabber.math.Vec2I
import java.util.*

class CircuitManager(val type: CircuitType, sizeX: Int, sizeY: Int) {
    val board = CircuitBoard(sizeX, sizeY)

    fun runInput(value: CardinalData<*>) {
        val executeStack: ArrayDeque<Pair<CardinalData<*>, Vec2I>> = ArrayDeque()
        executeStack.add(value to Vec2I(0, board.sizeY / 2))

        while (executeStack.isNotEmpty()) {
            val toAct = executeStack.removeFirst()

            if (board.isInBounds(toAct.second)) {
                val output = board[toAct.second]?.receive(toAct.first)

                println(output)

                if (output != null) {
                    if (output.up != null) {
                        executeStack.add(output.only(Cardinal.UP) to toAct.second + Cardinal.UP)
                    }
                    if (output.down != null) {
                        executeStack.add(output.only(Cardinal.DOWN) to toAct.second + Cardinal.DOWN)
                    }
                    if (output.left != null) {
                        executeStack.add(output.only(Cardinal.LEFT) to toAct.second + Cardinal.LEFT)
                    }
                    if (output.right != null) {
                        executeStack.add(output.only(Cardinal.RIGHT) to toAct.second + Cardinal.RIGHT)
                    }
                }
            }
        }
    }
}
