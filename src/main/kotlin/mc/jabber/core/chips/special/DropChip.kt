package mc.jabber.core.chips.special

import mc.jabber.Global
import mc.jabber.core.auto.AutoConstructInt
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.chips.storage.SimpleIntStore
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import mc.jabber.util.assertType

/**
 * Drops the first X inputs, then acts as a cross
 */
@AutoConstructInt(ChipID("chip_drop"), [1, 2, 3, 4, 5])
class DropChip(val count: Int) : ChipProcess() {
    override val id = Global.id("drop")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.ALL

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        val intStore = chipData[pos].assertType<SimpleIntStore>()

        if (intStore.value > 0) {
            intStore.value--
            return CardinalData.empty()
        }

        return CardinalData(data.down, data.up, data.right, data.left)
    }

    override fun makeInitialStateEntry(): NbtTransformable<*> {
        return SimpleIntStore(count)
    }
}
