package mc.jabber.core.chips.action

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipParams
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

/**
 * Multiplies the input and sends it across
 */
@ChipID("chip_mult", "Multiply")
class MultChip(buildParams: ChipParams) : ChipProcess(buildParams) {
    override val id = Global.id("mult")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.ALL

    override val params = ChipParams(buildParams) {
        registerLong("amount")
    }

    val amount = params.getLong("amount")

    override val lore: Array<String> = arrayOf("Multiplies inputs by $amount ")

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        val up = data.up?.let { it * amount }
        val down = data.down?.let { it * amount }
        val left = data.left?.let { it * amount }
        val right = data.right?.let { it * amount }
        return CardinalData(up, down, left, right)
    }
}
