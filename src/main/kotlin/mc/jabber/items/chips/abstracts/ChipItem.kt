package mc.jabber.items.chips.abstracts

import mc.jabber.data.CardinalData
import mc.jabber.math.Vec2I
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item

abstract class ChipItem : Item(FabricItemSettings()) {
    abstract fun <T> receive(data: CardinalData<T>, pos: Vec2I, state: MutableMap<Vec2I, Any>): CardinalData<T>

    // TODO: Remove this and let it fallback to vanilla behavior
    override fun toString(): String {
        return this::class.simpleName ?: "ChipItem"
    }
}
