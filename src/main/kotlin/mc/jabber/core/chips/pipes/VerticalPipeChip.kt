package mc.jabber.core.chips.pipes

import mc.jabber.Global
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import net.minecraft.util.Identifier

class VerticalPipeChip : ChipProcess() {
    override val id: Identifier = Global.id("vertical")
    override fun <T : NbtTransformable<*>> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData<T> {
        return data.of(data.up, data.down, null, null)
    }
}
