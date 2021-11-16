package mc.jabber.core.auto

/**
 * Causes a chip with a 0 parameter constructor to automatically be registered with [id]
 *
 * @param id The ID of the item to be automatically registered
 * @param name The lang entry for this item
 */
@Target(AnnotationTarget.CLASS)
annotation class ChipID(val id: String, val name: String = "")
