package mc.jabber.core.data

import mc.jabber.core.math.Vec2I

/**
 * A wrapper over [ArrayList]<[CardinalData]<*>?> that provides indexing and retrieval using [Vec2I]s as keys (in map form)
 *
 * @param sizeX The X dimension, hidden as its used later, but sizeY is not stored as it does not need to be used in later computation, hence [sizeX] is private to avoid confusion
 * @param sizeY The Y dimension, only used for initial size allocations
 */
class CircuitDataStorage(private val sizeX: Int, sizeY: Int){
    val totalSize = sizeX * sizeY
    val array: Array<CardinalData<*>?> = Array(sizeX * sizeY) {null}

    /**
     * Iterates over the collection as if it was a map
     *
     * Skips null values, and provides a Vec2I instead of the raw index
     */
    inline fun forEach(action: (Vec2I, CardinalData<*>)->Unit) {
        array.forEachIndexed { i, data ->
            if (data != null) {
                action(indexToVec(i), data)
            }
        }
    }

    /**
     * Sets the value at [index] to [data]
     *
     * [index] in implicitly converted to a raw index through [vecToIndex]
     */
    operator fun set(index: Vec2I, data: CardinalData<*>) {
        array[vecToIndex(index)] = data
    }

    operator fun get(index: Vec2I): CardinalData<*>? {
        return array[vecToIndex(index)]
    }

    /**
     * Expands the [index] against the backing array using the initially given [sizeX]
     */
    fun indexToVec(index: Int): Vec2I {
        return Vec2I(index % sizeX, index / sizeX)
    }

    /**
     * Flattens [vec2I] against the backing array using the initially given [sizeX]
     */
    fun vecToIndex(vec2I: Vec2I): Int {
        return vec2I.y * sizeX + vec2I.x
    }

    /**
     * Clear out any data
     */
    fun clear() {
        for (i in array.indices) {
            array[i] = null
        }
    }
}
