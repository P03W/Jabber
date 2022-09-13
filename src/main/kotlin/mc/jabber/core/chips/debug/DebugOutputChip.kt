package mc.jabber.core.chips.debug

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipParams
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.chips.DirBitmask
import mc.jabber.core.data.CardinalData
import mc.jabber.core.data.ExecutionContext
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.minecraft.util.Util

@ChipID("chip_debug")
class DebugOutputChip(buildParams: ChipParams) : ChipProcess(buildParams) {
    override val id = Global.id("debug")
    override val receiveDirections = DirBitmask.ALL
    override val sendDirections = DirBitmask.NONE

    override fun receive(
        data: CardinalData,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>,
        context: ExecutionContext?,
        memory: LongArray
    ): CardinalData {
        MinecraftClient.getInstance().player?.sendSystemMessage(
            Text.of("DEBUG: u=${data.up} d=${data.down} l=${data.left} r=${data.right}")
        )
        return CardinalData.empty
    }
}
