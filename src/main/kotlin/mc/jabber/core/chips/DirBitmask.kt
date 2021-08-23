package mc.jabber.core.chips

import mc.jabber.core.chips.DirBitmask.Companion.DYNAMIC

/**
 * A bitmask of values used for chips
 *
 * If [DYNAMIC] is set, the remaining flags should be ignored, and ideally be set to 0 / [NONE] for consistency
 */
class DirBitmask(private val i: Int) {
    operator fun plus(other: DirBitmask): Int {
        return this.i + other.i
    }

    operator fun Int.plus(bitmask: DirBitmask): Int {
        return this + bitmask.i
    }

    fun matches(i: Int): Boolean {
        return this.i and i != 0
    }

    companion object {
        const val NONE    = 0b00000
        const val UP      = 0b00001
        const val DOWN    = 0b00010
        const val LEFT    = 0b00100
        const val RIGHT   = 0b01000
        const val ALL     = 0b01111
        const val DYNAMIC = 0b10000
    }
}
