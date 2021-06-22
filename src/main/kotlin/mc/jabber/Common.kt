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
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@Suppress("unused")
object Common : ModInitializer {
    @OptIn(ExperimentalTime::class)
    override fun onInitialize() {
        Global.LOG.info("Main init")

        Global.BLOCKS.register()
        Global.BLOCKS.ENTITIES.register()
        Global.ITEMS.register()

        CircuitManager(CircuitType.COMPUTE, 4, 3).also {
            it.board.inputMaker = { ComputeData(null, null, null, Random.nextLong()) }
            it.board.outputConsumer = { data -> data.log() }

            it.board[0, 1] = PipeChip()
            it.board[1, 1] = PipeChip()
            it.board[1, 0] = PipeChip()
            it.board[2, 0] = PipeChip()
            it.board[3, 0] = PipeChip()
            it.board[3, 1] = PipeChip()
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
                                repeat(500000) { _ ->
                                    it.simulate()
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
