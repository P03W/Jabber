package mc.jabber.core.auto

/**
 * Causes a chip with a 0 parameter constructor to automatically be registered with [id]
 *
 * @param id The ID of the item to be automatically registered
 */
@Target(AnnotationTarget.CLASS)
annotation class ChipID(val id: String)
