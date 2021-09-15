package mc.jabber.core.chips.pipes

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

/**
 * Pipes the horizontal data
 */
@ChipID("chip_horizontal_pipe")
class HorizontalPipeChip : ChipProcess() {
    override val id = Global.id("horiz")
    override val receiveDirections = DirBitmask.LEFT and DirBitmask.RIGHT
    override val sendDirections = DirBitmask.LEFT and DirBitmask.RIGHT

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?
    ): CardinalData {
        return CardinalData(null, null, data.left, data.right)
    }
}
