package mc.jabber.minecraft.block

import mc.jabber.Global
import mc.jabber.minecraft.block.entity.CircuitTableBE
import mc.jabber.minecraft.items.CircuitItem
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CircuitTable(settings: Settings) : BlockWithEntity(settings) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return CircuitTableBE(
            pos,
            state
        )
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
        return ActionResult.SUCCESS
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

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    companion object {
        private val BOTTOM_COLLISION = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.81, 1.0)
        private val TOP_COLLISION = VoxelShapes.cuboid(0.31, 0.81, 0.31, 0.69, 0.94, 0.69)
        val COLLISION: VoxelShape =
            VoxelShapes.combineAndSimplify(BOTTOM_COLLISION, TOP_COLLISION, BooleanBiFunction.OR)
    }
}
