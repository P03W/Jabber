package mc.jabber.minecraft.block

import mc.jabber.minecraft.block.entity.SimpleComputerBE
import mc.jabber.minecraft.items.CircuitItem
import mc.jabber.util.assertType
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class SimpleComputerBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return SimpleComputerBE(pos, state)
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        val item = player.getStackInHand(hand)
        if (!world.isClient && item.item is CircuitItem) {
            if (!player.isCreative) item.decrement(1)
            val be = world.getBlockEntity(pos).assertType<SimpleComputerBE>()

            be.circuitItem = item

            return ActionResult.SUCCESS
        }
        return ActionResult.PASS
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
