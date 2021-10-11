package mc.jabber.mixin;

import io.github.cottonmc.cotton.gui.ValidatedSlot;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = WItemSlot.class, remap = false)
public interface WItemSlotPeerAccessor {
    @Accessor
    List<ValidatedSlot> getPeers();
}
