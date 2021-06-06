package mc.jabber.data

import mc.jabber.items.chips.abstracts.ChipItem
import mc.jabber.math.Vec2I

data class CircuitBoard(val sizeX: Int, val sizeY: Int) {
    private val board = Array<Array<ChipItem?>>(sizeY) { Array(sizeX) { null } }

    val size = sizeX * sizeY

    fun circuitAt(x: Int, y: Int): ChipItem? {
        return board[y][x]
    }

    fun circuitAt(vec: Vec2I): ChipItem? {
        return circuitAt(vec.x, vec.y)
    }
}
