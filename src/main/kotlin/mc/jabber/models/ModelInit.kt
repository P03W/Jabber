package mc.jabber.models

import mc.jabber.Global
import net.fabricmc.api.ClientModInitializer

object ModelInit : ClientModInitializer {
    override fun onInitializeClient() {
        Global.LOG.info("Registering models")
    }
}
