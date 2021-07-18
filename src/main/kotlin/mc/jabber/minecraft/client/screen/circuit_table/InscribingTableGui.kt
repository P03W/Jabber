package mc.jabber.minecraft.client.screen.circuit_table

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.Insets
import mc.jabber.Global
import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.math.Vec2I
import mc.jabber.minecraft.client.screen.util.SlotFilters
import mc.jabber.minecraft.items.ChipItem
import mc.jabber.minecraft.items.CircuitItem
import mc.jabber.proto.CircuitBoardBuffer
import mc.jabber.util.assertType
import mc.jabber.util.forEach
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class InscribingTableGui(i: Int, inv: PlayerInventory) : SyncedGuiDescription(Global.GUI.INSCRIBING_TABLE_GUI, i, inv) {
    val editingInv = SimpleInventory(10 * 10 + 1)
    val lastKnownInv = SimpleInventory(10 * 10 + 1)

    val inputItem: Item get() = editingInv.getStack(0).item
    val lastInputItem: Item get() = lastKnownInv.getStack(0).item

    val root = WGridPanel()
    val chipInputs = WItemSlot(editingInv, 1, 10, 10, false).apply {
        filter = SlotFilters.ChipOnly
    }

    var isFiringListener = false

    init {
        build()

        editingInv.addListener {
            if (isFiringListener) return@addListener
            isFiringListener = true
            onContentChanged(it)
            isFiringListener = false
        }
    }

    fun build() {
        setRootPanel(root)
        root.setSize(380, 220)
        root.insets = Insets.ROOT_PANEL

        val itemSlot = WItemSlot.of(editingInv, 0).apply {
            filter = SlotFilters.CircuitOnly
        }

        root.add(itemSlot, 4, 3)

        root.add(chipInputs, 10, 1)

        root.add(this.createPlayerInventoryPanel(), 0, 7)

        root.validate(this)
    }

    override fun onContentChanged(inventory: Inventory) {
        if (inputItem is CircuitItem) {
            chipInputs.isInsertingAllowed = true
            chipInputs.onShown()

            if (lastInputItem !is CircuitItem) {
                deserializeTo(editingInv, editingInv.getStack(0))
            }
        } else {
            chipInputs.isInsertingAllowed = false
            chipInputs.onHidden()
        }

        if (inputItem !is CircuitItem && lastInputItem is CircuitItem) {
            editingInv.forEach { i, _ -> if (i > 0) editingInv.setStack(i, ItemStack.EMPTY) }
        } else if (inputItem is CircuitItem && lastInputItem is CircuitItem && editingInv.getStack(0).nbt != lastKnownInv.getStack(0).nbt) {
            editingInv.forEach { i, _ -> if (i > 0) editingInv.setStack(i, ItemStack.EMPTY) }
            deserializeTo(editingInv, editingInv.getStack(0))
        }

        if (inputItem is CircuitItem) serializeTo(editingInv, editingInv.getStack(0))

        editingInv.forEach { i, itemStack ->
            lastKnownInv.setStack(i, itemStack.copy())
        }

        super.onContentChanged(inventory)
    }

    fun serializeTo(inv: Inventory, stack: ItemStack) {
        val circuitItem = stack.item.assertType<CircuitItem>()
        val board = CircuitBoard(circuitItem.sizeX, circuitItem.sizeY)

        inv.forEach { i, itemStack ->
            if (i == 0 || itemStack.isEmpty) return@forEach
            board[Vec2I.transformOut(i - 1, 10)] = itemStack.item.assertType<ChipItem>().process
        }

        stack.orCreateNbt.putByteArray("c", board.serialize().toByteArray())
    }

    fun deserializeTo(inv: Inventory, stack: ItemStack) {
        val board =
            CircuitBoard.deserialize(
                CircuitBoardBuffer.CircuitBoardProto.parseFrom(
                    stack.orCreateNbt.getByteArray("c")
                )
            )

        board.forEach { vec2I, process ->
            val index = vec2I.transformInto(10) + 1
            inv.setStack(index, ItemStack(Global.PROCESS_ITEM_MAP[process.id]))
        }
    }

    override fun close(player: PlayerEntity) {
        super.close(player)
        dropInventory(player, SimpleInventory(1).also { it.setStack(0, editingInv.getStack(0)) } )
    }
}
