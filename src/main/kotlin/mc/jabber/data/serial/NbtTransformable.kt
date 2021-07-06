package mc.jabber.data.serial

import net.minecraft.nbt.NbtCompound

interface NbtTransformable {
    fun toNbt(): NbtCompound
    fun fromNbt(nbt: NbtCompound)
    fun type(): Byte { return 0 }
}
