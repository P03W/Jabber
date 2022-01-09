package mc.jabber.core.chips.input.conditional

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipParams
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.chips.storage.SimpleLongStore
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import mc.jabber.util.assertType

@ChipID("chip_input_once")
class InputOnceChip(buildParams: ChipParams) : ChipProcess(buildParams) {
    override val isInput = true
    override val id = Global.id("in1")
    override val receiveDirections = DirBitmask.NONE
    override val sendDirections = DirBitmask.ALL

    override val params = ChipParams(buildParams) {
        registerLong("amount")
    }

    val amount = params.getLong("amount")

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        val hasSent = chipData[pos].assertType<SimpleLongStore>()

        if (hasSent.value == 0L) {
            hasSent.value = 1
            return CardinalData.ofAll(amount)
        } else {
            return CardinalData.empty()
        }
    }

    override fun makeInitialStateEntry(): NbtTransformable<*> {
        return SimpleLongStore(0)
    }
}
