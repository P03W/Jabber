package mc.jabber.core.chips.memory

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipParams
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

@ChipID("chip_mem_get")
class MemoryGetChip(buildParams: ChipParams) : ChipProcess(buildParams) {
    override val id = Global.id("memget")
    override val isInput = true
    override val receiveDirections = DirBitmask.NONE
    override val sendDirections = DirBitmask.ALL

    override val params = ChipParams(buildParams) {
        registerLong("index")
    }
    val index = params.getLong("index")

    override val lore: Array<String> = arrayOf("Outputs the value at memory index %index%")
    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        return CardinalData.ofAll(memory[index.toInt()])
    }
}
