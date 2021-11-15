package mc.jabber.init

import mc.jabber.Global
import mc.jabber.core.asm.CompiledCircuit
import mc.jabber.core.data.serial.NbtTransformable
import net.fabricmc.api.ModInitializer
import kotlin.time.ExperimentalTime

@Suppress("unused")
object Main : ModInitializer {
    override fun onInitialize() {
        Global.BLOCKS.register()
        Global.BLOCKS.ENTITIES.register()

        Global.ITEMS.register()

        Global.GUI.registerBoth()
    }
}
