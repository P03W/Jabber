package mc.jabber.core.chips.storage

import mc.jabber.core.data.serial.NbtTransformable
import net.minecraft.nbt.NbtCompound

data class SimpleLongStore(var value: Long) : NbtTransformable<SimpleLongStore> {
    override fun type(): Byte {
        return 2
    }

    override fun toNbt(): NbtCompound {
        return NbtCompound().apply {
            putLong("v", value)
        }
    }

    override fun fromNbt(nbt: NbtCompound): SimpleLongStore {
        return SimpleLongStore(nbt.getLong("v"))
    }
}
