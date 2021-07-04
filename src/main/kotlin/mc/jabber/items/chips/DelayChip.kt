package mc.jabber.items.chips

import mc.jabber.data.CardinalData
import mc.jabber.data.util.TriSet
import mc.jabber.items.chips.abstracts.ChipItem
import mc.jabber.math.Cardinal
import mc.jabber.math.Vec2I
import mc.jabber.util.assertType

class DelayChip(val delay: Int) : ChipItem() {
    override fun makeInitialStateEntry(): Any {
        return mutableListOf<TriSet<Int, Cardinal, Any>>()
    }

    override fun <T> receive(data: CardinalData<T>, pos: Vec2I, state: MutableMap<Vec2I, Any>): CardinalData<T> {
        // Grab the queue
        val queue = state[pos].assertType<MutableList<TriSet<Int, Cardinal, Any>>>()

        // Add the received values to the queue
        data.forEach { cardinal, t ->
            if (t != null) {
                queue.add(TriSet(delay, cardinal, t))
            }
        }

        // Build the output
        val out = data.ofAll(null)
        queue.forEach {
            it.first--
            if (it.first <= 0) {
                out.with(it.second, it.third)
            }
        }

        state[pos] = queue.filter { it.first > 0 }

        return out
    }
}
