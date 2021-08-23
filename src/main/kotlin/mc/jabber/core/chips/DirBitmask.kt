package mc.jabber.core.chips

import mc.jabber.core.chips.DirBitmask.Companion.DYNAMIC

/**
 * A bitmask of values used for chips
 *
 * If [DYNAMIC] is set, the remaining flags should be ignored, and ideally be set to 0 / [NONE] for consistency
 */
class DirBitmask(val mask: Int) {
    infix fun and(other: DirBitmask): DirBitmask {
        return DirBitmask(this.mask + other.mask)
    }

    fun matches(i: Int): Boolean {
        return this.mask and i == this.mask
    }

    fun matches(i: DirBitmask): Boolean {
        return this.mask and +i == this.mask
    }

    operator fun unaryPlus() = this.mask

    companion object {
        val NONE    = DirBitmask(0b00000)
        val UP      = DirBitmask(0b00001)
        val DOWN    = DirBitmask(0b00010)
        val LEFT    = DirBitmask(0b00100)
        val RIGHT   = DirBitmask(0b01000)
        val ALL     = DirBitmask(0b01111)
        val DYNAMIC = DirBitmask(0b10000)
    }
}
