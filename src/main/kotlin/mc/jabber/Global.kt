package mc.jabber

import mc.jabber.core.chips.action.AddChip
import mc.jabber.core.chips.duplicate.Duplicate4WayChip
import mc.jabber.core.chips.pipes.CrossPipeChip
import mc.jabber.core.chips.pipes.HorizontalPipeChip
import mc.jabber.core.chips.pipes.VerticalPipeChip
import mc.jabber.core.chips.pipes.corners.Quad1PipeChip
import mc.jabber.core.chips.pipes.corners.Quad2PipeChip
import mc.jabber.core.chips.pipes.corners.Quad3PipeChip
import mc.jabber.core.chips.pipes.corners.Quad4PipeChip
import mc.jabber.core.chips.special.DelayChip
import mc.jabber.minecraft.block.CircuitTable
import mc.jabber.minecraft.block.SimpleComputerBlock
import mc.jabber.minecraft.block.entity.SimpleComputerBE
import mc.jabber.minecraft.items.ChipItem
import mc.jabber.minecraft.items.CircuitItem
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// All constants
object Global {
    val LOG: Logger = LoggerFactory.getLogger("Jabber")
    const val MOD_ID = "jabber"

    //  Makes an ID with the MOD_ID
    fun id(path: String): Identifier {
        return Identifier(MOD_ID, path)
    }

    @Suppress("unused")
    object ITEMS {
        val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder.build(
            id("items")
        ) { ItemStack(CIRCUIT_ITEM_5x5) }

        // Circuit Boards
        val CIRCUIT_ITEM_5x5 = CircuitItem(5, 5)

        // Chips
        val CHIP_DUPLICATE_4_WAY = ChipItem(Duplicate4WayChip())
        val CHIP_ADD_1 = ChipItem(AddChip(1))

        // Pipes
        val CHIP_CROSS_PIPE = ChipItem(CrossPipeChip())
        val CHIP_HORIZONTAL_PIPE = ChipItem(HorizontalPipeChip())
        val CHIP_VERTICAL_PIPE = ChipItem(VerticalPipeChip())
        val CHIP_QUADRANT_1_PIPE = ChipItem(Quad1PipeChip())
        val CHIP_QUADRANT_2_PIPE = ChipItem(Quad2PipeChip())
        val CHIP_QUADRANT_3_PIPE = ChipItem(Quad3PipeChip())
        val CHIP_QUADRANT_4_PIPE = ChipItem(Quad4PipeChip())

        // Delay
        val CHIP_DELAY_1 = ChipItem(DelayChip(1))
        val CHIP_DELAY_2 = ChipItem(DelayChip(2))
        val CHIP_DELAY_3 = ChipItem(DelayChip(3))
        val CHIP_DELAY_4 = ChipItem(DelayChip(4))
        val CHIP_DELAY_5 = ChipItem(DelayChip(5))
        val CHIP_DELAY_10 = ChipItem(DelayChip(10))
        val CHIP_DELAY_20 = ChipItem(DelayChip(20))

        fun register() {
            fun Item.register(itemID: String) {
                Registry.register(Registry.ITEM, id(itemID), this)
            }

            CIRCUIT_ITEM_5x5.register("circuit_5x5")
        }
    }

    object BLOCKS {
        val CIRCUIT_TABLE = CircuitTable(FabricBlockSettings.of(Material.REPAIR_STATION))
        val SIMPLE_COMPUTER = SimpleComputerBlock(FabricBlockSettings.of(Material.REPAIR_STATION))

        fun register() {
            fun Block.registerAndItem(blockID: String) {
                Registry.register(Registry.BLOCK, id(blockID), this)
                Registry.register(
                    Registry.ITEM,
                    id(blockID),
                    BlockItem(this, FabricItemSettings().group(ITEMS.ITEM_GROUP))
                )
            }

            CIRCUIT_TABLE.registerAndItem("circuit_table")
            SIMPLE_COMPUTER.registerAndItem("simple_computer")
        }

        object ENTITIES {
            lateinit var SIMPLE_COMPUTER: BlockEntityType<SimpleComputerBE>

            fun register() {
                SIMPLE_COMPUTER = Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    id("simple_computer"),
                    FabricBlockEntityTypeBuilder
                        .create(::SimpleComputerBE, BLOCKS.SIMPLE_COMPUTER)
                        .build()
                )
            }
        }
    }
}
