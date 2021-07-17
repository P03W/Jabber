@file:Suppress("NOTHING_TO_INLINE", "UnstableApiUsage")

package mc.jabber.util

import com.google.common.io.ByteStreams
import com.google.protobuf.ByteString
import mc.jabber.Global
import mc.jabber.core.data.serial.NbtTransformable
import net.minecraft.nbt.NbtIo
import org.slf4j.Logger
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Allows for calling info with any object by converting it to a string
 */
inline fun Logger.info(obj: Any?) {
    info(obj.toString())
}

/**
 * Allows for logging anything by automatically converting to string
 */
inline fun Any?.log() {
    Global.LOG.info(this)
}

/**
 * A short and simple type assertion with no UncheckedCast warnings
 *
 * Has a nice error message as well that states the received and expected types
 *
 * @param T The expected type, may be inferred by compiler
 * @return The value this was called on
 * @throws AssertionError If the value was not of the expected type
 */
@OptIn(ExperimentalContracts::class)
inline fun <reified T> Any?.assertType(): T {
    contract {
        returns() implies (this@assertType is T)
    }

    assert(this is T) {
        if (this != null) {
            "Got a value of ${this::class.qualifiedName}, but should have gotten an object of type ${T::class.simpleName}"
        } else {
            "Got a null, but should have gotten an object of type ${T::class.simpleName}"
        }
    }

    return this as T
}

fun <T> NbtTransformable<T>.asIdableByteArray(): ByteArray {
    val bytes = ByteStreams.newDataOutput()
    bytes.writeByte(type().toInt())
    NbtIo.write(toNbt(), bytes)
    return bytes.toByteArray()
}

fun ByteArray.toByteString() = ByteString.copyFrom(this)
