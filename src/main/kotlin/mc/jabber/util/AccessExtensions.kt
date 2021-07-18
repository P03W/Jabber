package mc.jabber.util

import io.github.cottonmc.cotton.gui.ValidatedSlot
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import mc.jabber.mixin.WItemSlotPeerAccessor

val WItemSlot.peers: List<ValidatedSlot> get() = (this as WItemSlotPeerAccessor).peers
