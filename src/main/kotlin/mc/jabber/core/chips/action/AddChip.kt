package mc.jabber.core.chips.action

import mc.jabber.Global
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import mc.jabber.util.assertType
import net.minecraft.util.Identifier

class AddChip(val amount: Int) : ChipProcess() {
    override val id = Global.id("add")
    override fun receive(

        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData {
        val up = data.up?.let { it + amount }
        val down = data.down?.let { it + amount }
        val left = data.left?.let { it + amount }
        val right = data.right?.let { it + amount }
        return CardinalData(down, up, right, left).assertType()
    }
}
