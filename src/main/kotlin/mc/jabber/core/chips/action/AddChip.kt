package mc.jabber.core.chips.action

import mc.jabber.Global
import mc.jabber.core.auto.AutoConstructInt
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

/**
 * Adds to the input and sends it across
 */
@AutoConstructInt(ChipID("chip_add"), [1, 2, 3, 4, 5, 10, 20])
class AddChip(val amount: Int) : ChipProcess() {
    override val id = Global.id("add$amount")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.ALL

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?
    ): CardinalData {
        val up = data.up?.let { it + amount }
        val down = data.down?.let { it + amount }
        val left = data.left?.let { it + amount }
        val right = data.right?.let { it + amount }
        return CardinalData(up, down, left, right)
    }
}
