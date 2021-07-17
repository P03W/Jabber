package mc.jabber.minecraft.client.screen.circuit_table

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.Insets
import mc.jabber.Global
import mc.jabber.core.chips.pipes.HorizontalPipeChip
import mc.jabber.core.chips.pipes.corners.Quad1PipeChip
import mc.jabber.core.chips.pipes.corners.Quad2PipeChip
import mc.jabber.core.chips.pipes.corners.Quad3PipeChip
import mc.jabber.core.chips.pipes.corners.Quad4PipeChip
import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.minecraft.client.screen.util.SlotFilters
import mc.jabber.minecraft.items.CircuitItem
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack


class InscribingTableGui(i: Int, inv: PlayerInventory) : SyncedGuiDescription(Global.GUI.INSCRIBING_TABLE_GUI, i, inv) {
    val inventory = SimpleInventory(1)
    val inputItem: ItemStack get() = inventory.getStack(0)

    var isFiringListener = false
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setSize(380, 220)
        root.insets = Insets.ROOT_PANEL

        val itemSlot = WItemSlot.of(inventory, 0).setFilter(SlotFilters.ChipOrCircuit)
        root.add(itemSlot, 4, 3)

        root.add(this.createPlayerInventoryPanel(), 0, 7)

        root.validate(this)

        inventory.addListener {
            if (isFiringListener) return@addListener
            isFiringListener = true
            onContentChanged(it)
            isFiringListener = false
        }
    }

    override fun onContentChanged(inventory: Inventory) {
        if (inputItem.item is CircuitItem) {
            val board = CircuitBoard(5, 5).also {
                it[0, 0] = Global.ITEMS.CHIP_DEBUG_CONSTANT_1.process
                it[0, 1] = Quad1PipeChip()
                it[1, 1] = Quad2PipeChip()
                it[1, 0] = Quad4PipeChip()
                it[2, 0] = HorizontalPipeChip()
                it[3, 0] = Quad3PipeChip()
                it[3, 1] = Global.ITEMS.CHIP_DEBUG_OUTPUT.process
            }
            inputItem.orCreateNbt.putByteArray("c", board.serialize().toByteArray())
        }
        super.onContentChanged(inventory)
        println(inventory)
    }

    override fun close(player: PlayerEntity) {
        super.close(player)
        dropInventory(player, inventory)
    }
}
