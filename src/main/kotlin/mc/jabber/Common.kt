package mc.jabber

import mc.jabber.circuit.CircuitManager
import mc.jabber.data.CircuitType
import mc.jabber.data.ComputeData
import mc.jabber.items.chips.PipeChip
import net.fabricmc.api.ModInitializer

object Common : ModInitializer {
    override fun onInitialize() {
        Globals.LOG.info("Main init")

        CircuitManager(CircuitType.COMPUTE, 4, 3).also {
            it.board[0, 1] = PipeChip()
            it.runInput(ComputeData(10, 10, 10, 10))
        }
    }
}

