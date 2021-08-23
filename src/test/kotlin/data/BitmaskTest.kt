package data

import mc.jabber.core.chips.DirBitmask
import org.junit.jupiter.api.Test

class BitmaskTest {
    @Test
    fun testSingle() {
        val mask = +DirBitmask.UP
        assert(DirBitmask.UP.matches(mask))
        assert(!DirBitmask.ALL.matches(mask))
    }

    @Test
    fun testMany() {
        val mask = +(DirBitmask.UP and DirBitmask.DOWN and DirBitmask.DYNAMIC)
        assert(DirBitmask.UP.matches(mask))
        assert(DirBitmask.DOWN.matches(mask))
        assert(DirBitmask.DYNAMIC.matches(mask))
        assert(!DirBitmask.ALL.matches(mask))
    }

    @Test
    fun testCombination() {
        val mask = +(DirBitmask.UP and DirBitmask.DOWN and DirBitmask.LEFT and DirBitmask.RIGHT)
        assert(DirBitmask.UP.matches(mask))
        assert(DirBitmask.DOWN.matches(mask))
        assert(DirBitmask.ALL.matches(mask))
        assert(!DirBitmask.DYNAMIC.matches(mask))
    }
}
