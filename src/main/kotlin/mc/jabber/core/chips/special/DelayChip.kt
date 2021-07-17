package mc.jabber.core.chips.special

import com.google.common.io.ByteStreams
import com.google.protobuf.ByteString
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.data.serial.rebuildArbitraryData
import mc.jabber.core.data.util.TriSet
import mc.jabber.core.math.Cardinal
import mc.jabber.core.math.Vec2I
import mc.jabber.proto.DelayChipStateBuffer
import mc.jabber.proto.DelayChipStateProtoKt.entry
import mc.jabber.proto.delayChipStateProto
import mc.jabber.util.assertType
import net.minecraft.nbt.NbtByteArray
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtIo

class DelayChip(val delay: Int) : ChipProcess() {
    override fun makeInitialStateEntry(): NbtTransformable<DelayState> {
        return DelayState()
    }

    override fun <T : NbtTransformable<*>> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
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

    class DelayState : NbtTransformable<DelayState> {
        var data = mutableListOf<TriSet<Int, Cardinal, out NbtTransformable<*>>>()

        @Suppress("UnstableApiUsage")
        override fun toNbt(): NbtCompound {
            val out = NbtCompound()
            val data = NbtByteArray(delayChipStateProto {
                data.forEach {
                    entries += entry {
                        remainingDelay = it.first
                        direction = it.second.ordinal
                        val additionalBytes = ByteStreams.newDataOutput()
                        additionalBytes.writeByte(it.third.type().toInt())
                        NbtIo.write(it.third.toNbt(), additionalBytes)
                        data = ByteString.copyFrom(additionalBytes.toByteArray())
                    }
                }
            }.toByteArray())
            out.put("d", data)
            return out
        }

        override fun fromNbt(nbt: NbtCompound): DelayState {
            val newData = mutableListOf<TriSet<Int, Cardinal, out NbtTransformable<*>>>()

            val proto = DelayChipStateBuffer.DelayChipStateProto.parseFrom(nbt.getByteArray("d"))
            proto.entriesList.forEach {
                newData += TriSet(it.remainingDelay, Cardinal.values()[it.direction], rebuildArbitraryData(it.data))
            }

            return DelayState().also { it.data = newData }
        }
    }
}
