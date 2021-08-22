package mc.jabber.core.chips.pipes

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import net.minecraft.util.Identifier

@ChipID("chip_horizontal_pipe")
class HorizontalPipeChip : ChipProcess() {
    override val id = Global.id("horiz")

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData {
        return CardinalData(null, null, data.left, data.right)
    }
}
