package mc.jabber.items.chips

import mc.jabber.data.CardinalData
import mc.jabber.items.chips.abstracts.ChipItem
import mc.jabber.math.Vec2I

class PipeChip : ChipItem() {
    override fun <T> receive(data: CardinalData<T>, pos: Vec2I, state: MutableMap<Vec2I, Any>): CardinalData<T> {
        val got = data.acquire() ?: return data.ofAll(null)
        return data.ofAll(got.second).with(got.first, null)
    }
}
