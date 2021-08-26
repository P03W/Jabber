package mc.jabber.core.math

import mc.jabber.core.chips.DirBitmask

/**
 * Simple 4 value cardinal direction enum
 */
enum class Cardinal(val vec: Vec2I, val mask: DirBitmask) {
    UP(Vec2I(0, -1), DirBitmask.UP),
    DOWN(Vec2I(0, 1), DirBitmask.DOWN),
    LEFT(Vec2I(-1, 0), DirBitmask.LEFT),
    RIGHT(Vec2I(1, 0), DirBitmask.RIGHT);

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
