package mc.jabber.minecraft.client.screen

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WWidget
import mc.jabber.util.assertType
import mc.jabber.util.peers
import net.minecraft.client.util.math.MatrixStack

object CustomBackgroundPainters {
    var SLOT = BackgroundPainter { matrices: MatrixStack, left: Int, top: Int, panel: WWidget ->
        val slot = panel.assertType<WItemSlot>()
        for (x in 0 until slot.width / 18) {
            for (y in 0 until slot.height / 18) {
                val index = x + y * (slot.width / 18)
                if (slot.peers[index].isTakingAllowed.not()) continue

                val lo = -0x48000000
                val bg = 0x4C000000
                //this will cause a slightly discolored bottom border on vanilla backgrounds but it's necessary for color support, it shouldn't be *too* visible unless you're looking for it
                val hi = -0x47000001
                ScreenDrawing.drawBeveledPanel(
                    matrices, x * 18 + left, y * 18 + top, 16 + 2, 16 + 2,
                    lo, bg, hi
                )
                if (slot.focusedSlot == index) {
                    val sx = x * 18 + left
                    val sy = y * 18 + top
                    ScreenDrawing.coloredRect(matrices, sx, sy, 18, 1, -0x60)
                    ScreenDrawing.coloredRect(matrices, sx, sy + 1, 1, 18 - 1, -0x60)
                    ScreenDrawing.coloredRect(matrices, sx + 18 - 1, sy + 1, 1, 18 - 1, -0x60)
                    ScreenDrawing.coloredRect(matrices, sx + 1, sy + 18 - 1, 18 - 1, 1, -0x60)
                }
            }
        }
    }
}
