package mc.jabber.core.chips.special

import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import mc.jabber.util.assertType
import net.minecraft.util.Identifier

class CustomChip(
    override val id: Identifier,
    override val isInput: Boolean = false,
    private val method: (CardinalData<*>, Vec2I, HashMap<Vec2I, NbtTransformable<*>>) -> CardinalData<*>
) :
    ChipProcess() {
    override fun <T : NbtTransformable<*>> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData<T> {
        return method(data, pos, chipData).assertType()
    }
}
