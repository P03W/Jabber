package mc.jabber.chips.pipes.corners

import mc.jabber.chips.abstract.ChipProcess
import mc.jabber.data.CardinalData
import mc.jabber.data.serial.NbtTransformable
import mc.jabber.math.Vec2I

class Quad3PipeChip : ChipProcess() {
    override fun <T : NbtTransformable> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        state: HashMap<Vec2I, Any>
    ): CardinalData<T> {
        return data.of(null, data.right, data.up, null)
    }
}
