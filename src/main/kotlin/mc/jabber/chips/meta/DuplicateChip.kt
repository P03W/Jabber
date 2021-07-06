package mc.jabber.chips.meta

import mc.jabber.data.CardinalData
import mc.jabber.data.serial.NbtTransformable
import mc.jabber.chips.abstracts.ChipProcess
import mc.jabber.math.Vec2I

class DuplicateChip : ChipProcess() {
    override fun <T : NbtTransformable> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        state: MutableMap<Vec2I, Any>
    ): CardinalData<T> {
        val got = data.acquire() ?: return data.ofAll(null)
        return data.ofAll(got.second).with(got.first.mirror(), null)
    }
}
