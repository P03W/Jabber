package mc.jabber.init

import mc.jabber.ComputerNetworks
import mc.jabber.Global
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.render.debug.DebugRenderer
import net.minecraft.util.math.BlockPos

@Suppress("unused")
object Client : ClientModInitializer {
    override fun onInitializeClient() {
        Global.GUI.registerClient()

        WorldRenderEvents.LAST.register { worldRenderContext ->
            ComputerNetworks.networks.forEach { (_, list) ->
                list.forEach {
                    it.locations.forEach {
                        renderBlockOverlay(it)
                    }
                }
            }
        }
    }

    private fun renderBlockOverlay(pos: BlockPos) {
        DebugRenderer.drawBox(pos, 0.1f, 1.0f, 0.0f, 0.5f, 0.01f )
    }
}
