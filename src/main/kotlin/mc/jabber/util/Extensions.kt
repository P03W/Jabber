@file:Suppress("NOTHING_TO_INLINE")

package mc.jabber.util

import mc.jabber.Global
import org.slf4j.Logger

inline fun Any?.log() {
    Global.LOG.info(this ?: "null")
}

inline fun Logger.info(obj: Any) {
    info(obj.toString())
}
