package mc.jabber.core.circuit

import mc.jabber.Global
import mc.jabber.core.asm.CircuitCompiler
import mc.jabber.core.asm.CompiledCircuit
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.CircuitDataStorage
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.data.serial.rebuildArbitraryData
import mc.jabber.core.math.Vec2I
import mc.jabber.util.asIdByteArray
import mc.jabber.util.assertType
import mc.jabber.util.warn
import net.fabricmc.fabric.api.util.NbtType
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtByteArray
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.util.Identifier
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class CircuitManager(
    var context: ExecutionContext?,
    sizeX: Int,
    sizeY: Int,
    _initialBoard: CircuitBoard = CircuitBoard(sizeX, sizeY)
) {
    var board = _initialBoard
        private set

    private lateinit var compiledCircuit: CompiledCircuit

    var didSetup by Delegates.vetoable(false) { _: KProperty<*>, _: Boolean, new: Boolean ->
        return@vetoable new
    }

    fun setup() {
        compiledCircuit = CircuitCompiler.compileCircuit(board)
        compiledCircuit.setup()
        didSetup = true
    }

    fun simulate() {
        if (didSetup) {
            compiledCircuit.simulate(context)
        } else {
            "Tried to simulate a board that has not been setup!".warn()
            Throwable().stackTrace.toList().warn()
        }
    }

    fun writeNbt(nbt: NbtCompound): NbtCompound {
        nbt.putInt("bX", board.sizeX)
        nbt.putInt("bY", board.sizeY)

        nbt.put("e", NbtList().apply {
            board.forEach { vec2I, chipProcess ->
                add(NbtString.of("${vec2I.x}*${vec2I.y}|${chipProcess.id.path}"))
            }
        })

        nbt.put("so", NbtCompound().apply {
            compiledCircuit.getChipStorage().forEach { (vec2I, transform) ->
                put("${vec2I.x}*${vec2I.y}", NbtByteArray(transform.asIdByteArray()))
            }
        })

        nbt.put("st", NbtCompound().apply {
            compiledCircuit.getChipState().forEach { vec2I, cardinalData ->
                put("${vec2I.x}*${vec2I.y}", cardinalData.toNbt())
            }
        })

        return nbt
    }

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun readNbt(nbt: NbtCompound): CircuitManager {
            val sizeX = nbt.getInt("bX")
            val sizeY = nbt.getInt("by")

            val board = CircuitBoard(sizeX, sizeY)

            nbt.getList("e", NbtType.STRING).forEach { entry ->
                val values = entry.assertType<NbtString>().asString().split("|")
                val (x, y) = values[0].split("*").map { int -> int.toInt() }

                board[x, y] = Global.PROCESS_ITEM_MAP[Identifier("jabber:${values[1]}")]?.process
            }

            val storage = buildMap<Vec2I, NbtTransformable<*>> {
                val storeNbt = nbt.get("so").assertType<NbtCompound>()
                storeNbt.keys.forEach { vecString ->
                    val (x, y) = vecString.split("*").map { int -> int.toInt() }
                    val vec = Vec2I(x, y)

                    put(vec, rebuildArbitraryData(storeNbt.getByteArray(vecString).toList()))
                }
            }

            val state = CircuitDataStorage(sizeX, sizeY)
            val stateNbt = nbt.get("so").assertType<NbtCompound>()
            stateNbt.keys.forEach { vecString ->
                val (x, y) = vecString.split("*").map { int -> int.toInt() }
                val vec = Vec2I(x, y)

                state[vec] = CardinalData.readNbt(stateNbt.get(vecString).assertType())
            }

            val manager = CircuitManager(null, sizeX, sizeY, board)

            manager.setup()

            manager.compiledCircuit.stateFrom(storage, state)

            return manager
        }
    }
}
