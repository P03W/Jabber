package mc.jabber.core.chips.constant

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

/**
 * Always outputs 0 in all directions
 */
@ChipID("chip_constant_0")
class Constant0Chip : ChipProcess() {
    override val id = Global.id("const0")
    override val isInput = true
    override val receiveDirections = DirBitmask.NONE
    override val sendDirections = DirBitmask.ALL

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?
    ): CardinalData {
        return CardinalData(0, 0, 0, 0)
    }
}
