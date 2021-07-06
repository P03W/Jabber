package mc.jabber.util

import java.util.*

/**
 * Implementation of the recommend replacement for kotlin's stdlib capitalize
 * @return The string but with the first character in title case
 */
fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
