package mc.jabber.core.data

import mc.jabber.proto.CircuitManagerBuffer

/**
 * An enum representing the type of circuit this is
 */
enum class CircuitType(val templateData: CardinalData<*>) {
    COMPUTE(ComputeData(null, null, null, null));

    fun toProto(): CircuitManagerBuffer.CircuitManager.Type {
        return when (this) {
            COMPUTE -> CircuitManagerBuffer.CircuitManager.Type.COMPUTE
        }
    }

    companion object {
        fun fromProto(type: CircuitManagerBuffer.CircuitManager.Type): CircuitType {
            return when (type) {
                CircuitManagerBuffer.CircuitManager.Type.COMPUTE -> COMPUTE
                CircuitManagerBuffer.CircuitManager.Type.UNRECOGNIZED -> throw IllegalStateException("Did not recognize enum of circuit type")
            }
        }
    }
}
