package mc.jabber

import mc.jabber.block.CircuitTable
import mc.jabber.block.SimpleComputerBlock
import mc.jabber.block.entity.SimpleComputerBE
import mc.jabber.items.CircuitItem
import mc.jabber.items.chips.CrossChip
import mc.jabber.items.chips.DelayChip
import mc.jabber.items.chips.PipeChip
import mc.jabber.items.chips.abstracts.ChipItem
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

    object ITEMS {
        val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder.build(
            id("items")
        ) { ItemStack(CIRCUIT_ITEM_5x5) }

        // Circuit Boards
        val CIRCUIT_ITEM_5x5 = CircuitItem(5, 5)

        // Chips
        val CHIP_PIPE = ChipItem(PipeChip())
        val CHIP_CROSS = ChipItem(CrossChip())
        val CHIP_DELAY_1 = ChipItem(DelayChip(1))

        fun register() {
            fun Item.register(itemID: String) {
                Registry.register(Registry.ITEM, id(itemID), this)
            }

            CIRCUIT_ITEM_5x5.register("circuit_5x5")
            CHIP_PIPE.register("chip_pipe")
            CHIP_CROSS.register("chip_cross")
            CHIP_DELAY_1.register("chip_delay_1")
        }
    }

    object BLOCKS {
        val CIRCUIT_TABLE = CircuitTable(FabricBlockSettings.of(Material.REPAIR_STATION))
        val SIMPLE_COMPUTER = SimpleComputerBlock(FabricBlockSettings.of(Material.REPAIR_STATION))

        fun register() {
            fun Block.registerAndItem(blockID: String) {
                Registry.register(Registry.BLOCK, id(blockID), this)
                Registry.register(Registry.ITEM, id(blockID), BlockItem(this, FabricItemSettings()))
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
