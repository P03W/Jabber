package mc.jabber.minecraft.client.screen.util

import mc.jabber.minecraft.items.ChipItem
import mc.jabber.minecraft.items.CircuitItem
import net.minecraft.item.ItemStack
import java.util.function.Predicate

object SlotFilters {
    val ChipOnly = { item: ItemStack -> item.item is ChipItem }
    val CircuitOnly = { item: ItemStack -> item.item is CircuitItem }
    val ChipOrCircuit = { item: ItemStack -> ChipOnly(item) || CircuitOnly(item) }
}
