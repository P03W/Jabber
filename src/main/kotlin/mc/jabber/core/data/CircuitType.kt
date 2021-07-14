package mc.jabber.core.data

/**
 * An enum representing the type of circuit this is
 */
enum class CircuitType(val templateData: CardinalData<*>) {
    COMPUTE(ComputeData(null, null, null, null)),
}
