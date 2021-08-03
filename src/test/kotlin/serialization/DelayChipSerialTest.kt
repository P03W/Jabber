package serialization

import mc.jabber.core.data.util.TriSet
import mc.jabber.core.chips.special.DelayChip
import mc.jabber.core.math.Cardinal
import org.junit.jupiter.api.Test
import kotlin.random.Random

class DelayChipSerialTest {
    @Test
    fun testDelayChipSerial() {
        val state = DelayChip.DelayState()

        val random = Random(21095478)

        val expectedA = TriSet(random.nextInt(), Cardinal.LEFT, random.nextLong())
        val expectedB = TriSet(random.nextInt(), Cardinal.UP, random.nextLong())
        val expectedC = TriSet(random.nextInt(), Cardinal.DOWN, random.nextLong())

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
