package mc.jabber.core.chips.input.worldly

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipParams
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I

@ChipID("chip_dist_to_ground")
class DistToGroundChip(buildParams: ChipParams) : ChipProcess(buildParams) {
    override val id = Global.id("d2g")
    override val isInput = true
    override val receiveDirections = DirBitmask.NONE
    override val sendDirections = DirBitmask.ALL

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        if (context == null) return CardinalData.empty()

        var remainingSteps = 5000L
        val checkingPos = context.blockPos.mutableCopy()
        while (remainingSteps-- > 0) {
            checkingPos.y--
            if (!context.world.getBlockState(checkingPos).isAir) {
                break
            }
        }

        if (remainingSteps == -1L) {
            return CardinalData.ofAll(Long.MAX_VALUE)
        }

        return CardinalData.ofAll(4999 - remainingSteps)
    }
}
