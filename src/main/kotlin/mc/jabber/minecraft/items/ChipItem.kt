package mc.jabber.minecraft.items

import mc.jabber.Global
import mc.jabber.core.chips.ChipProcess
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

/**
 * A chip, that provides an abstract process that can be called
 */
class ChipItem(val process: ChipProcess) : Item(FabricItemSettings().group(Global.ITEMS.ITEM_GROUP)) {
    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext?
    ) {
        val nbt = stack.getOrCreateSubNbt("params").takeUnless { it.isEmpty } ?: process.params.writeToNbt()
        process.lore.forEach {
            tooltip.add(LiteralText(convertLoreEntry(it, nbt)).formatted(Formatting.GRAY))
        }

        if (!nbt.isEmpty && process.lore.isNotEmpty())
            tooltip.add(LiteralText(""))

        nbt.keys.forEach {
            tooltip.add(LiteralText("$it = ${nbt.get(it)}"))
        }
    }

    private fun convertLoreEntry(text: String, params: NbtCompound): String {
        val matches = REGEX.findAll(text)
        var working = text
        matches.forEach {
            it.groups.forEach next@{ group ->
                val key = group?.value ?: return@next
                val value = params.get(key)
                if (value != null) {
                    working = working.replace("%$key%", value.asString())
                }
            }
        }
        return working
    }

    companion object {
        val REGEX = Regex("%(.+)%")
    }
}
