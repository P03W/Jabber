package mc.jabber.minecraft.block.entity

import mc.jabber.core.circuit.CircuitManager
import mc.jabber.core.data.CircuitType
import mc.jabber.minecraft.items.CircuitItem
import mc.jabber.util.assertType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class SimpleComputerBE(
    val stepsPerTick: Int,
    pos: BlockPos,
    state: BlockState,
    blockEntity: BlockEntityType<SimpleComputerBE>
) :
    BlockEntity(blockEntity, pos, state) {
    var circuitItem: ItemStack? by observable(null, ::rebuildCircuit)
    private var circuit: CircuitManager? = null

    @Suppress("UNUSED_PARAMETER")
    fun rebuildCircuit(prop: KProperty<*>, old: ItemStack?, new: ItemStack?) {
        if (new == null) {
            circuit = null; return
        }
        val item = new.item.assertType<CircuitItem>()
        circuit = CircuitManager(CircuitType.COMPUTE, item.sizeX, item.sizeY)
        circuit!!.setup()
    }

    companion object {
        @Suppress("UNUSED_PARAMETER")
        fun tick(world: World, blockPos: BlockPos, state: BlockState, be: SimpleComputerBE) {
            repeat(be.stepsPerTick) {
                be.circuit?.simulate()
            }
        }
    }
}