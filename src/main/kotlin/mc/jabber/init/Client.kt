package mc.jabber.init

import mc.jabber.Global
import net.fabricmc.api.ClientModInitializer

object Client : ClientModInitializer {
    override fun onInitializeClient() {
        Global.LOG.info("Registering models")
    }
}
