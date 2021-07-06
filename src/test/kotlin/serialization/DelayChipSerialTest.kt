package serialization

import mc.jabber.core.data.serial.LongBox
import mc.jabber.core.data.util.TriSet
import mc.jabber.core.chips.special.DelayChip
import mc.jabber.core.math.Cardinal
import org.junit.jupiter.api.Test
import kotlin.random.Random

class DelayChipSerialTest {
    @Test
    fun testDelayChipSerial() {
        println("Hello!")
        println("This is *very* slow on a cold JVM")
        println("The implementation is actually quite fast normally")
        println("If you duplicate this test, it'll take barely any time")

        val state = DelayChip.DelayState()

        val random = Random(21095478)

        val expectedA = TriSet(random.nextInt().toShort(), Cardinal.LEFT, LongBox(random.nextLong()))
        val expectedB = TriSet(random.nextInt().toShort(), Cardinal.UP, LongBox(random.nextLong()))
        val expectedC = TriSet(random.nextInt().toShort(), Cardinal.DOWN, LongBox(random.nextLong()))

        state.data.add(expectedA)
        state.data.add(expectedB)
        state.data.add(expectedC)

        // Make sure we can serialize
        val compound = state.toNbt()

        // Make sure we can de-serialize
        val result = DelayChip.DelayState().also { it.fromNbt(compound) }

        // Make sure the data is the same
        assert(result.data[0] == expectedA) { "Expected $expectedA but got ${result.data[0]}" }
        assert(result.data[1] == expectedB) { "Expected $expectedB but got ${result.data[1]}" }
        assert(result.data[2] == expectedC) { "Expected $expectedC but got ${result.data[2]}" }
    }
}
