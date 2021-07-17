package mc.jabber.core.chips.action

import mc.jabber.Global
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ComputeData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import mc.jabber.util.assertType
import net.minecraft.util.Identifier

class AddChip(val amount: Long) : ChipProcess() {
    override val id: Identifier = Global.id("add")
    override fun <T : NbtTransformable<*>> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData<T> {
        return when (data) {
            is ComputeData -> {
                val up = data.up?.also { it.long += amount }
                val down = data.down?.also { it.long += amount }
                val left = data.left?.also { it.long += amount }
                val right = data.right?.also { it.long += amount }
                data.of(down, up, right, left).assertType()
            }
        }
    }
}
