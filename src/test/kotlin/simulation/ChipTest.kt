package simulation

import mc.jabber.core.chips.action.AddChip
import mc.jabber.core.chips.duplicate.Duplicate4WayChip
import mc.jabber.core.chips.logic.*
import mc.jabber.core.data.CardinalData
import mc.jabber.core.math.Cardinal
import mc.jabber.core.math.Vec2I
import org.junit.jupiter.api.Test

class ChipTest {
    @Test
    fun testAddChip() {
        assert(AddChip(5).receive(CardinalData(1, 1, 1, 1), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 6L)
        assert(AddChip(2).receive(CardinalData(0, 0, 0, 0), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 2L)
    }

    @Test
    fun testLogicalAndChip() {
        assert(AndChip().receive(CardinalData(1, 1, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
        assert(AndChip().receive(CardinalData(0, 1, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 0L)
        assert(AndChip().receive(CardinalData(0, 0, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 0L)
        assert(AndChip().receive(CardinalData(0, 0, 0, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 0L)
    }

    @Test
    fun testLogicalOrChip() {
        assert(OrChip().receive(CardinalData(1, 1, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
        assert(OrChip().receive(CardinalData(0, 1, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
        assert(OrChip().receive(CardinalData(0, 0, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
        assert(OrChip().receive(CardinalData(0, 0, 0, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 0L)
    }

    @Test
    fun testLogicalNandChip() {
        assert(NandChip().receive(CardinalData(1, 1, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 0L)
        assert(NandChip().receive(CardinalData(0, 1, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
        assert(NandChip().receive(CardinalData(0, 0, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
        assert(NandChip().receive(CardinalData(0, 0, 0, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
    }

    @Test
    fun testLogicalNotChip() {
        assert(NotChip().receive(CardinalData(1, null, null, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 0L)
        assert(NotChip().receive(CardinalData(0, null, null, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
    }

    @Test
    fun testOneHotChip() {
        assert(OneHotChip().receive(CardinalData(1, 0, null, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
        assert(OneHotChip().receive(CardinalData(0, 0, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
        assert(OneHotChip().receive(CardinalData(0, 0, null, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 0L)
    }

    @Test
    fun testLogicalXorChip() {
        assert(XorChip().receive(CardinalData(0, 0, null, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 0L)
        assert(XorChip().receive(CardinalData(1, 0, null, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
        assert(XorChip().receive(CardinalData(0, 0, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 1L)
        assert(XorChip().receive(CardinalData(1, 0, 1, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 0L)
        assert(XorChip().receive(CardinalData(13, 123, 2244, null), Vec2I.ZERO, hashMapOf(), null,).acquire()?.second == 2226L)
    }

    @Test
    fun testDuplicate4Way() {
        val result = Duplicate4WayChip().receive(CardinalData(15456, null, null, null), Vec2I.ZERO, hashMapOf(), null,)
        assert(result[Cardinal.UP] == 15456L)
        assert(result[Cardinal.LEFT] == 15456L)
        assert(result[Cardinal.RIGHT] == 15456L)
        assert(result[Cardinal.DOWN] == null)

        val result2 = Duplicate4WayChip().receive(
            CardinalData(null, null, -15456, null),
            Vec2I.ZERO,
            hashMapOf(),
            null,
        )
        assert(result2[Cardinal.UP] == -15456L)
        assert(result2[Cardinal.LEFT] == -15456L)
        assert(result2[Cardinal.DOWN] == -15456L)
        assert(result2[Cardinal.RIGHT] == null)
    }
}
