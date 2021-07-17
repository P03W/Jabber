package simulation

import mc.jabber.core.chips.pipes.corners.Quad1PipeChip
import mc.jabber.core.chips.pipes.corners.Quad2PipeChip
import mc.jabber.core.chips.pipes.corners.Quad3PipeChip
import mc.jabber.core.chips.pipes.corners.Quad4PipeChip
import mc.jabber.core.chips.special.CustomChip
import mc.jabber.core.chips.special.DelayChip
import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.circuit.CircuitManager
import mc.jabber.core.data.CircuitType
import mc.jabber.core.data.ComputeData
import mc.jabber.core.data.serial.LongBox
import net.minecraft.util.Identifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BasicSimulationTest {
    @Test
    fun testIllegalCircuitSizeY() {
        Assertions.assertThrows(AssertionError::class.java) {
            CircuitBoard(3, 4)
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
        val circuitManager = CircuitManager(CircuitType.COMPUTE, 4, 3)
        circuitManager.setup()
        repeat(5) {
            circuitManager.simulate()
        }
    }

    @Test
    fun testDataTransfer() {
        val expected: MutableList<LongBox> = mutableListOf()
        repeat(10) {
            expected.add(LongBox(it.toLong() + 1))
        }

        val queue = mutableListOf<LongBox>()
        queue.addAll(expected.map { it.copy() })

        CircuitManager(CircuitType.COMPUTE, 4, 3).also {
            it.board[0, 0] = CustomChip(Identifier("jabber:a")) { _, _, _ -> ComputeData(null, queue.removeFirstOrNull(), null, null) }
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
            repeat(20) { _ ->
                it.simulate()
            }

            assert(expected.isEmpty()) { "Did not receive all expected values! Still missing $expected" }
        }
    }
}
