package mc.jabber

import com.google.common.reflect.ClassPath
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.action.AddChip
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
import mc.jabber.util.assertType
import mc.jabber.util.log
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
import org.reflections.Reflections
import org.slf4j.Logger
import org.slf4j.LoggerFactory


// All constants
@Suppress("MemberVisibilityCanBePrivate")
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
        val CIRCUIT_ITEM_2x2 = CircuitItem(2, 2)
        val CIRCUIT_ITEM_4x3 = CircuitItem(4, 3)
        val CIRCUIT_ITEM_5x5 = CircuitItem(5, 5)
        val CIRCUIT_ITEM_8x6 = CircuitItem(8, 6)
        val CIRCUIT_ITEM_10x10 = CircuitItem(10, 10)

        // Chips
        @OptIn(ExperimentalStdlibApi::class)
        @Suppress("UnstableApiUsage")
        val CHIPS = buildMap<String, ChipItem> {
            val chips = Reflections("mc.jabber.core.chips").getSubTypesOf(ChipProcess::class.java)

            val loader = this::class.java.classLoader
            println(chips)
            chips.forEach {
                val clazz = loader.loadClass(it.name)
                clazz.annotations.map { it::class.java }.log()
                val construct = clazz.constructors.firstOrNull { constructor -> constructor.parameterCount == 0 }
                if (construct != null) {
                    if (clazz.isAnnotationPresent(ChipID::class.java)) {
                        // Never runs
                    } else {
                        throw IllegalStateException("Chip ${it.name} has a 0 parameter constructor but is not annotated with @ChipID")
                    }
                } else {
                    "Failure! ${it.name} only has the following constructors: ${clazz.constructors}"
                }
            }
        }

        // Add
        val CHIP_ADD_1 = ChipItem(AddChip(1))

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
            MinecraftClient.getInstance().player?.sendSystemMessage(
                LiteralText("DEBUG: output of $data"),
                Util.NIL_UUID
            )
            return@CustomChip data.empty()
        })

        fun register() {
            fun Item.register(itemID: String) {
                Registry.register(Registry.ITEM, id(itemID), this)
            }

            CHIPS.forEach { (id, item) -> item.register(id) }

            CIRCUIT_ITEM_2x2.register("circuit_2x2")
            CIRCUIT_ITEM_4x3.register("circuit_4x3")
            CIRCUIT_ITEM_5x5.register("circuit_5x5")
            CIRCUIT_ITEM_8x6.register("circuit_8x6")
            CIRCUIT_ITEM_10x10.register("circuit_10x10")
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
