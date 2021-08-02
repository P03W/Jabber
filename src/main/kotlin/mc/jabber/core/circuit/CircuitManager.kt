package mc.jabber.core.circuit

import mc.jabber.core.data.cardinal.CardinalData
import mc.jabber.core.data.CircuitDataStorage
import mc.jabber.core.data.CircuitType
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.data.serial.rebuildArbitraryData
import mc.jabber.core.math.Vec2I
import mc.jabber.proto.CircuitManagerBuffer
import mc.jabber.proto.circuitManagerProto
import mc.jabber.util.asIdableByteArray
import mc.jabber.util.toByteString

class CircuitManager(val type: CircuitType, sizeX: Int, sizeY: Int, _initialBoard: CircuitBoard = CircuitBoard(sizeX, sizeY)) {

    var board = _initialBoard
        private set

    val chipData: HashMap<Vec2I, NbtTransformable<*>> = hashMapOf()
    val state = CircuitDataStorage(sizeX, sizeY)
    val stagingMap = CircuitDataStorage(sizeX, sizeY)

    fun setup() {
        board.forEach { vec2I, process ->
            val initialData = process.makeInitialStateEntry()
            if (initialData != null) {
                chipData[vec2I] = initialData
            }
        }
    }

    fun simulate() {
        // Generate input
        val empty = type.templateData.empty()
        board.forEachInput { vec2I, chipProcess ->
            chipProcess.receive(empty, vec2I, chipData).forEach { dir, any ->
                val offset = vec2I + dir

                if (any != null && board.isInBounds(offset) && board[offset] != null) {
                    stagingMap[offset] = empty.with(dir, any)
                }
            }
        }

        // Simulate each state
        state.forEach { vec2I, data ->
            board[vec2I]!!.receive(data, vec2I, chipData).forEach { dir, any ->
                val offset = vec2I + dir

                if (any != null && board.isInBounds(offset) && board[offset] != null) {
                    stagingMap[offset] = data.empty().with(dir, any)
                }
            }
        }

        state.clear()

        // Copy the staged data back in
        stagingMap.forEach { point, data ->
            state[point] = data
        }

        // Delete the old data we copied in
        stagingMap.clear()
    }

    fun serialize(): CircuitManagerBuffer.CircuitManagerProto {
        return circuitManagerProto {
            type = this@CircuitManager.type.toProto()
            board = this@CircuitManager.board.serialize()
            chipData.run {
                this@CircuitManager.chipData.forEach { (vec2i, u) ->
                    put(vec2i.transformInto(this@CircuitManager.board.sizeX), u.asIdableByteArray().toByteString())
                }
            }
            state.run {
                this@CircuitManager.state.forEach { vec2I, data ->
                    put(vec2I.transformInto(this@CircuitManager.board.sizeX), data.serialize())
                }
            }
        }
    }

    override fun toString(): String {
        return "\nCircuitManager(type=$type)\n$state\n$board"
    }

    companion object {
        fun deserialize(proto: CircuitManagerBuffer.CircuitManagerProto): CircuitManager {
            val type = CircuitType.fromProto(proto.type)
            val board = CircuitBoard.deserialize(proto.board)

            val manager = CircuitManager(type, board.sizeX, board.sizeY)
            manager.board = board

            proto.chipDataMap.forEach { (pos, u) ->
                manager.chipData[Vec2I.transformOut(pos, board.sizeX)] = rebuildArbitraryData(u)
            }

            proto.stateMap.forEach { (pos, data) ->
                manager.state[Vec2I.transformOut(pos, board.sizeX)] = CardinalData.deserialize(data)
            }

            return manager
        }
    }
}
