package mc.jabber.init

import mc.jabber.Global
import net.devtech.arrp.api.RRPCallback
import net.devtech.arrp.api.RRPPreGenEntrypoint
import net.devtech.arrp.api.RuntimeResourcePack
import net.devtech.arrp.json.lang.JLang
import net.devtech.arrp.json.models.JModel
import net.devtech.arrp.json.models.JTextures
import net.fabricmc.loader.api.FabricLoader
import kotlin.reflect.full.memberProperties


object Resources : RRPPreGenEntrypoint {
    val RESOURCE_PACK = RuntimeResourcePack.create("jabber_runtime")

    override fun pregen() {
        val lang = JLang()

        Global.ITEMS::class.memberProperties.forEach {
            val name = it.name
            if (name.startsWith("CIRCUIT_ITEM_")) {
                val size = name.removePrefix("CIRCUIT_ITEM_")
                val itemId = Global.id("circuit_$size")

                // Model
                RESOURCE_PACK.addModel(
                    JModel
                        .model()
                        .parent("item/generated")
                        .textures(
                            JTextures()
                                .layer0("jabber:item/circuit")
                        ),
                    Global.id("item/circuit_$size")
                )

                // Lang
                lang.item(itemId, "$size Circuit Board")
            }
        }

        RESOURCE_PACK.addLang(Global.id("en_us"), lang)

        if (FabricLoader.getInstance().isDevelopmentEnvironment) RESOURCE_PACK.dump()

        RRPCallback.AFTER_VANILLA.register { it.add(RESOURCE_PACK) }
    }
}
