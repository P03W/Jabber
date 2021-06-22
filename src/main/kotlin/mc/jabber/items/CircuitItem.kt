package mc.jabber.items

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.world.World

class CircuitItem(val sizeX: Int, val sizeY: Int) : Item(FabricItemSettings().maxCount(1)) {
    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        tooltip.add(LiteralText("${sizeX}x$sizeY").formatted(Formatting.GRAY))
    }
}
