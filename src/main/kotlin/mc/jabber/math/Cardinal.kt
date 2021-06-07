package mc.jabber.math

enum class Cardinal(val vec: Vec2I) {
    UP(Vec2I(0, 1)),
    DOWN(Vec2I(0, -1)),
    LEFT(Vec2I(-1, 0)),
    RIGHT(Vec2I(1, 0));

    fun mirror(): Cardinal {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}
