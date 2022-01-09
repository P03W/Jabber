package mc.jabber.core.chips.pipes

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipParams
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

/**
 * Pipes data from any direction
 */
@ChipID("chip_cross_pipe", "Cross Over Pipe")
class CrossPipeChip(buildParams: ChipParams) : ChipProcess(buildParams) {
    override val id = Global.id("cross")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.ALL

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        return CardinalData(data.down, data.up, data.right, data.left)
    }
}
