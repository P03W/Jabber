package data

import mc.jabber.core.math.Vec2I

class Vec2ITest {
    fun testVec2IEquality() {
        val vecA = Vec2I(123, -456)
        val vecB = Vec2I(123, -456)
        val vecC = Vec2I(-0, 35)

        assert(vecA == vecB)
        assert(vecA != vecC)
    }
}
