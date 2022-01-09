package mc.jabber.core.chips.pipes.corners

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipParams
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

@ChipID("chip_quad_3_pipe")
class Quad3PipeChip(buildParams: ChipParams) : ChipProcess(buildParams) {
    override val id = Global.id("quad3")
    override val receiveDirections = DirBitmask.UP and DirBitmask.RIGHT
    override val sendDirections = DirBitmask.DOWN and DirBitmask.LEFT

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        return CardinalData(null, data.right, data.up, null)
    }
}
