package mc.jabber.minecraft.items

import mc.jabber.Global
import mc.jabber.core.chips.ChipProcess
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
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
        process.lore.forEach {
            tooltip.add(LiteralText(it).formatted(Formatting.GRAY))
        }
    }
}
