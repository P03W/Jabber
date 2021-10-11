package mc.jabber.minecraft.block.entity

import mc.jabber.Global
import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.circuit.CircuitManager
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.math.Vec2I
import mc.jabber.minecraft.items.CircuitItem
import mc.jabber.util.assertType
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtString
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class SimpleComputerBE(
    val stepsPerTick: Int,
    pos: BlockPos,
    state: BlockState,
    blockEntity: BlockEntityType<SimpleComputerBE>
) : BlockEntity(blockEntity, pos, state) {
    var isRebuildingFromNbt = false
    var circuitItem: ItemStack? by observable(null, ::rebuildCircuit)
    var circuit: CircuitManager? = null
        private set

    @Suppress("UNUSED_PARAMETER")
    fun rebuildCircuit(prop: KProperty<*>, old: ItemStack?, new: ItemStack?) {
        if (isRebuildingFromNbt) return

        markDirty()
        if (new == null) {
            circuit = null; return
        }

        if (world !is ServerWorld) return

        val context = ExecutionContext(world.assertType(), pos, null)

        val item = new.item.assertType<CircuitItem>()
        val entries = new.orCreateNbt.getList("c", NbtType.STRING)
        circuit = if (entries.isNotEmpty()) {
            val board = CircuitBoard(item.sizeX, item.sizeY)

            entries.forEach { entry ->
                val values = entry.assertType<NbtString>().asString().split("|")
                val (x, y) = values[0].split("*").map { int -> int.toInt() }
                board[x, y] = Global.PROCESS_ITEM_MAP[Identifier("jabber:${values[1]}")]?.process ?: return@forEach
            }

            CircuitManager(
                context,
                item.sizeX,
                item.sizeY,
                board
            )
        } else {
            CircuitManager(
                context,
                item.sizeX,
                item.sizeY
            )
        }
        circuit!!.setup()
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        super.writeNbt(nbt)

        circuit?.writeNbt(nbt)

        return nbt
    }

    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)

        if (nbt.contains("bX") && nbt.contains("bY") && nbt.contains("e") && nbt.contains("so") && nbt.contains("st")) {
            circuit = CircuitManager.readNbt(nbt)
        }
    }

    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun tick(world: World, blockPos: BlockPos, state: BlockState, be: SimpleComputerBE) {
            if (world is ServerWorld) {
                repeat(be.stepsPerTick) {
                    be.circuit?.simulate()
                }
            }
        }
    }
}
