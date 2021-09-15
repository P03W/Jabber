package mc.jabber.core.chips.logic

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

@ChipID("chip_one_hot")
class OneHotChip : ChipProcess() {
    override val id = Global.id("1hot")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.ALL

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?
    ): CardinalData {
        var count = 0
        data.forEach { _, l ->
            if (l != null && l != 0L) {
                count++
                if (count > 1) {
                    return data.outputNotReceived(0)
                }
            }
        }

        return data.outputNotReceived(count.toLong())
    }
}
