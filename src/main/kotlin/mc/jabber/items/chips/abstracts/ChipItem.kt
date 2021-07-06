package mc.jabber.items.chips.abstracts

import mc.jabber.Global
import mc.jabber.data.CardinalData
import mc.jabber.data.serial.NbtTransformable
import mc.jabber.math.Vec2I
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item

/**
 * An abstract chip, that provides the basic functions for providing and handling data
 */
abstract class ChipItem : Item(FabricItemSettings().group(Global.ITEMS.ITEM_GROUP)) {
    abstract fun <T : NbtTransformable<*>> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        state: MutableMap<Vec2I, Any>
    ): CardinalData<T>

    /**
     * Called once in chip setup, should be serializable to Tag
     *
     * @return The data to be stored by default in the state
     */
    open fun makeInitialStateEntry(): NbtTransformable<*>? {
        return null
    }
}
