package mc.jabber

import mc.jabber.circuit.CircuitManager
import mc.jabber.data.CircuitType
import mc.jabber.data.ComputeData
import mc.jabber.items.chips.PipeChip
import mc.jabber.util.log
import net.fabricmc.api.ModInitializer

object Common : ModInitializer {
    override fun onInitialize() {
        Globals.LOG.info("Main init")

        CircuitManager(CircuitType.COMPUTE, 4, 3).also {
            it.board[0, 1] = PipeChip()
            it.board[1, 1] = PipeChip()
            it.board[1, 0] = PipeChip()
            it.board[2, 0] = PipeChip()
            it.board[3, 0] = PipeChip()
            it.board[3, 1] = PipeChip()
            it.stepWithInput(ComputeData(null, null, null, 10))
            it.simulate()
            it.log()
        }
    }
}
