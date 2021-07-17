package mc.jabber.core.math

import kotlinx.serialization.Serializable
import mc.jabber.core.data.serial.NbtTransformable
import net.minecraft.nbt.NbtCompound
import java.nio.ByteBuffer
import kotlin.math.sqrt

/**
 * A simple 2 int class with math operators
 */
@Serializable
data class Vec2I(var x: Int, var y: Int) : NbtTransformable<Vec2I> {

    /*

    Self compare

     */

    operator fun plus(other: Vec2I): Vec2I = Vec2I(x + other.x, y + other.y)
    operator fun plusAssign(other: Vec2I) {
        x += other.x
        y += other.y
    }

    operator fun minus(other: Vec2I): Vec2I = Vec2I(x - other.x, y - other.y)
    operator fun minusAssign(other: Vec2I) {
        x -= other.x
        y -= other.y
    }

    operator fun times(other: Vec2I): Vec2I = Vec2I(x * other.x, y * other.y)
    operator fun timesAssign(other: Vec2I) {
        x *= other.x
        y *= other.y
    }

    operator fun div(other: Vec2I): Vec2I = Vec2I(x / other.x, y / other.y)
    operator fun divAssign(other: Vec2I) {
        x /= other.x
        y /= other.y
    }

    operator fun compareTo(other: Vec2I): Int {
        val myLen = sqrt((x * x + y * y).toDouble())
        val otherLen = sqrt((other.x * other.x + other.y * other.y).toDouble())
        return myLen.compareTo(otherLen)
    }

    /*

    Cardinal compare

     */

    operator fun plus(other: Cardinal): Vec2I = plus(other.vec)
    operator fun plusAssign(other: Cardinal) = plusAssign(other.vec)

    operator fun minus(other: Cardinal): Vec2I = minus(other.vec)
    operator fun minusAssign(other: Cardinal) = minusAssign(other.vec)

    operator fun times(other: Cardinal): Vec2I = times(other.vec)
    operator fun timesAssign(other: Cardinal) = timesAssign(other.vec)

    operator fun compareTo(other: Cardinal): Int = compareTo(other.vec)

    override fun toNbt(): NbtCompound {
        val out = NbtCompound()
        val buffer = ByteBuffer.allocate(16)
        buffer.putInt(x)
        buffer.putInt(y)

        out.putByteArray("p", buffer.array())
        return out
    }

    override fun fromNbt(nbt: NbtCompound): Vec2I {
        val array = nbt.getByteArray("p")
        val buffer = ByteBuffer.wrap(array)

        x = buffer.int
        y = buffer.int
        return Vec2I(x, y)
    }

    override fun type(): Byte {
        return 2
    }

    fun transformInto(sizedDimension: Int): Int {
        return y * sizedDimension + x
    }

    companion object {
        val ZERO = Vec2I(0, 0)

        fun transformOut(value: Int, sizedDimension: Int): Vec2I {
            return Vec2I(value % sizedDimension, value / sizedDimension)
        }
    }
}
