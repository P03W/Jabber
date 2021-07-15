package mc.jabber.minecraft.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos

/**
 * Simple factory wrapper around [SimpleComputerBE] to allow for registration
 */
class SimpleComputerBEFactory(val stepsPerTick: Int, val be: () -> BlockEntityType<SimpleComputerBE>) {
    fun make(pos: BlockPos, state: BlockState): SimpleComputerBE {
        return SimpleComputerBE(stepsPerTick, pos, state, be())
    }
}
