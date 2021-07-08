package mc.jabber.init

import mc.jabber.Global
import mc.jabber.util.assertType
import mc.jabber.util.capitalize
import net.devtech.arrp.api.RRPCallback
import net.devtech.arrp.api.RRPPreGenEntrypoint
import net.devtech.arrp.api.RuntimeResourcePack
import net.devtech.arrp.json.lang.JLang
import net.devtech.arrp.json.models.JModel
import net.devtech.arrp.json.models.JTextures
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.registry.Registry
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties


@Suppress("unused")
object Resources : RRPPreGenEntrypoint {
    val RESOURCE_PACK: RuntimeResourcePack = RuntimeResourcePack.create("jabber_runtime")

    val toAutoRegister: MutableList<KProperty1<Global.ITEMS, *>> = mutableListOf()
    var canRegister = AtomicBoolean()

    override fun pregen() {
        val lang = JLang()

        Global.ITEMS::class.memberProperties.forEach {
            val name = it.name
            when {
                name.startsWith("CIRCUIT_ITEM_") -> makeCircuit(name, lang)
                name.startsWith("CHIP_") -> {
                    toAutoRegister.add(it); makeChip(name, lang)
                }
            }
        }

        canRegister.set(true)

        RESOURCE_PACK.addLang(Global.id("en_us"), lang)

        if (FabricLoader.getInstance().isDevelopmentEnvironment) RESOURCE_PACK.dump()

        RRPCallback.AFTER_VANILLA.register { it.add(RESOURCE_PACK) }
    }

    @Suppress("ControlFlowWithEmptyBody")
    fun autoRegisterChips() {
        // Safety to make sure we arent registering stuff in the middle of resource generation
        while (!canRegister.get()) {
        }

        toAutoRegister.forEach {
            val type = it.name.removePrefix("CHIP_").lowercase()
            val itemId = Global.id("chip_$type")
            Registry.register(Registry.ITEM, itemId, it.get(Global.ITEMS).assertType())
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

    private fun makeChip(name: String, lang: JLang) {
        val type = name.removePrefix("CHIP_").lowercase()
        val itemId = Global.id("chip_$type")

        // Model
        RESOURCE_PACK.addModel(
            JModel
                .model()
                .parent("item/generated")
                .textures(
                    JTextures()
                        .layer0("jabber:item/chips/chip_$type")
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
