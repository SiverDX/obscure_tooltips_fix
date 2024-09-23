package de.cadentem.obscure_tooltips_fix.mixin.apotheosis;

import de.cadentem.obscure_tooltips_fix.OTF;
import dev.shadowsoffire.apotheosis.ench.EnchModuleClient;
import net.minecraftforge.client.event.ScreenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EnchModuleClient.class, remap = false)
public abstract class EnchModuleClientMixin {
    @Inject(method = "drawAnvilCostBlob", at = @At(value = "INVOKE", target = "Ldev/shadowsoffire/apotheosis/util/DrawsOnLeft;draw(Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;Lnet/minecraft/client/gui/GuiGraphics;Ljava/util/List;I)V", shift = At.Shift.BEFORE))
    private void obscure_tooltips_fix$setSkip(final ScreenEvent.Render.Post event, final CallbackInfo callback) {
        OTF.APOTHEOSIS_SKIP = true;
    }
}
