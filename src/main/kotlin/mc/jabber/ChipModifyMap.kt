package mc.jabber

import mc.jabber.Global.ITEMS.CHIP_QUADRANT_1_PIPE
import mc.jabber.Global.ITEMS.CHIP_QUADRANT_2_PIPE
import mc.jabber.Global.ITEMS.CHIP_QUADRANT_3_PIPE
import mc.jabber.Global.ITEMS.CHIP_QUADRANT_4_PIPE
import mc.jabber.minecraft.items.ChipItem
import net.minecraft.item.Item

object ChipModifyMap {
    sealed interface ModifyType
    class ChangeTypeModify(val other: List<ChipItem>): ModifyType {
        override fun toString(): String {
            return other.toString()
        }
    }
    class InputModify<in T>(val action: (T) -> Unit): ModifyType

    private val map = mapOf(
        CHIP_QUADRANT_1_PIPE to ChangeTypeModify(listOf(CHIP_QUADRANT_2_PIPE, CHIP_QUADRANT_3_PIPE, CHIP_QUADRANT_4_PIPE)),
        CHIP_QUADRANT_2_PIPE to ChangeTypeModify(listOf(CHIP_QUADRANT_1_PIPE, CHIP_QUADRANT_3_PIPE, CHIP_QUADRANT_4_PIPE)),
        CHIP_QUADRANT_3_PIPE to ChangeTypeModify(listOf(CHIP_QUADRANT_1_PIPE, CHIP_QUADRANT_2_PIPE, CHIP_QUADRANT_4_PIPE)),
        CHIP_QUADRANT_4_PIPE to ChangeTypeModify(listOf(CHIP_QUADRANT_1_PIPE, CHIP_QUADRANT_2_PIPE, CHIP_QUADRANT_3_PIPE))
    )

    operator fun get(item: ChipItem): ChangeTypeModify? {
        return map[item]
    }
}
