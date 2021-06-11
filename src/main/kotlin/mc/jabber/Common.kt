package mc.jabber

import com.github.p03w.aegis.register
import mc.jabber.circuit.CircuitManager
import mc.jabber.data.CircuitType
import mc.jabber.data.ComputeData
import mc.jabber.items.chips.PipeChip
import mc.jabber.util.log
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.text.LiteralText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

object Common : ModInitializer {
    @OptIn(ExperimentalTime::class)
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
            repeat(10) { _ ->
                it.simulate()
            }
            it.log()
        }

        CommandRegistrationCallback.EVENT.register { d, _ ->
            d.register("jabber") {
                literal("stress") {
                    executes { context ->
                        val time = measureTime {
                            CircuitManager(CircuitType.COMPUTE, 4, 3).also {
                                it.board[0, 1] = PipeChip()
                                it.board[1, 1] = PipeChip()
                                it.board[1, 0] = PipeChip()
                                it.board[2, 0] = PipeChip()
                                it.board[3, 0] = PipeChip()
                                it.board[3, 1] = PipeChip()
                                repeat(1000000000) { _ ->
                                    it.stepWithInput(ComputeData(null, null, null, 10))
                                }
                                it.log()
                            }
                        }
                        context.source.sendFeedback(LiteralText("Finished stress test in $time"), false)
                    }
                }
            }
        }
    }
}
