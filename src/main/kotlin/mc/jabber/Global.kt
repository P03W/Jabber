package mc.jabber

import mc.jabber.block.CircuitTable
import mc.jabber.block.SimpleComputerBlock
import mc.jabber.block.entity.SimpleComputerBE
import mc.jabber.items.CircuitItem
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object Global {
    val LOG: Logger = LoggerFactory.getLogger("Jabber")
    const val MOD_ID = "jabber"

    object ITEMS {
        val CIRCUIT_ITEM_5x5 = CircuitItem(5, 5)

        fun register() {
            fun Item.register(id: String) {
                Registry.register(Registry.ITEM, Identifier(MOD_ID, id), this)
            }

            CIRCUIT_ITEM_5x5.register("circuit_5x5")
        }
    }

    object BLOCKS {
        val CIRCUIT_TABLE = CircuitTable(FabricBlockSettings.of(Material.REPAIR_STATION))
        val SIMPLE_COMPUTER = SimpleComputerBlock(FabricBlockSettings.of(Material.REPAIR_STATION))

        fun register() {
            fun Block.registerAndItem(id: String) {
                Registry.register(Registry.BLOCK, Identifier(MOD_ID, id), this)
                Registry.register(Registry.ITEM, Identifier(MOD_ID, id), BlockItem(this, FabricItemSettings()))
            }

            CIRCUIT_TABLE.registerAndItem("circuit_table")
            SIMPLE_COMPUTER.registerAndItem("simple_computer")
        }

        object ENTITIES {
            lateinit var SIMPLE_COMPUTER: BlockEntityType<SimpleComputerBE>

            fun register() {
                SIMPLE_COMPUTER = Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    Identifier(MOD_ID, "simple_computer"),
                    FabricBlockEntityTypeBuilder
                        .create(::SimpleComputerBE, BLOCKS.SIMPLE_COMPUTER)
                        .build()
                )
            }
        }
    }
}
