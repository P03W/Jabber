@file:Suppress("NOTHING_TO_INLINE")

package mc.jabber.util

import mc.jabber.Global
import org.slf4j.Logger
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


inline fun Logger.info(obj: Any) {
    info(obj.toString())
}

inline fun Any?.log() {
    Global.LOG.info(this ?: "null")
}

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
