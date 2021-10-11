package mc.jabber.core.chips.storage

import mc.jabber.core.data.serial.NbtTransformable
import net.minecraft.nbt.NbtCompound

data class SimpleIntStore(var value: Int) : NbtTransformable<SimpleIntStore> {
    override fun type(): Byte {
        return 2
    }

    override fun toNbt(): NbtCompound {
        return NbtCompound().apply {
            putInt("v", value)
        }
    }

    override fun fromNbt(nbt: NbtCompound): SimpleIntStore {
        return SimpleIntStore(nbt.getInt("v"))
    }
}
