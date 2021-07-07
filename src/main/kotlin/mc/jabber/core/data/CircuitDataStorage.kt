package mc.jabber.core.data

import mc.jabber.core.math.Vec2I

class CircuitDataStorage(val sizeX: Int, val sizeY: Int) : ArrayList<CardinalData<*>?>(sizeX * sizeY) {
    val totalSize = sizeX * sizeY
    init {
        for (i in 0..totalSize) {
            add(null)
        }
    }

    inline fun forEach(action: (Vec2I, CardinalData<*>)->Unit) {
        forEachIndexed { i, data ->
            if (data != null) {
                action(indexToVec(i), data)
            }
        }
    }

    operator fun set(vec2I: Vec2I, data: CardinalData<*>) {
        this[vecToIndex(vec2I)] = data
    }

    operator fun get(vec2I: Vec2I): CardinalData<*>? {
        return this[vecToIndex(vec2I)]
    }

    fun indexToVec(index: Int): Vec2I {
        return Vec2I(index / sizeX, index % sizeX)
    }

    fun vecToIndex(vec2I: Vec2I): Int {
        return vec2I.y * sizeX + vec2I.x
    }

    override fun clear() {
        for (i in 0..totalSize) {
            this[i] = null
        }
    }
}
