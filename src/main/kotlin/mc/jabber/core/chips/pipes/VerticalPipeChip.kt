package mc.jabber.core.chips.pipes

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

/**
 * Pipes the vertical data
 */
@ChipID("chip_vertical_pipe")
class VerticalPipeChip : ChipProcess() {
    override val id = Global.id("vert")
    override val receiveDirections = DirBitmask.UP and DirBitmask.DOWN
    override val sendDirections = DirBitmask.UP and DirBitmask.DOWN

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData {
        return CardinalData(data.up, data.down, null, null)
    }
}
