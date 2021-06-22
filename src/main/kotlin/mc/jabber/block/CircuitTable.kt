package mc.jabber.block

import mc.jabber.items.CircuitItem
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
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
        if (!world.isClient) {
            val item = player.getStackInHand(hand)
            if (item.item is CircuitItem) {
                return ActionResult.SUCCESS
            }
            return ActionResult.PASS
        }
        return ActionResult.PASS
    }
}
