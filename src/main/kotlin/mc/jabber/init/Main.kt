package mc.jabber.init

import mc.jabber.Global
import mc.jabber.core.asm.CompiledCircuit
import net.fabricmc.api.ModInitializer
import kotlin.time.ExperimentalTime

@Suppress("unused")
object Main : ModInitializer {
    private var compiledCircuit: CompiledCircuit? = null

    @OptIn(ExperimentalTime::class)
    override fun onInitialize() {
        Global.BLOCKS.register()
        Global.BLOCKS.ENTITIES.register()

        Global.ITEMS.register()

        Global.GUI.registerBoth()
    }
}
