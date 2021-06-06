package mc.jabber.data

import mc.jabber.math.Cardinal

sealed class CardinalData<T>(val up: T?, val down: T?, val left: T?, val right: T?) {
    operator fun get(direction: Cardinal): T? {
        return when (direction) {
            Cardinal.UP -> up
            Cardinal.DOWN -> down
            Cardinal.LEFT -> left
            Cardinal.RIGHT -> right
        }
    }

    fun with(direction: Cardinal, value: T?): CardinalData<T> {
        return when (direction) {
            Cardinal.UP -> of(value, down, left, right)
            Cardinal.DOWN -> of(up, value, left, right)
            Cardinal.LEFT -> of(up, down, value, right)
            Cardinal.RIGHT -> of(up, down, left, value)
        }
    }

    fun of(up: T?, down: T?, left: T?, right: T?): CardinalData<T> {
        return this::class.constructors.first().call(up, down, left, right)
    }

    fun ofAll(value: T?): CardinalData<T> {
        return of(value, value, value, value)
    }

    override fun toString(): String {
        return this::class.simpleName + "(up=$up, down=$down, left=$left, right=$right)"
    }
}

class ComputeData(up: Long?, down: Long?, left: Long?, right: Long?) : CardinalData<Long>(up, down, left, right)
