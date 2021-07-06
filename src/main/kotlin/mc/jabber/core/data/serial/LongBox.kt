package mc.jabber.core.data.serial

import net.minecraft.nbt.NbtCompound

data class LongBox(var long: Long): NbtTransformable {
    override fun toNbt(): NbtCompound {
        return NbtCompound().also { it.putLong("l", long) }
    }

    override fun fromNbt(nbt: NbtCompound) {
        long = nbt.getLong("l")
    }

    override fun type(): Byte {
        return 1
    }

    override fun toString(): String {
        return long.toString()
    }

    operator fun plusAssign(amount: Long) {
        long += amount
    }
}
