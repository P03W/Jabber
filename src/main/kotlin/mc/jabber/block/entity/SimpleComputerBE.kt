package mc.jabber.block.entity

import mc.jabber.Global
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class SimpleComputerBE(pos: BlockPos, state: BlockState) : BlockEntity(Global.BLOCKS.ENTITIES.SIMPLE_COMPUTER, pos, state) {
    companion object {
        fun tick(world: World, blockPos: BlockPos, state: BlockState, be: SimpleComputerBE) {

        }
    }
}
