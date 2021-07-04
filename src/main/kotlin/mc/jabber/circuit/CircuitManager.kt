package mc.jabber.circuit

import mc.jabber.data.CardinalData
import mc.jabber.data.CircuitBoard
import mc.jabber.data.CircuitType
import mc.jabber.data.util.DualHashMap
import mc.jabber.math.Vec2I

class CircuitManager(val type: CircuitType, sizeX: Int, sizeY: Int) {
    val board = CircuitBoard(sizeX, sizeY)

    val state: DualHashMap<Vec2I, Any, CardinalData<*>> = DualHashMap()
    val stagingMap: HashMap<Vec2I, CardinalData<*>> = hashMapOf()

    fun setup() {
        state.backingOfB.forEach { (vec2I, _) ->
            val chip = board[vec2I]
            val initialData = chip?.makeInitialStateEntry()
            if (initialData != null && initialData != Unit) {
                state.setA(vec2I, initialData)
            }
        }
    }

    fun simulate() {
        val input = board.inputMaker?.invoke()
        if (input != null) state.setB(Vec2I(0, board.sizeY / 2), input)

        // Simulate each state
        state.backingOfB.forEach { (vec2I, data) ->
            board[vec2I]?.receive(data, vec2I, state.backingOfA)!!.forEach { dir, any ->
                val offset = vec2I + dir

                if (offset == board.outputPoint && any != null) board.outputConsumer.invoke(any)

                if (any != null && board.isInBounds(offset) && board[offset] != null) {
                    stagingMap[offset] = data.empty().with(dir, any)
                }
            }
        }

        state.backingOfB.clear()

        // Copy the staged data back in
        stagingMap.forEach { (point, data) ->
            state.setB(point, data)
        }

        // delete it all
        stagingMap.clear()
    }

    override fun toString(): String {
        return "\nCircuitManager(type=$type)\n$state\n$board"
    }
}
