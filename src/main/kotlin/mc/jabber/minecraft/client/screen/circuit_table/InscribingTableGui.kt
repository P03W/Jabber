package mc.jabber.minecraft.client.screen.circuit_table

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.Insets
import mc.jabber.Global
import mc.jabber.core.chips.ChipParams
import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.math.Vec2I
import mc.jabber.minecraft.client.screen.CustomBackgroundPainters
import mc.jabber.minecraft.client.screen.SimpleSingleItemInv
import mc.jabber.minecraft.client.screen.util.SlotFilters
import mc.jabber.minecraft.items.ChipItem
import mc.jabber.minecraft.items.CircuitItem
import mc.jabber.util.assertType
import mc.jabber.util.forEach
import mc.jabber.util.peers
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.util.Identifier

class InscribingTableGui(i: Int, inv: PlayerInventory) : SyncedGuiDescription(Global.GUI.INSCRIBING_TABLE_GUI, i, inv) {
    private val editingInv = SimpleSingleItemInv(10 * 10 + 1)
    private val lastKnownInv = SimpleSingleItemInv(10 * 10 + 1)

    private val inputItem: Item get() = editingInv.getStack(0).item
    private val lastInputItem: Item get() = lastKnownInv.getStack(0).item

    private val root = WGridPanel()
    private val chipInputs = WItemSlot(editingInv, 1, 10, 10, false).apply {
        filter = SlotFilters.ChipOnly
    }

    var isFiringListener = false

    init {
        build()

        editingInv.addListener {
            chipInputs.backgroundPainter = CustomBackgroundPainters.SLOT
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

        chipInputs.isModifiable = false
        root.add(chipInputs, 10, 1)

        root.add(this.createPlayerInventoryPanel(), 0, 7)

        root.validate(this)

        chipInputs.backgroundPainter = CustomBackgroundPainters.SLOT
    }

    override fun onContentChanged(inventory: Inventory) {
        chipInputs.backgroundPainter = CustomBackgroundPainters.SLOT
        if (inputItem is CircuitItem) {
            if (lastInputItem !is CircuitItem) {
                openDimensions(editingInv.getStack(0).item.assertType())
                if (editingInv.getStack(0).orCreateNbt.contains("c", NbtType.LIST)) {
                    deserializeTo(editingInv, editingInv.getStack(0))
                }
            }
        } else {
            openDimensions(null)
        }

        if (inputItem !is CircuitItem && lastInputItem is CircuitItem) {
            openDimensions(null)
            editingInv.forEach { i, _ -> if (i > 0) editingInv.setStack(i, ItemStack.EMPTY) }
        } else if (inputItem is CircuitItem && lastInputItem is CircuitItem && editingInv.getStack(0).nbt != lastKnownInv.getStack(
                0
            ).nbt
        ) {
            editingInv.forEach { i, _ -> if (i > 0) editingInv.setStack(i, ItemStack.EMPTY) }
            openDimensions(editingInv.getStack(0).item.assertType())
            if (editingInv.getStack(0).orCreateNbt.contains("c", NbtType.BYTE_ARRAY)) {
                deserializeTo(editingInv, editingInv.getStack(0))
            }
        }

        if (inputItem is CircuitItem) serializeTo(editingInv, editingInv.getStack(0))

        editingInv.forEach { i, itemStack ->
            lastKnownInv.setStack(i, itemStack.copy())
        }

        super.onContentChanged(inventory)
    }

    private fun serializeTo(inv: Inventory, stack: ItemStack) {
        val circuitItem = stack.item.assertType<CircuitItem>()
        val board = CircuitBoard(circuitItem.sizeX, circuitItem.sizeY)
        val list = NbtList()

        inv.forEach { i, itemStack ->
            if (i == 0 || itemStack.item !is ChipItem) return@forEach
            val vec2I = Vec2I.transformOut(i - 1, 10)
            val chipProcess = itemStack.item.assertType<ChipItem>().process
            board[vec2I] = chipProcess
            list.add(NbtCompound().apply {
                putInt("loc", vec2I.transformInto(10))
                putString("id", chipProcess.id.path)
                put("data", itemStack.getOrCreateSubNbt("params"))
            })
        }

        stack.orCreateNbt.put("c", list)
    }

    private fun deserializeTo(inv: Inventory, stack: ItemStack) {
        val board = stack.orCreateNbt.getList("c", NbtType.COMPOUND)

        board.forEach { entry ->
            entry.assertType<NbtCompound>()
            val index = entry.getInt("loc") + 1
            val id = entry.getString("id")
            val params = ChipParams.fromNbt(entry.getCompound("data"))

            inv.setStack(
                index,
                ItemStack(
                    Global.PROCESS_ITEM_MAP[Identifier("jabber:$id")]
                ).also {
                    it.setSubNbt("params", params.writeToNbt())
                }
            )
        }
    }

    override fun close(player: PlayerEntity) {
        super.close(player)
        dropInventory(player, SimpleInventory(1).also { it.setStack(0, editingInv.getStack(0)) })
    }

    private fun openDimensions(circuit: CircuitItem?) {

        val (sizeX, sizeY) = if (circuit == null) {
            0 to 0
        } else {
            circuit.sizeX to circuit.sizeY
        }

        val peers = chipInputs.peers

        peers.forEach {
            it.isVisible = false
            it.isInsertingAllowed = false
            it.isTakingAllowed = false
        }

        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                val index = Vec2I(x, y).transformInto(10)
                val validatedSlot = peers[index]
                validatedSlot.isVisible = true
                validatedSlot.isInsertingAllowed = true
                validatedSlot.isTakingAllowed = true
            }
        }
    }
}
