package mc.jabber.core.chips

/**
 * A bitmask of values used for chips
 */
data class DirBitmask(val mask: Int) {
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
        val NONE    = DirBitmask(0b0000)
        val UP      = DirBitmask(0b0001)
        val DOWN    = DirBitmask(0b0010)
        val LEFT    = DirBitmask(0b0100)
        val RIGHT   = DirBitmask(0b1000)
        val ALL     = DirBitmask(0b1111)
    }
}
