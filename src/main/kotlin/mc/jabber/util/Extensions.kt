@file:Suppress("NOTHING_TO_INLINE", "UnstableApiUsage")

package mc.jabber.util

import com.google.common.io.ByteStreams
import com.google.protobuf.ByteString
import mc.jabber.Global
import mc.jabber.core.data.serial.NbtTransformable
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtIo
import net.minecraft.util.registry.Registry
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import org.slf4j.Logger
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KClass

/**
 * Implementation of the recommend replacement for kotlin's stdlib capitalize
 * @return The string but with the first character in title case
 */
fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

/**
 * Allows for a simple transform from [other] to [this] through ID
 */
fun <A, B> Registry<A>.idFlip(other: Registry<B>, instance: B): A? {
    return this.get(other.getId(instance))
}

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
 * Allows for calling warn with any object by converting it to a string
 */
inline fun Logger.warn(obj: Any?) {
    warn(obj.toString())
}

/**
 * Allows for warning anything by automatically converting to string
 */
inline fun Any?.warn() {
    Global.LOG.warn(this)
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

fun <T> NbtTransformable<T>.asIdByteArray(): ByteArray {
    val bytes = ByteStreams.newDataOutput()
    bytes.writeByte(type().toInt())
    NbtIo.write(toNbt(), bytes)
    return bytes.toByteArray()
}

fun ByteArray.toByteString(): ByteString = ByteString.copyFrom(this)

fun ClassNode.byteArray(): ByteArray {
    val classWriter = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)
    accept(classWriter)
    val out = classWriter.toByteArray()
    classWriter.visitEnd()
    return out
}

inline fun Inventory.forEach(action: (Int, ItemStack) -> Unit) {
    for (i in 0 until size()) {
        action(i, this.getStack(i))
    }
}


fun Class<*>.hasAnnotation(annotation: KClass<out Annotation>): Boolean = isAnnotationPresent(annotation.java)
