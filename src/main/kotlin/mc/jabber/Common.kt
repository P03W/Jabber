package mc.jabber

import mc.jabber.data.ComputeData
import mc.jabber.math.Cardinal
import net.fabricmc.api.ModInitializer

object Common : ModInitializer {
    override fun onInitialize() {
        val data = ComputeData(0, 0, 0, 40)
        println(data)
        println(data.ofAll(2194))
        println(data[Cardinal.UP])
    }
}

