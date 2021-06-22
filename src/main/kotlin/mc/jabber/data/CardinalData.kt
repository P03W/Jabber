package mc.jabber.data

import mc.jabber.math.Cardinal
import mc.jabber.util.assertType
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed class CardinalData<T>(val up: T?, val down: T?, val left: T?, val right: T?) {
    operator fun get(direction: Cardinal): T? {
        return when (direction) {
            Cardinal.UP -> up
            Cardinal.DOWN -> down
            Cardinal.LEFT -> left
            Cardinal.RIGHT -> right
        }
    }

    /**
     * Returns null if all values are null, or a random value from the non-null ones
     */
    fun acquire(): Pair<Cardinal, T>? {
        val dirs = Cardinal.values()
        dirs.shuffle()
        dirs.forEach {
            val got = get(it)
            if (got != null) return it to got
        }
        return null
    }

    fun with(direction: Cardinal, value: Any?): CardinalData<T> {
        return when (direction) {
            Cardinal.UP -> of(value, down, left, right)
            Cardinal.DOWN -> of(up, value, left, right)
            Cardinal.LEFT -> of(up, down, value, right)
            Cardinal.RIGHT -> of(up, down, left, value)
        }
    }

    fun only(direction: Cardinal): CardinalData<T> {
        return when (direction) {
            Cardinal.UP -> of(up, null, null, null)
            Cardinal.DOWN -> of(null, down, null, null)
            Cardinal.LEFT -> of(null, null, left, null)
            Cardinal.RIGHT -> of(null, null, null, right)
        }
    }

    // Type erasure ruining everything once again
    fun of(up: Any?, down: Any?, left: Any?, right: Any?): CardinalData<T> {
        return when (this) {
            is ComputeData -> ComputeData(up as Long?, down as Long?, left as Long?, right as Long?).assertType()
        }
    }

    fun empty() = ofAll(null)

    fun ofAll(value: Any?): CardinalData<T> {
        return of(value, value, value, value)
    }

    @OptIn(ExperimentalContracts::class)
    inline fun forEach(method: (Cardinal, T?) -> Unit) {
        contract {
            callsInPlace(method, InvocationKind.AT_LEAST_ONCE)
        }

        method(Cardinal.UP, up)
        method(Cardinal.DOWN, down)
        method(Cardinal.LEFT, left)
        method(Cardinal.RIGHT, right)
    }

    override fun toString(): String {
        return this::class.simpleName + "(up=$up, down=$down, left=$left, right=$right)"
    }
}

class ComputeData(up: Long?, down: Long?, left: Long?, right: Long?) : CardinalData<Long>(up, down, left, right)
