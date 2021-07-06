package mc.jabber.chips.action

import mc.jabber.chips.abstract.ChipProcess
import mc.jabber.data.CardinalData
import mc.jabber.data.ComputeData
import mc.jabber.data.serial.NbtTransformable
import mc.jabber.math.Vec2I
import mc.jabber.util.assertType

class AddChip(val amount: Long) : ChipProcess() {
    override fun <T : NbtTransformable> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        state: HashMap<Vec2I, Any>
    ): CardinalData<T> {
        return when (data) {
            is ComputeData -> {
                val up = data.up?.also { it += amount }
                val down = data.down?.also { it += amount }
                val left = data.left?.also { it += amount }
                val right = data.right?.also { it += amount }
                data.of(down, up, right, left).assertType()
            }
        }
    }
}
