package mc.jabber.items.chips.abstracts

import mc.jabber.Global
import mc.jabber.data.CardinalData
import mc.jabber.data.serial.NbtTransformable
import mc.jabber.math.Vec2I
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item

/**
 * A chip, that provides an abstract process that can be called
 */
class ChipItem(val process: ChipProcess) : Item(FabricItemSettings().group(Global.ITEMS.ITEM_GROUP))
