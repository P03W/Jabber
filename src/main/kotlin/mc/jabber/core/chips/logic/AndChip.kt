package mc.jabber.core.chips.logic

import mc.jabber.Global
import mc.jabber.core.auto.ChipID
import mc.jabber.core.chips.ChipProcess
import mc.jabber.core.data.cardinal.CardinalData
import mc.jabber.core.data.serial.LongBox
import mc.jabber.core.data.serial.NbtTransformable
import mc.jabber.core.math.Vec2I
import net.minecraft.util.Identifier

@ChipID("chip_logical_and")
class AndChip : ChipProcess() {
    override val id: Identifier = Global.id("and")

    override fun <T : NbtTransformable<*>> receive(
        data: CardinalData<T>,
        pos: Vec2I,
        chipData: HashMap<Vec2I, NbtTransformable<*>>
    ): CardinalData<T> {
        return if (data.all { _, t -> t != null && if (t is LongBox) t.long > 0 else false}) {
            data.replaceNullNoRemain(LongBox(1))
        } else {
            data.replaceNullNoRemain(LongBox(0))
        }
    }
}
