package mc.jabber.core.auto

@Target(AnnotationTarget.CLASS)
annotation class AutoConstructInt(val id: ChipID, val toConstruct: IntArray)
