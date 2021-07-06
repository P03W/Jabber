package mc.jabber.util.error

/**
 * An error that states that data was not in a known format
 */
class UnknownDataFormatException(override val message: String?) : RuntimeException()
