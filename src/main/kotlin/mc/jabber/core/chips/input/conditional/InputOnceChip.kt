package mc.jabber.core.chips.input.conditional

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

@AutoConstructInt(ChipID("chip_input_once"), [1, 2, 3, 4, 5])
class InputOnceChip(val value: Int) : ChipProcess() {
    override val isInput = true
    override val id = Global.id("in1")
    override val receiveDirections = DirBitmask.NONE
    override val sendDirections = DirBitmask.ALL

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?
    ): CardinalData {
        val hasSent = chipData[pos].assertType<SimpleIntStore>()

        if (hasSent.value == 0) {
            hasSent.value = 1
            return CardinalData.ofAll(value.toLong())
        } else {
            return CardinalData.empty()
        }
    }

    override fun makeInitialStateEntry(): NbtTransformable<*> {
        return SimpleIntStore(0)
    }
}
