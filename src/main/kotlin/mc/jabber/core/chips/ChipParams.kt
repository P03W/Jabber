package mc.jabber.core.chips

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap
import mc.jabber.util.assertType

class ChipParams(orig: ChipParams? = null, builder: ChipParams.()->Unit) {
    val longParams = Object2LongArrayMap<String>()
    val enumParams = mutableMapOf<String, Enum<*>>()

    init {
        builder()

        if (orig != null) {
            orig.longParams.forEach { (name, value) ->
                longParams.computeIfPresent(name) { _, _ -> value}
            }

            orig.enumParams.forEach { (name, value) ->
                enumParams.computeIfPresent(name) {_, _ -> value}
            }
        }
    }

    fun registerLong(name: String) {
        longParams[name] = 0
    }

    fun <E : Enum<E>> registerEnum(name: String, enum: E) {
        enumParams[name] = enum
    }

    fun getLong(name: String): Long {
        return longParams.getLong(name)
    }

    inline fun <reified E : Enum<E>> getEnum(name: String): E {
        return enumParams[name].assertType()!!
    }
}
