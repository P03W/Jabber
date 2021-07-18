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
import mc.jabber.core.chips.special.CustomChip
import mc.jabber.core.chips.special.DelayChip
import mc.jabber.core.data.ComputeData
import mc.jabber.core.data.serial.LongBox
import mc.jabber.minecraft.block.InscribingTable
import mc.jabber.minecraft.block.SimpleComputerBlock
import mc.jabber.minecraft.block.entity.InscribingTableBE
import mc.jabber.minecraft.block.entity.SimpleComputerBE
import mc.jabber.minecraft.block.entity.SimpleComputerBEFactory
import mc.jabber.minecraft.client.screen.circuit_table.InscribingTableGui
import mc.jabber.minecraft.client.screen.circuit_table.InscribingTableScreen
import mc.jabber.minecraft.items.ChipItem
import mc.jabber.minecraft.items.CircuitItem
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import net.minecraft.util.registry.Registry
import org.slf4j.Logger
import org.slf4j.LoggerFactory


// All constants
object Global {
    val LOG: Logger = LoggerFactory.getLogger("Jabber")
    const val MOD_ID = "jabber"

    val PROCESS_ITEM_MAP: HashMap<Identifier, ChipItem> = hashMapOf()

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

        // Debug
        val CHIP_DEBUG_CONSTANT_1 = ChipItem(CustomChip(id("constant_1"), true) { _, _, _ ->
            return@CustomChip ComputeData(LongBox(1), LongBox(1), LongBox(1), LongBox(1))
        })
        val CHIP_DEBUG_VOID = ChipItem(CustomChip(id("void")) { data, _, _ ->
            return@CustomChip data.empty()
        })
        val CHIP_DEBUG_OUTPUT = ChipItem(CustomChip(id("debug_output")) { data, _, _ ->
            println(data)
            MinecraftClient.getInstance().player?.sendSystemMessage(LiteralText("DEBUG: output of $data"), Util.NIL_UUID)
            return@CustomChip data.empty()
        })

        fun register() {
            fun Item.register(itemID: String) {
                Registry.register(Registry.ITEM, id(itemID), this)
            }

            CIRCUIT_ITEM_5x5.register("circuit_5x5")
        }
    }

    object BLOCKS {
        val INSCRIBING_TABLE = InscribingTable(FabricBlockSettings.of(Material.REPAIR_STATION).nonOpaque())
        val SIMPLE_COMPUTER = SimpleComputerBlock(1, FabricBlockSettings.of(Material.REPAIR_STATION))
        val QUAD_COMPUTER = SimpleComputerBlock(4, FabricBlockSettings.of(Material.REPAIR_STATION))

        fun register() {
            fun Block.registerAndItem(blockID: String) {
                Registry.register(Registry.BLOCK, id(blockID), this)
                Registry.register(
                    Registry.ITEM,
                    id(blockID),
                    BlockItem(this, FabricItemSettings().group(ITEMS.ITEM_GROUP))
                )
            }

            INSCRIBING_TABLE.registerAndItem("inscribing_table")
            SIMPLE_COMPUTER.registerAndItem("simple_computer")
            QUAD_COMPUTER.registerAndItem("quad_computer")
        }

        object ENTITIES {
            lateinit var INSCRIBING_TABLE: BlockEntityType<InscribingTableBE>
            lateinit var SIMPLE_COMPUTER: BlockEntityType<SimpleComputerBE>
            lateinit var QUAD_COMPUTER: BlockEntityType<SimpleComputerBE>

            fun register() {
                INSCRIBING_TABLE = Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    id("inscribing_table"),
                    FabricBlockEntityTypeBuilder
                        .create(::InscribingTableBE, BLOCKS.INSCRIBING_TABLE)
                        .build()
                )

                SIMPLE_COMPUTER = Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    id("simple_computer"),
                    FabricBlockEntityTypeBuilder
                        .create(SimpleComputerBEFactory(1) { SIMPLE_COMPUTER }::make, BLOCKS.SIMPLE_COMPUTER)
                        .build()
                )

                QUAD_COMPUTER = Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    id("quad_computer"),
                    FabricBlockEntityTypeBuilder
                        .create(SimpleComputerBEFactory(4) { QUAD_COMPUTER }::make, BLOCKS.QUAD_COMPUTER)
                        .build()
                )
            }
        }
    }

    object GUI {
        lateinit var INSCRIBING_TABLE_GUI: ScreenHandlerType<InscribingTableGui>

        fun registerBoth() {
            INSCRIBING_TABLE_GUI =
                ScreenHandlerRegistry.registerSimple(id("inscribing_table")) { i: Int, inv: PlayerInventory ->
                    InscribingTableGui(i, inv)
                }
        }

        fun registerClient() {
            ScreenRegistry.register(INSCRIBING_TABLE_GUI) { gui, inventory, title ->
                InscribingTableScreen(
                    gui,
                    inventory.player,
                    title
                )
            }
        }
    }
}
