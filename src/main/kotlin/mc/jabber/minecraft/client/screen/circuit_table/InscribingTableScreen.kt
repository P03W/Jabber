package mc.jabber.minecraft.client.screen.circuit_table

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text

class InscribingTableScreen(gui: InscribingTableGui, player: PlayerEntity, title: Text) :
    CottonInventoryScreen<InscribingTableGui>(gui, player, title)
