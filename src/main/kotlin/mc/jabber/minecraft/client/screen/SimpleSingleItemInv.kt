package mc.jabber.minecraft.client.screen

import net.minecraft.inventory.SimpleInventory

class SimpleSingleItemInv(size: Int) : SimpleInventory(size) {
    override fun getMaxCountPerStack(): Int {
        return 1
    }
}
