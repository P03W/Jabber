package mc.jabber.core.chips

import it.unimi.dsi.fastutil.objects.Object2LongArrayMap
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtLong

/**
 * A collection of maps that can be used by chips for dynamic information
 */
class ChipParams(orig: ChipParams? = null, builder: (ChipParams.()->Unit)? = null) {
    val longParams = Object2LongArrayMap<String>()
    val size: Int get() = longParams.size

    init {
        if (builder != null) {
            builder(this)
        }

        orig?.longParams?.overwriteExisting(longParams)
    }

    fun registerLong(name: String, value: Long = 0) {
        longParams[name] = value
    }
    fun getLong(name: String): Long {
        return longParams.getLong(name)
    }

    fun writeToNbt(): NbtCompound {
        val nbt = NbtCompound()
        longParams.forEach { (name, value) ->
            nbt.putLong(name, value)
        }
        return nbt
    }

    override fun toString(): String {
        return "ChipParams(longParams=$longParams)"
    }

    companion object {
        fun fromNbt(nbt: NbtCompound): ChipParams {
            return ChipParams {
                nbt.keys.forEach { key ->
                    when (val container = nbt.get(key)) {
                        is NbtLong -> longParams[key] = container.longValue()
                        else -> throw IllegalStateException("Dont know how to handle $container")
                    }
                }
            }
        }
    }

    private inline fun <reified V> MutableMap<String, V>.overwriteExisting(other: MutableMap<String, V>) {
        this.forEach { (name, value) ->
            other.computeIfPresent(name) {_, _ -> value}
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChipParams

        if (longParams != other.longParams) return false

        return true
    }

    override fun hashCode(): Int {
        return longParams.hashCode()
    }
}
