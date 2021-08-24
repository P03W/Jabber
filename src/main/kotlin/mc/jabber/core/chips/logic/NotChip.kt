package mc.jabber.core.chips.logic

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

@ChipID("chip_not")
class NotChip : ChipProcess() {
    override val id = Global.id("not")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.ALL

    override fun receive(data: CardinalData, pos: Vec2I, chipData: HashMap<Vec2I, NbtTransformable<*>>): CardinalData {
        return CardinalData(data.down?.not(), data.up?.not(), data.right?.not(), data.left?.not())
    }

    private fun Long.not(): Long = if (this == 0L) 1 else 0
}
