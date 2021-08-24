package mc.jabber.core.chips.logic

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

@ChipID("chip_xor")
class XorChip : ChipProcess() {
    override val id = Global.id("xor")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.ALL

    override fun receive(data: CardinalData, pos: Vec2I, chipData: HashMap<Vec2I, NbtTransformable<*>>): CardinalData {
        var count = 0
        data.forEach { _, l ->
            if (l != null && l != 0L) count++
        }

        return data.outputNotReceived((count % 2).toLong())
    }
}
