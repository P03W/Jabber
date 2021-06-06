package mc.jabber.items.chips

import mc.jabber.data.CardinalData
import mc.jabber.items.chips.abstracts.ChipItem

class PipeChip : ChipItem() {
    override fun <T> receive(data: CardinalData<T>): CardinalData<T> {
        return data.of(data.down, data.up, data.right, data.left)
    }
}
