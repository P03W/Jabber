package mc.jabber.core.circuit

import com.google.common.io.ByteStreams
import com.google.protobuf.ByteString
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.CircuitDataStorage
import mc.jabber.core.data.CircuitType
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Cardinal
import mc.jabber.core.math.Vec2I
import mc.jabber.proto.CircuitManagerBuffer
import mc.jabber.proto.cardinalData
import mc.jabber.proto.circuitManager
import net.minecraft.nbt.NbtIo

class CircuitManager(val type: CircuitType, sizeX: Int, sizeY: Int) {
    val board = CircuitBoard(sizeX, sizeY)

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

    @Suppress("UnstableApiUsage")
    fun serialize(): CircuitManagerBuffer.CircuitManager {
        return circuitManager {
            board = this@CircuitManager.board.serialize()
            chipData.run {
                this@CircuitManager.chipData.forEach { (vec2i, u) ->
                    val additionalBytes = ByteStreams.newDataOutput()
                    NbtIo.write(u.toNbt(), additionalBytes)
                    val string = ByteString.copyFrom(additionalBytes.toByteArray())
                    put(vec2i.transformInto(this@CircuitManager.board.sizeX), string)
                }
            }
            state.run {
                this@CircuitManager.state.forEach { vec2I, data ->
                    put(vec2I.transformInto(this@CircuitManager.board.sizeX), cardinalData {
                        data.forEach { cardinal, u ->
                            if (u != null) {
                                val additionalBytes = ByteStreams.newDataOutput()
                                NbtIo.write(u.toNbt(), additionalBytes)
                                val string = ByteString.copyFrom(additionalBytes.toByteArray())
                                when (cardinal) {
                                    Cardinal.UP -> up = string
                                    Cardinal.DOWN -> down = string
                                    Cardinal.LEFT -> left = string
                                    Cardinal.RIGHT -> right = string
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    override fun toString(): String {
        return "\nCircuitManager(type=$type)\n$state\n$board"
    }
}
