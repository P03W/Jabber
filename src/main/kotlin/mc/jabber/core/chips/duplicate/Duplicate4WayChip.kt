package mc.jabber.core.chips.duplicate

import mc.jabber.Global
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import net.minecraft.util.Identifier

class Duplicate4WayChip : ChipProcess() {
    override val id: Identifier = Global.id("dup4")
    override fun <T : NbtTransformable<*>> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData<T> {
        val got = data.acquire() ?: return data.ofAll(null)
        return data.ofAll(got.second).with(got.first.mirror(), null)
    }
}
