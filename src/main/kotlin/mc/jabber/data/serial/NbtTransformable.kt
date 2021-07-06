package mc.jabber.data.serial

import net.minecraft.nbt.NbtCompound

interface NbtTransformable<out T> {
    fun toNbt(): NbtCompound
    fun fromNbt(nbt: NbtCompound): T
    fun type(): Byte { return 0 }
}
