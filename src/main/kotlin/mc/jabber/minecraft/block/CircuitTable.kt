package mc.jabber.minecraft.block

import mc.jabber.minecraft.items.CircuitItem
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CircuitTable(settings: Settings) : Block(settings) {
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
            return ActionResult.SUCCESS
        }
        return ActionResult.PASS
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return COLLISION
    }

    override fun getOutlineShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape {
        return COLLISION
    }

    override fun getRaycastShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape = COLLISION
    override fun getCullingShape(state: BlockState?, world: BlockView?, pos: BlockPos?): VoxelShape = COLLISION

    companion object {
        val COLLISION: VoxelShape = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.81, 1.0)
    }
}
