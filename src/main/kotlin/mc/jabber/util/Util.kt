package mc.jabber.util

import java.util.*

/**
 * @return the string but with the first character is title case
 */
fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
