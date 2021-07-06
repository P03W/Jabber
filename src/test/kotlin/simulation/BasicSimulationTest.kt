package simulation

import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.circuit.CircuitManager
import mc.jabber.core.data.CircuitType
import mc.jabber.core.data.ComputeData
import mc.jabber.core.data.serial.LongBox
import mc.jabber.core.chips.pipes.HorizontalPipeChip
import mc.jabber.core.chips.pipes.corners.Quad3PipeChip
import mc.jabber.core.chips.pipes.corners.Quad2PipeChip
import mc.jabber.core.chips.pipes.corners.Quad4PipeChip
import mc.jabber.core.chips.pipes.corners.Quad1PipeChip
import mc.jabber.core.chips.special.DelayChip
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random

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
        val random = Random(42)

        val expected: MutableList<LongBox> = mutableListOf()
        repeat(10) {
            expected.add(LongBox(random.nextLong()))
        }

        val queue = mutableListOf<LongBox>()
        queue.addAll(expected.map { it.copy() })

        CircuitManager(CircuitType.COMPUTE, 4, 3).also {
            it.board.inputMaker = { ComputeData(null, null, null, queue.removeFirstOrNull()) }
            it.board.outputConsumer = { data ->
                if (expected.first() == data) {
                    expected.removeFirst()
                } else {
                    throw AssertionError("Data arrived out of order! Expected ${expected.first()} but got $data")
                }
            }

            it.board[0, 1] = HorizontalPipeChip()
            it.board[1, 1] = Quad2PipeChip()
            it.board[1, 0] = Quad4PipeChip()
            it.board[2, 0] = DelayChip(1)
            it.board[3, 0] = Quad3PipeChip()
            it.board[3, 1] = Quad1PipeChip()
            it.setup()
            repeat(20) { _ ->
                it.simulate()
            }

            assert(expected.isEmpty()) { "Did not receive all expected values! Still missing $expected" }
        }
    }
}
