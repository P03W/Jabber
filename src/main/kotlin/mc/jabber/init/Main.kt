package mc.jabber.init

import com.github.p03w.aegis.register
import mc.jabber.Global
import mc.jabber.circuit.CircuitManager
import mc.jabber.data.CircuitType
import mc.jabber.chips.meta.DuplicateChip
import mc.jabber.util.log
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.text.LiteralText
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@Suppress("unused")
object Main : ModInitializer {
    @OptIn(ExperimentalTime::class)
    override fun onInitialize() {
        Global.LOG.info("Main init")

        Global.BLOCKS.register()
        Global.BLOCKS.ENTITIES.register()
        Global.ITEMS.register()

        CommandRegistrationCallback.EVENT.register { d, _ ->
            d.register("jabber") {
                literal("stress") {
                    executes { context ->
                        val time = measureTime {
                            CircuitManager(CircuitType.COMPUTE, 4, 3).also {
                                it.board[0, 1] = DuplicateChip()
                                it.board[1, 1] = DuplicateChip()
                                it.board[1, 0] = DuplicateChip()
                                it.board[2, 0] = DuplicateChip()
                                it.board[3, 0] = DuplicateChip()
                                it.board[3, 1] = DuplicateChip()
                                it.setup()
                                repeat(5_000_000) { _ ->
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
