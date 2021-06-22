package mc.jabber.block

import mc.jabber.block.entity.SimpleComputerBE
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class SimpleComputerBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return SimpleComputerBE(pos, state)
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T> {
        return BlockEntityTicker<T> { world1: World, blockPos: BlockPos, blockState: BlockState, t: T ->
            if (t is SimpleComputerBE) {
                SimpleComputerBE.tick(world1, blockPos, blockState, t)
            } else {
                throw IllegalArgumentException("Got $type for the tick function!?")
            }
        }
    }
}
