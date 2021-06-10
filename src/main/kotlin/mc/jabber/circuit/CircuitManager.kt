package mc.jabber.circuit

import mc.jabber.data.CardinalData
import mc.jabber.data.CircuitBoard
import mc.jabber.data.CircuitType
import mc.jabber.data.util.DualHashMap
import mc.jabber.math.Vec2I
import mc.jabber.util.log

class CircuitManager(val type: CircuitType, sizeX: Int, sizeY: Int) {
    val board = CircuitBoard(sizeX, sizeY)

    val state: DualHashMap<Vec2I, Any, CardinalData<*>> = DualHashMap()
    val stagingMap: HashMap<Vec2I, CardinalData<*>> = hashMapOf()

    var input: CardinalData<*>? = null

    fun stepWithInput(input: CardinalData<*>?) {
        this.input = input
        simulate()
        // Consume the input
        this.input = null
    }

    fun simulate() {
        // Set the input if we got any
        if (input != null) state.setB(Vec2I(0, board.sizeY / 2), input!!)

        // Simulate each state
        state.forEach { vec2I, _, data ->
            state.backingOfB.remove(vec2I)

            if (board.isInBounds(vec2I) && data != null) {
                val output = board[vec2I]?.receive(data, vec2I, state.backingOfA)

                if (board[vec2I] != null) "$vec2I received $data and returned $output".log()

                output?.forEach { dir, any ->
                    val offset = vec2I + dir
                    if (any != null && board[offset] != null) {
                        if (stagingMap[offset] == null) {
                            stagingMap[offset] = data.empty()
                        }

                        "$any going $dir into ${board[offset]}".log()
                        stagingMap[offset] = output.only(dir)
                    }
                }
            }
        }

        state.log()
        stagingMap.log()

        // Copy the staged data back in
        stagingMap.forEach { (point, data) ->
            state.setB(point, data)
        }

        // delete it all
        stagingMap.clear()
    }

    override fun toString(): String {
        return buildString {
            append("\nCircuitManager(type=$type)")
            append('\n')
            append(input)
            append('\n')
            append(state)
            append('\n')
            append(board)
        }
    }
}
