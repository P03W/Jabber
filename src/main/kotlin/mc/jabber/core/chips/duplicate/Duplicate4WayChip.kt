package mc.jabber.core.chips.duplicate

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

/**
 * Takes in an input and outputs on all not received channels
 */
@ChipID("chip_duplicate_4_way")
class Duplicate4WayChip : ChipProcess() {
    override val id = Global.id("dup4")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.ALL

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?
    ): CardinalData {
        val got = data.acquire() ?: return data.ofAll(null)
        return data.outputNotReceived(got.second)
    }
}
