package mc.jabber.init

import mc.jabber.circuit.CircuitManager
import mc.jabber.data.CircuitType
import mc.jabber.data.ComputeData
import mc.jabber.data.serial.LongBox
import mc.jabber.data.util.TriSet
import mc.jabber.items.chips.DelayChip
import mc.jabber.items.chips.PipeChip
import mc.jabber.math.Cardinal
import mc.jabber.util.log
import kotlin.random.Random

object IntegrityChecks {
    fun checkModIntegrity() {
        fun sep() = "----------".log()

        basicBoardSimulationCheck()
        sep()
        delayChipSerialization()
    }

    fun basicBoardSimulationCheck() {
        CircuitManager(CircuitType.COMPUTE, 4, 3).also {
            it.board.inputMaker = {
                val long = Random.nextLong()
                "Sending $long".log()
                ComputeData(null, null, null, LongBox(long))
            }
            it.board.outputConsumer = { data -> "Received $data".log() }

            it.board[0, 1] = PipeChip()
            it.board[1, 1] = PipeChip()
            it.board[1, 0] = PipeChip()
            it.board[2, 0] = PipeChip()
            it.board[3, 0] = PipeChip()
            it.board[3, 1] = PipeChip()
            it.setup()
            repeat(10) { _ ->
                it.simulate()
            }
        }
    }

    fun delayChipSerialization() {
        val state = DelayChip.DelayState()

        val expectedA = TriSet(Random.nextInt().toShort(), Cardinal.values().random(), LongBox(Random.nextLong()))
        val expectedB = TriSet(Random.nextInt().toShort(), Cardinal.values().random(), LongBox(Random.nextLong()))
        val expectedC = TriSet(Random.nextInt().toShort(), Cardinal.values().random(), LongBox(Random.nextLong()))

        expectedA.log()
        expectedB.log()
        expectedC.log()

        "Testing serialization".log()

        state.data.add(expectedA)
        state.data.add(expectedB)
        state.data.add(expectedC)

        val compound = state.toNbt()

        "Testing de-serialization".log()

        val result = state.fromNbt(compound)
        result.data.forEach {
            it.log()
        }
    }
}
