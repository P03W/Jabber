package mc.jabber.core.chips.pipes.corners

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.cardinal.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import net.minecraft.util.Identifier

@ChipID("chip_quad_3_pipe")
class Quad3PipeChip : ChipProcess() {
    override val id: Identifier = Global.id("quad3")
    override fun <T : NbtTransformable<*>> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData<T> {
        return data.of(null, data.right, data.up, null)
    }
}
