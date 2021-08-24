package mc.jabber.core.chips.special

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

/**
 * A chip that always outputs nothing, and will destroy any input
 *
 * Not really useful for the current circuit design but hey, maybe at some point, round-robin circuit that destroys every other input?
 */
@ChipID("chip_trash")
class TrashChip : ChipProcess() {
    override val id = Global.id("trash")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.NONE

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData {
        return data.empty()
    }
}
