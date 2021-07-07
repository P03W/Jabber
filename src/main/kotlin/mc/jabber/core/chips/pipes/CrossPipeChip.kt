package mc.jabber.core.chips.pipes

import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.math.Vec2I

class CrossPipeChip : ChipProcess() {
    override fun <T : NbtTransformable> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable>
    ): CardinalData<T> {
        return data.of(data.down, data.up, data.right, data.left)
    }
}
