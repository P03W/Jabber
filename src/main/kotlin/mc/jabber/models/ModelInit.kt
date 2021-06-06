package mc.jabber.models

import mc.jabber.Globals
import net.fabricmc.api.ClientModInitializer

object ModelInit : ClientModInitializer {
    override fun onInitializeClient() {
        Globals.LOG.info("Registering models")
    }
}
