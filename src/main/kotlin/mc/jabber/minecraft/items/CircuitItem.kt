package mc.jabber.minecraft.items

import mc.jabber.Global
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

/**
 * An item that holds a circuit, with a small amount of fancy text
 */
class CircuitItem(val sizeX: Int, val sizeY: Int) :
    Item(FabricItemSettings().maxCount(1).group(Global.ITEMS.ITEM_GROUP)) {
    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        val size = "${stack.getSubNbt("components")?.getByteArray("circuit")?.size?.toString() ?: "0"} Bytes"
        tooltip.add(LiteralText(size).formatted(Formatting.GRAY))
    }
}
