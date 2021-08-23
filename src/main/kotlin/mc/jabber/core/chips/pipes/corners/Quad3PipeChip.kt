package mc.jabber.core.chips.pipes.corners

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

@ChipID("chip_quad_3_pipe")
class Quad3PipeChip : ChipProcess() {
    override val id = Global.id("quad3")
    override val receiveDirections = DirBitmask.UP + DirBitmask.RIGHT
    override val sendDirections = DirBitmask.DOWN + DirBitmask.LEFT

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData {
        return CardinalData(null, data.right, data.up, null)
    }
}
