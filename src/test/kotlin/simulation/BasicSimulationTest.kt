package simulation

import mc.jabber.circuit.CircuitManager
import mc.jabber.data.CircuitType
import mc.jabber.data.ComputeData
import mc.jabber.data.serial.LongBox
import mc.jabber.items.chips.PipeChip
import org.junit.jupiter.api.Test
import kotlin.random.Random

class BasicSimulationTest {
    @Test
    fun testBasicSimulation() {
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

            it.board[0, 1] = PipeChip()
            it.board[1, 1] = PipeChip()
            it.board[1, 0] = PipeChip()
            it.board[2, 0] = PipeChip()
            it.board[3, 0] = PipeChip()
            it.board[3, 1] = PipeChip()
            it.setup()
            repeat(20) { _ ->
                it.simulate()
            }

            assert(expected.isEmpty()) { "Did not receive all expected values! Still missing $expected" }
        }
    }
}
