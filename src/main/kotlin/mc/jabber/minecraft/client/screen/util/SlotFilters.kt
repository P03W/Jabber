package mc.jabber.minecraft.client.screen.util

import mc.jabber.minecraft.items.ChipItem
import mc.jabber.minecraft.items.CircuitItem
import net.minecraft.item.ItemStack
import java.util.function.Predicate

object SlotFilters {
    val ChipOnly = Predicate<ItemStack> { item: ItemStack -> item.item is ChipItem }
    val CircuitOnly = Predicate<ItemStack> { item: ItemStack -> item.item is CircuitItem }
    val ChipOrCircuit = Predicate<ItemStack> { item: ItemStack -> ChipOnly.test(item) || CircuitOnly.test(item) }
}
