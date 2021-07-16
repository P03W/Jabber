package mc.jabber.minecraft.client.screen.circuit_table

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.Insets
import mc.jabber.Global
import mc.jabber.minecraft.client.screen.util.SlotFilters
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory


class CircuitTableGuiDescription(i: Int, inv: PlayerInventory) : SyncedGuiDescription(Global.GUI.CIRCUIT_TABLE_GUI, i, inv) {
    val inventory = SimpleInventory(1)
    val currentItem get() = inventory.getStack(0)

    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setSize(380, 220)
        root.insets = Insets.ROOT_PANEL

        val itemSlot = WItemSlot.of(inventory, 0).setFilter(SlotFilters.ChipOnly)
        root.add(itemSlot, 4, 3)

        root.add(this.createPlayerInventoryPanel(), 0, 7)

        root.validate(this)
    }

    override fun close(player: PlayerEntity) {
        super.close(player)
        dropInventory(player, inventory)
    }
}
