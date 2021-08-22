package mc.jabber.core.chips.logic

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import net.minecraft.util.Identifier

@ChipID("chip_logical_or")
class OrChip : ChipProcess() {
    override val id: Identifier = Global.id("or")

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData {
        return if (data.any { _, t -> t != null && t > 0}) {
            data.replaceNull(1)
        } else {
            data.replaceNull(0)
        }
    }
}
