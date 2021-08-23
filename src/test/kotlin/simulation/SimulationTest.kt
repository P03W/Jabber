package simulation

import mc.jabber.core.chips.pipes.corners.Quad1PipeChip
import mc.jabber.core.chips.pipes.corners.Quad2PipeChip
import mc.jabber.core.chips.pipes.corners.Quad3PipeChip
import mc.jabber.core.chips.pipes.corners.Quad4PipeChip
import mc.jabber.core.chips.special.CustomChip
import mc.jabber.core.chips.special.DelayChip
import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.circuit.CircuitManager
import mc.jabber.core.data.CardinalData
import net.minecraft.util.Identifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SimulationTest {
    @Test
    fun testIllegalCircuitSizeY() {
        Assertions.assertThrows(AssertionError::class.java) {
            CircuitBoard(1, 0)
        }
    }

    @Test
    fun testIllegalCircuitSizeX() {
        Assertions.assertThrows(AssertionError::class.java) {
            CircuitBoard(0, 1)
        }
    }

    @Test
    fun testEmptyBoard() {
        val circuitManager = CircuitManager(4, 3)
        circuitManager.setup()
        repeat(5) {
            circuitManager.simulate()
        }
    }

    @Test
    fun testDataTransfer() {
        val expected: MutableList<Long> = mutableListOf()
        repeat(10) {
            expected.add(it.toLong() + 1)
        }

        val queue = mutableListOf<Long>()
        queue.addAll(expected)

        CircuitManager(4, 3).also {
            it.board[0, 0] = CustomChip(Identifier("jabber:a"), true) { _, _, _ -> CardinalData(null, queue.removeFirstOrNull(), null, null) }
            it.board[0, 1] = Quad1PipeChip()
            it.board[1, 1] = Quad2PipeChip()
            it.board[1, 0] = Quad4PipeChip()
            it.board[2, 0] = DelayChip(1)
            it.board[3, 0] = Quad3PipeChip()
            it.board[3, 1] = CustomChip(Identifier("jabber:b")) { data, _, _ ->
                if (expected.first() == data.acquire()?.second) {
                    expected.removeFirst()
                } else {
                    throw AssertionError("Data arrived out of order! Expected ${expected.first()} but got $data")
                }
                data.empty()
            }
            it.setup()
            // Expected required steps
            repeat(17) { _ ->
                it.simulate()
            }

            assert(expected.isEmpty()) { "Did not receive all expected values! Still missing $expected" }
        }
    }
}
