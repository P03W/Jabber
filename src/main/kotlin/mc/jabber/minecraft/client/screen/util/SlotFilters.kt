package mc.jabber.minecraft.client.screen.util

import mc.jabber.minecraft.items.ChipItem
import mc.jabber.minecraft.items.CircuitItem
import net.minecraft.item.ItemStack
import java.util.function.Predicate

object SlotFilters {
    val ChipOnly = SlotFilterWrapper { item: ItemStack -> item.item is ChipItem }
    val CircuitOnly = SlotFilterWrapper { item: ItemStack -> item.item is CircuitItem }
}

class SlotFilterWrapper(var predicate: (ItemStack) -> Boolean): Predicate<ItemStack> {
    override fun test(t: ItemStack): Boolean = predicate(t)
    operator fun invoke(t: ItemStack) = test(t)

    infix fun or(other: SlotFilterWrapper): SlotFilterWrapper {
        predicate = { it: ItemStack -> this(it) || other(it) }
        return this
    }

    infix fun and(other: SlotFilterWrapper): SlotFilterWrapper {
        predicate = { it: ItemStack -> this(it) && other(it) }
        return this
    }

    infix fun andNot(other: SlotFilterWrapper): SlotFilterWrapper {
        predicate = { it: ItemStack -> this(it) && !other(it) }
        return this
    }

    infix fun xor(other: SlotFilterWrapper): SlotFilterWrapper {
        predicate = { it: ItemStack -> this(it) xor other(it) }
        return this
    }

    operator fun not(): SlotFilterWrapper {
        predicate = { it: ItemStack -> !this(it) }
        return this
    }
}
