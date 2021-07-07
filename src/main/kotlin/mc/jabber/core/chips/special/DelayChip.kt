package mc.jabber.core.chips.special

import com.google.common.io.ByteStreams
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.data.serial.rebuildArbitraryData
import mc.jabber.core.data.util.TriSet
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.math.Cardinal
import mc.jabber.core.math.Vec2I
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
        chipData: HashMap<Vec2I, NbtTransformable>
    ): CardinalData<T> {
        // Grab the queue
        val queue = chipData[pos].assertType<DelayState>()

        // Add the received values to the queue
        data.forEach { cardinal, t ->
            if (t != null) {
                queue.data.add(TriSet(delay, cardinal, t))
            }
        }

        // Build the output
        var out = data.ofAll(null)
        queue.data.forEach {
            it.first--
            if (it.first <= 0) {
                out = out.with(it.second, it.third)
            }
        }

        queue.data = queue.data.filter { it.first > 0 }.toMutableList()

        return out
    }

    class DelayState : NbtTransformable {
        var data = mutableListOf<TriSet<Short, Cardinal, out NbtTransformable>>()

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
