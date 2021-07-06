package serialization

import mc.jabber.data.serial.LongBox
import mc.jabber.data.util.TriSet
import mc.jabber.items.chips.DelayChip
import mc.jabber.math.Cardinal
import org.junit.jupiter.api.Test
import kotlin.random.Random

class DelayChipSerialTest {
    @Test
    fun testDelayChipSerial() {
        val state = DelayChip.DelayState()

        val expectedA = TriSet(Random.nextInt().toShort(), Cardinal.values().random(), LongBox(Random.nextLong()))
        val expectedB = TriSet(Random.nextInt().toShort(), Cardinal.values().random(), LongBox(Random.nextLong()))
        val expectedC = TriSet(Random.nextInt().toShort(), Cardinal.values().random(), LongBox(Random.nextLong()))

        state.data.add(expectedA)
        state.data.add(expectedB)
        state.data.add(expectedC)

        // Make sure we can serialize
        val compound = state.toNbt()

        // Make sure we can de-serialize
        val result = state.fromNbt(compound)

        // Make sure the data is the same
        assert(result.data[0] == expectedA) { "Expected $expectedA but got ${result.data[0]}" }
        assert(result.data[1] == expectedB) { "Expected $expectedB but got ${result.data[1]}" }
        assert(result.data[2] == expectedC) { "Expected $expectedC but got ${result.data[2]}" }
    }
}
