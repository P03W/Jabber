package mc.jabber.core.data

import mc.jabber.proto.CircuitManagerBuffer

/**
 * An enum representing the type of circuit this is
 */
enum class CircuitType(val templateData: CardinalData<*>) {
    COMPUTE(ComputeData(null, null, null, null));

    fun toProto(): CircuitManagerBuffer.CircuitManagerProto.Type {
        return when (this) {
            COMPUTE -> CircuitManagerBuffer.CircuitManagerProto.Type.COMPUTE
        }
    }

    companion object {
        fun fromProto(type: CircuitManagerBuffer.CircuitManagerProto.Type): CircuitType {
            return when (type) {
                CircuitManagerBuffer.CircuitManagerProto.Type.COMPUTE -> COMPUTE
                CircuitManagerBuffer.CircuitManagerProto.Type.UNRECOGNIZED -> throw IllegalStateException("Did not recognize enum of circuit type")
            }
        }
    }
}
