package mc.jabber.minecraft.block.entity

import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.circuit.CircuitManager
import mc.jabber.core.data.ExecutionContext
import mc.jabber.minecraft.items.CircuitItem
import mc.jabber.util.assertType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
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
        val bytes = new.orCreateNbt.getByteArray("c")
        circuit = if (bytes.isNotEmpty()) {
            CircuitManager(
                context,
                item.sizeX,
                item.sizeY,
                CircuitBoard(1, 1)
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
