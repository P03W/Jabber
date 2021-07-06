package mc.jabber.core.chips.pipes.corners

import mc.jabber.core.chips.abstract.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

class Quad1PipeChip : ChipProcess() {
    override fun <T : NbtTransformable> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        state: HashMap<Vec2I, Any>
    ): CardinalData<T> {
        return data.of(data.left, null, null, data.down)
    }
}
