package mc.jabber.minecraft.block.entity

import mc.jabber.core.circuit.CircuitBoard
import mc.jabber.core.circuit.CircuitManager
import mc.jabber.minecraft.items.CircuitItem
import mc.jabber.proto.CircuitBoardBuffer
import mc.jabber.proto.CircuitManagerBuffer
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
    private var circuit: CircuitManager? = null

    @Suppress("UNUSED_PARAMETER")
    fun rebuildCircuit(prop: KProperty<*>, old: ItemStack?, new: ItemStack?) {
        if (isRebuildingFromNbt) return

        markDirty()
        if (new == null) {
            circuit = null; return
        }
        val item = new.item.assertType<CircuitItem>()
        circuit = CircuitManager(
            item.sizeX,
            item.sizeY,
            CircuitBoard.deserialize(CircuitBoardBuffer.CircuitBoardProto.parseFrom(new.orCreateNbt.getByteArray("c")))
        )
        circuit!!.setup()
    }

    override fun readNbt(nbt: NbtCompound) {
        isRebuildingFromNbt = true
        if (nbt.contains("c")) {
            circuitItem = ItemStack.fromNbt(nbt.getCompound("c"))
        }
        if (nbt.contains("d")) {
            val rebuild = CircuitManagerBuffer.CircuitManagerProto.parseFrom(nbt.getByteArray("d"))
            circuit = CircuitManager.deserialize(rebuild)
        }
        isRebuildingFromNbt = false
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        super.writeNbt(nbt)
        if (circuitItem != null && circuit != null) {
            nbt.put("c", circuitItem!!.writeNbt(NbtCompound()))
            nbt.putByteArray("d", circuit!!.serialize().toByteArray())
        }
        return nbt
    }

    override fun toInitialChunkDataNbt(): NbtCompound {
        return writeNbt(NbtCompound())
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
