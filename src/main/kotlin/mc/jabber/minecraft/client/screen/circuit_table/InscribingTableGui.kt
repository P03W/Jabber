package mc.jabber.minecraft.client.screen.circuit_table

import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.data.Insets
import mc.jabber.Global
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
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
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
            openDimensions(CircuitItem(0, 0))
        }

        if (inputItem !is CircuitItem && lastInputItem is CircuitItem) {
            openDimensions(CircuitItem(0, 0))
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

        inv.forEach { i, itemStack ->
            if (i == 0 || itemStack.item !is ChipItem) return@forEach
            board[Vec2I.transformOut(i - 1, 10)] = itemStack.item.assertType<ChipItem>().process
        }

        stack.orCreateNbt.put("c", NbtList().apply {
            board.forEach { vec2I, chipProcess ->
                add(NbtString.of("${vec2I.x}*${vec2I.y}|${chipProcess.id.path}"))
            }}
        )
    }

    private fun deserializeTo(inv: Inventory, stack: ItemStack) {
        val board = stack.orCreateNbt.getList("c", NbtType.STRING)

        board.forEach { entry ->
            val values = entry.assertType<NbtString>().asString().split("|")
            val (x, y) = values[0].split("*").map { int -> int.toInt() }
            val index = Vec2I(x, y).transformInto(10) + 1

            inv.setStack(index, ItemStack(Global.PROCESS_ITEM_MAP[Identifier("jabber:${values[1]}")]))
        }
    }

    override fun close(player: PlayerEntity) {
        super.close(player)
        dropInventory(player, SimpleInventory(1).also { it.setStack(0, editingInv.getStack(0)) })
    }

    private fun openDimensions(circuit: CircuitItem) {
        val peers = chipInputs.peers

        peers.forEach {
            it.isVisible = false
            it.isInsertingAllowed = false
            it.isTakingAllowed = false
        }

        for (x in 0 until circuit.sizeX) {
            for (y in 0 until circuit.sizeY) {
                val index = Vec2I(x, y).transformInto(10)
                val validatedSlot = peers[index]
                validatedSlot.isVisible = true
                validatedSlot.isInsertingAllowed = true
                validatedSlot.isTakingAllowed = true
            }
        }
    }
}
