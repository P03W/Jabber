package mc.jabber.core.chips.pipes.corners

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

@ChipID("chip_quad_1_pipe")
class Quad1PipeChip : ChipProcess() {
    override val id = Global.id("quad1")
    override val receiveDirections = DirBitmask.DOWN and DirBitmask.LEFT
    override val sendDirections = DirBitmask.UP and DirBitmask.RIGHT


    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData {
        return CardinalData(data.left, null, null, data.down)
    }
}
