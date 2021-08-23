package math

import mc.jabber.core.math.Cardinal
import mc.jabber.core.math.Vec2I
import org.junit.jupiter.api.Test

class Vec2IMathTest {
    val vecA = Vec2I(2, -3)
    val vecB = Vec2I(3, -3)

    @Test
    fun testVec2IEquality() {
        val vecA = Vec2I(123, -456)
        val vecB = Vec2I(123, -456)
        val vecC = Vec2I(-0, 35)

        assert(vecA == vecB)
        assert(vecA != vecC)
    }

    @Test
    fun testVecComparison() {
        assert(vecB > vecA)
        assert(vecB >= vecA)
        assert(vecA < vecB)
        assert(vecA <= vecB)
    }

    @Test
    fun testVecSelfMath() {
        assert(vecA + vecB == Vec2I(5, -6))
        assert(vecA - vecB == Vec2I(-1, 0))
        assert(vecA * vecB == Vec2I(6, 9))
        assert(vecA / vecB == Vec2I(2/3, 1))
        assert(vecB * Vec2I.ZERO == Vec2I.ZERO)
    }

    @Test
    fun testVecCardinalMath() {
        assert(vecA + Cardinal.UP == vecA - Cardinal.DOWN)
        assert(vecA + Cardinal.LEFT == vecA - Cardinal.RIGHT)
        assert(vecA * Cardinal.UP == Vec2I(0, 3))
        assert(vecB > Cardinal.DOWN)
    }
}
