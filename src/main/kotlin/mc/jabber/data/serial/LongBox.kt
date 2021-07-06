package mc.jabber.data.serial

import net.minecraft.nbt.NbtCompound

class LongBox(private val long: Long): NbtTransformable<LongBox> {
    override fun toNbt(): NbtCompound {
        return NbtCompound().also { it.putLong("l", long) }
    }

    override fun fromNbt(nbt: NbtCompound): LongBox {
        return LongBox(nbt.getLong("l"))
    }

    override fun type(): Byte {
        return 1
    }

    override fun toString(): String {
        return long.toString()
    }
}
