package mc.jabber.core.chips.pipes.corners

import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

class Quad2PipeChip : ChipProcess() {
    override fun <T : NbtTransformable> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        chipData: HashMap<Vec2I, Any>
    ): CardinalData<T> {
        return data.of(data.right, null, data.down, null)
    }
}
