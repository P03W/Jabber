package mc.jabber.util.error

/**
 * And error that states that data was formatted in a known way, but not validly
 */
class InvalidDataFormatException(override val message: String?) : RuntimeException()
