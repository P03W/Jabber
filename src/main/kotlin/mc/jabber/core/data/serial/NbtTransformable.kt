package mc.jabber.core.data.serial

import net.minecraft.nbt.NbtCompound

/**
 * Represents an arbitrary object that can be transformed to and from NBT
 *
 * Deserialization is handled by [rebuildArbitraryData]
 */
interface NbtTransformable<out THIS> {
    fun toNbt(): NbtCompound
    fun fromNbt(nbt: NbtCompound): THIS
    fun type(): Byte {
        return 0
    }
}
