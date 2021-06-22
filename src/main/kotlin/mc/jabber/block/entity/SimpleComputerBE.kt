package mc.jabber.block.entity

import mc.jabber.Global
import mc.jabber.circuit.CircuitManager
import mc.jabber.items.CircuitItem
import mc.jabber.util.assertType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.properties.Delegates
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

class SimpleComputerBE(pos: BlockPos, state: BlockState) : BlockEntity(Global.BLOCKS.ENTITIES.SIMPLE_COMPUTER, pos, state) {
    var circuitItem: ItemStack? by observable(null, ::rebuildCircuit)
    private var circuit: CircuitManager? = null

    fun rebuildCircuit(prop: KProperty<*>, old: ItemStack?, new: ItemStack?) {
        if (new == null) {circuit = null; return}
        new.item.assertType<CircuitItem>()
    }

    companion object {
        fun tick(world: World, blockPos: BlockPos, state: BlockState, be: SimpleComputerBE) {

        }
    }
}
