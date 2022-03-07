package mc.jabber.init

import mc.jabber.Global
import net.fabricmc.api.ModInitializer

@Suppress("unused")
object Main : ModInitializer {
    override fun onInitialize() {
        Global.BLOCKS.register()
        Global.BLOCKS.ENTITIES.register()

        Global.ITEMS.register()

        Global.GUI.registerBoth()

        Command.register()
    }
}
