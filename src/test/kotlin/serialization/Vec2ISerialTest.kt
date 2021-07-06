package serialization

import mc.jabber.core.math.Vec2I
import org.junit.jupiter.api.Test

class Vec2ISerialTest {
    @Test
    fun testVec2ISerial() {
        val vecA = Vec2I(123, -456)
        val vecB = Vec2I(-0, 35)

        val resultA = Vec2I(0, 0).also { it.fromNbt(vecA.toNbt()) }
        val resultB = Vec2I(0, 0).also { it.fromNbt(vecB.toNbt()) }

        assert(vecA == resultA)
        assert(vecB == resultB)
    }
}
