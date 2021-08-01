package mc.jabber.core.auto

@Target(AnnotationTarget.CLASS)
annotation class AutoConstructInts(val id: ChipID, val toConstruct: IntArray)
