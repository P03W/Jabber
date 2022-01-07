package mc.jabber.core.chips.special

import mc.jabber.Global
import mc.jabber.core.auto.AutoConstructInt
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.data.util.TriSet
import mc.jabber.core.math.Cardinal
import mc.jabber.core.math.Vec2I
import mc.jabber.util.assertType
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList

/**
 * A chip that takes in any input, and passes it on again after [delay] steps
 *
 * @param delay How long each input should be held before passing it on
 */
@AutoConstructInt(ChipID("chip_delay"), [1, 2, 3, 4, 5, 10, 20])
class DelayChip(val delay: Int) : ChipProcess() {
    override val id = Global.id("delay$delay")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.ALL

    override fun makeInitialStateEntry(): NbtTransformable<DelayState> {
        return DelayState()
    }

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        // Grab the queue
        val queue = chipData[pos].assertType<DelayState>()

        // Add the received values to the queue
        data.forEach { cardinal, t ->
            if (t != null) {
                queue.data.add(TriSet(delay, cardinal, t))
            }
        }

        // Build the output
        var out = CardinalData.ofAll(null)
        queue.data.forEach {
            it.first--
            if (it.first <= 0) {
                out = out.with(it.second, it.third)
            }
        }

        queue.data = queue.data.filter { it.first > 0 }.toMutableList()

        return out
    }

    /**
     * Allows us to store the held data, mildly hacky but works
     */
    class DelayState : NbtTransformable<DelayState> {
        var data = mutableListOf<TriSet<Int, Cardinal, Long>>()

        override fun type(): Byte {
            return 1
        }

        override fun toNbt(): NbtCompound {
            return NbtCompound().apply {
                put("e", NbtList().apply {
                    data.forEach { entry ->
                        add(NbtCompound().apply {
                            putInt("r", entry.first)
                            putByte("c", entry.second.ordinal.toByte())
                            putLong("d", entry.third)
                        })
                    }
                })
            }
        }

        override fun fromNbt(nbt: NbtCompound): DelayState {
            val newData = mutableListOf<TriSet<Int, Cardinal, Long>>()
            nbt.getList("e", NbtType.COMPOUND).forEach {
                val compound = it as NbtCompound
                newData.add(TriSet(compound.getInt("r"), Cardinal.values()[compound.getByte("c").toInt()], compound.getLong("d")))
            }
            return DelayState().apply { data = newData }
        }
    }
}
