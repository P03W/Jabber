package mc.jabber.core.chips

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap
import mc.jabber.util.assertType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtInt
import net.minecraft.nbt.NbtLong

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
    fun getLong(name: String): Long {
        return longParams.getLong(name)
    }

    fun <E : Enum<E>> registerEnum(name: String, enum: E) {
        enumParams[name] = enum
    }
    inline fun <reified E : Enum<E>> getEnum(name: String): E {
        return enumParams[name].assertType()!!
    }

    fun writeToNbt(): NbtCompound {
        val nbt = NbtCompound()
        longParams.forEach { (name, value) ->
            nbt.putLong(name, value)
        }
        enumParams.forEach { (name, value) ->
            nbt.putInt(name, value.ordinal)
        }
        return nbt
    }

    companion object {
        fun fromNbt(nbt: NbtCompound): ChipParams {
            return ChipParams {
                nbt.keys.forEach { key ->
                    when (val container = nbt.get(key)) {
                        is NbtLong -> longParams.computeIfPresent(key) { _, _ -> container.longValue() }
                        is NbtInt -> enumParams.computeIfPresent(key) { _, exiting -> exiting.javaClass.enumConstants[container.intValue()] }
                    }
                }
            }
        }
    }
}
