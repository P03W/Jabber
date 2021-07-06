package mc.jabber.core.math

/**
 * Simple 4 value cardinal direction enum
 */
enum class Cardinal(val vec: Vec2I) {
    UP(Vec2I(0, -1)),
    DOWN(Vec2I(0, 1)),
    LEFT(Vec2I(-1, 0)),
    RIGHT(Vec2I(1, 0));

    /**
     * Flips the direction
     * @return The opposite of the Cardinal, so up returns down, ect.
     */
    fun mirror(): Cardinal {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}
