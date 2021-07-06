package mc.jabber.core.data.serial

import net.minecraft.nbt.NbtCompound

/**
 * Represents an arbitrary object that can be transformed to and from NBT
 *
 * Deserialization is handled by [rebuildArbitraryData]
 */
interface NbtTransformable {
    fun toNbt(): NbtCompound
    fun fromNbt(nbt: NbtCompound)
    fun type(): Byte { return 0 }
}
