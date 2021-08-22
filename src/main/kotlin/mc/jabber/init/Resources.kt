package mc.jabber.init

import mc.jabber.Global
import mc.jabber.util.capitalize
import net.devtech.arrp.api.RRPCallback
import net.devtech.arrp.api.RRPPreGenEntrypoint
import net.devtech.arrp.api.RuntimeResourcePack
import net.devtech.arrp.json.lang.JLang
import net.devtech.arrp.json.models.JModel
import net.devtech.arrp.json.models.JTextures
import net.fabricmc.loader.api.FabricLoader
import kotlin.reflect.full.memberProperties


@Suppress("unused")
object Resources : RRPPreGenEntrypoint {
    val RESOURCE_PACK: RuntimeResourcePack = RuntimeResourcePack.create("jabber_runtime")
    val lang = JLang()

    override fun pregen() {
        Global.ITEMS::class.memberProperties.forEach {
            val name = it.name
            when {
                name.startsWith("CIRCUIT_ITEM_") -> makeCircuit(name, lang)
                name.startsWith("CHIP_") -> makeChip(name, lang)
            }
        }

        RRPCallback.AFTER_VANILLA.register {
            RESOURCE_PACK.addLang(Global.id("en_us"), lang);
            it.add(RESOURCE_PACK);
            if (FabricLoader.getInstance().isDevelopmentEnvironment) RESOURCE_PACK.dump()
        }
    }

    private fun makeCircuit(name: String, lang: JLang) {
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

    fun makeChip(name: String, lang: JLang) {
        val type = name.lowercase().removePrefix("chip_")
        val itemId = Global.id("chip_$type")

        // Model
        RESOURCE_PACK.addModel(
            JModel
                .model()
                .parent("item/generated")
                .textures(
                    JTextures()
                        .layer0("jabber:item/chips/$type")
                ),
            Global.id("item/chip_$type")
        )

        // Lang
        val displayName = if (type.contains('_')) {
            type.split("_").joinToString(" ") { it.capitalize() }
        } else type.capitalize()
        lang.item(itemId, "$displayName Chip")
    }
}
