package data

import mc.jabber.core.math.Cardinal
import org.junit.jupiter.api.Test

class CardinalTest {
    @Test
    fun testCardinalMirror() {
        assert(Cardinal.UP.mirror() == Cardinal.DOWN)
        assert(Cardinal.DOWN.mirror() == Cardinal.UP)
        assert(Cardinal.LEFT.mirror() == Cardinal.RIGHT)
        assert(Cardinal.RIGHT.mirror() == Cardinal.LEFT)
    }
}
