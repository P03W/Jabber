package data

import mc.jabber.data.util.TriSet
import org.junit.jupiter.api.Test

class TriSetTest {
    @Test
    fun testTriSetEquality() {
        val triSetA = TriSet(4, "asd", 0.02)
        val triSetB = TriSet(4, "asd", 0.02)
        val triSetC = triSetA.copy()
        val triSetD = TriSet(4, "asd", "123")

        assert(triSetA == triSetB)
        assert(triSetA == triSetC)

        assert(triSetA != triSetD)
        assert(triSetB != triSetD)
        assert(triSetC != triSetD)
    }
}
