package de.cadentem.obscure_tooltips_fix.mixin.roughlyenoughitems;

import de.cadentem.obscure_tooltips_fix.OTF;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.impl.client.gui.forge.ScreenOverlayImplForge;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenOverlayImplForge.class)
public abstract class ScreenOverlayImplForgeMixin {
    @Inject(method = "renderTooltipInner", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V", shift = At.Shift.BEFORE))
    private void obscure_tooltips_fix$setContext(final Screen screen, final GuiGraphics graphics, final Tooltip tooltip, final int mouseX, final int mouseY, final CallbackInfo callback) {
        OTF.REI_CONTEXT = true;
    }
}
