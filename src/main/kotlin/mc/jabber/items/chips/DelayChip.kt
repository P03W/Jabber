package mc.jabber.items.chips

import com.google.common.io.ByteStreams
import mc.jabber.data.CardinalData
import mc.jabber.data.serial.NbtTransformable
import mc.jabber.data.serial.rebuildArbitraryData
import mc.jabber.data.util.TriSet
import mc.jabber.items.chips.abstracts.ChipItem
import mc.jabber.items.chips.abstracts.ChipProcess
import mc.jabber.math.Cardinal
import mc.jabber.math.Vec2I
import mc.jabber.util.assertType
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.nbt.NbtByteArray
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtList
import java.nio.ByteBuffer

class DelayChip(val delay: Short) : ChipProcess() {
    override fun makeInitialStateEntry(): NbtTransformable {
        return DelayState()
    }

    override fun <T : NbtTransformable> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        state: MutableMap<Vec2I, Any>
    ): CardinalData<T> {
        // Grab the queue
        val queue = state[pos].assertType<DelayState>()

        // Add the received values to the queue
        data.forEach { cardinal, t ->
            if (t != null) {
                queue.data.add(TriSet(delay, cardinal, t))
            }
        }

        // Build the output
        val out = data.ofAll(null)
        queue.data.forEach {
            it.first--
            if (it.first <= 0) {
                out.with(it.second, it.third)
            }
        }

        state[pos] = queue.data.filter { it.first > 0 }

        return out
    }

    class DelayState : NbtTransformable {
        val data = mutableListOf<TriSet<Short, Cardinal, out NbtTransformable>>()

        /**
         * Format is a NBT_COMPOUND with a single entry "d" (for data)
         * "d" is a list of byte arrays of the format
         * 2 bytes -> Remaining delay
         * 1 byte  -> Direction to output
         * 1 byte  -> Format of remaining data
         * X bytes -> All remaining data is whatever the format is
         */
        @Suppress("UnstableApiUsage")
        override fun toNbt(): NbtCompound {
            val out = NbtCompound()
            out.put("d", NbtList().also {
                data.forEach { set ->
                    val additionalBytes = ByteStreams.newDataOutput()
                    NbtIo.write(set.third.toNbt(), additionalBytes)

                    val additionalFixed = additionalBytes.toByteArray()

                    val compact = ByteBuffer.allocate(4 + additionalFixed.size)
                    compact.putShort(set.first)
                    compact.put(set.second.ordinal.toByte())
                    compact.put(set.third.type())
                    compact.put(additionalFixed)

                    it.add(NbtByteArray(compact.array()))
                }
            })
            return out
        }

        override fun fromNbt(nbt: NbtCompound) {
            data.clear()
            val list = nbt.getList("d", NbtType.BYTE_ARRAY)

            list.forEach {
                it.assertType<NbtByteArray>()
                val buffer = ByteBuffer.allocate(it.size).also { buffer -> buffer.mark() }
                it.forEach { byte -> buffer.put(byte.byteValue()) }

                buffer.reset()

                val remainingDelay = buffer.short
                val cardinal = Cardinal.values()[buffer.get().toInt()]

                val array = buffer.array()
                val arbitrary = array.drop(array.size - buffer.remaining())

                data.add(TriSet(remainingDelay, cardinal, rebuildArbitraryData(arbitrary)))
            }
        }
    }
}
