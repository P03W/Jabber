package mc.jabber.data

import mc.jabber.items.chips.abstracts.ChipItem
import mc.jabber.math.Vec2I

data class CircuitBoard(val sizeX: Int, val sizeY: Int) {
    init {
        assert(sizeY % 2 == 1) { "Attempted to create a CircuitBoard with an even amount of rows! ($sizeY)" }
    }

    private val board = Array<Array<ChipItem?>>(sizeY) { Array(sizeX) { null } }

    val size = sizeX * sizeY

    operator fun get(x: Int, y: Int): ChipItem? = board[y][x]
    operator fun get(vec: Vec2I): ChipItem? = get(vec.x, vec.y)

    operator fun set(vec: Vec2I, chipItem: ChipItem?) {
        board[vec.y][vec.x] = chipItem
    }

    operator fun set(x: Int, y: Int, chipItem: ChipItem?) {
        board[y][x] = chipItem
    }

    fun isInBounds(x: Int, y: Int): Boolean = (x >= 0 && y >= 0) && (x < sizeX && y < sizeY)
    fun isInBounds(vec: Vec2I) = isInBounds(vec.x, vec.y)
}
