package mc.jabber.items.chips

import mc.jabber.data.CardinalData
import mc.jabber.data.util.TriSet
import mc.jabber.items.chips.abstracts.ChipItem
import mc.jabber.math.Cardinal
import mc.jabber.math.Vec2I
import mc.jabber.util.assertType

class DelayChip(val delay: Int) : ChipItem() {
    override fun <T> receive(data: CardinalData<T>, pos: Vec2I, state: MutableMap<Vec2I, Any>): CardinalData<T> {
        // Make sure we did the init
        if (state[pos] == null) state[pos] = mutableListOf<DelayEntry>()

        // Grab the queue
        val queue = state[pos].assertType<DelayQueue>()

        // Add the received values to the queue
        data.forEach { cardinal, t ->
            if (t != null) {
                queue.add(DelayEntry(delay, cardinal, t))
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

private typealias DelayEntry = TriSet<Int, Cardinal, Any>
private typealias DelayQueue = MutableList<DelayEntry>
