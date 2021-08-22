package mc.jabber.core.auto

/**
 * Causes a chip with constructor that only takes an `Int` to automatically be registered with all [toConstruct] passed
 *
 * @param id The ID of the chip, will have `_$int` added for each entry
 * @param toConstruct An array of `Int`s to construct with
 */
@Target(AnnotationTarget.CLASS)
annotation class AutoConstructInt(val id: ChipID, val toConstruct: IntArray)
