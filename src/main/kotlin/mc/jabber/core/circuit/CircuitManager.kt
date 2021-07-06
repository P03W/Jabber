package mc.jabber.core.circuit

import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.CircuitType
import mc.jabber.core.math.Vec2I

class CircuitManager(val type: CircuitType, sizeX: Int, sizeY: Int) {
    val board = CircuitBoard(sizeX, sizeY)

    val chipData: HashMap<Vec2I, Any> = hashMapOf()
    val state: HashMap<Vec2I, CardinalData<*>> = hashMapOf()
    val stagingMap: HashMap<Vec2I, CardinalData<*>> = hashMapOf()

    fun setup() {
        board.forEach { vec2I, process ->
            val initialData = process.makeInitialStateEntry()
            if (initialData != null) {
                chipData[vec2I] = initialData
            }
        }
    }

    fun simulate() {
        val input = board.inputMaker?.invoke()
        if (input != null) state[Vec2I(0, board.sizeY / 2)] = input

        // Simulate each state
        state.forEach { (vec2I, data) ->
            board[vec2I]!!.receive(data, vec2I, chipData).forEach { dir, any ->
                val offset = vec2I + dir

                if (offset == board.outputPoint && any != null) board.outputConsumer.invoke(any)

                if (any != null && board.isInBounds(offset) && board[offset] != null) {
                    stagingMap[offset] = data.empty().with(dir, any)
                }
            }
        }

        state.clear()

        // Copy the staged data back in
        stagingMap.forEach { (point, data) ->
            state[point] = data
        }

        // Delete the old data we copied in
        stagingMap.clear()
    }

    override fun toString(): String {
        return "\nCircuitManager(type=$type)\n$state\n$board"
    }
}
