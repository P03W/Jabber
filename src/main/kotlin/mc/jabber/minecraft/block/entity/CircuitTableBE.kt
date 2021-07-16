package mc.jabber.minecraft.block.entity

import mc.jabber.Global
import mc.jabber.minecraft.client.screen.circuit_table.CircuitTableGuiDescription
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.BlockPos


class CircuitTableBE(pos: BlockPos?, state: BlockState?) :
    BlockEntity(Global.BLOCKS.ENTITIES.CIRCUIT_TABLE, pos, state), NamedScreenHandlerFactory {
    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
        return CircuitTableGuiDescription(syncId, inv)
    }

    override fun getDisplayName(): Text {
        return TranslatableText("block.jabber.circuit_table")
    }
}
