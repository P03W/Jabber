package mc.jabber.items.chips.abstracts

import mc.jabber.Global
import mc.jabber.data.CardinalData
import mc.jabber.math.Vec2I
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item

abstract class ChipItem : Item(FabricItemSettings().group(Global.ITEMS.ITEM_GROUP)) {
    abstract fun <T> receive(data: CardinalData<T>, pos: Vec2I, state: MutableMap<Vec2I, Any>): CardinalData<T>

    open fun makeInitialStateEntry(): Any { return Unit }
}
