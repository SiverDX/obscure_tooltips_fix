package de.cadentem.obscure_tooltips_fix.mixin;

import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import de.cadentem.obscure_tooltips_fix.OTF;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TooltipRenderer.class)
public abstract class TooltipRendererMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", ordinal = 0, shift = At.Shift.AFTER), cancellable = true)
    private static void obscure_tooltips_fix$skipSidePanel(final TooltipContext context, final ItemStack stack, final Font font, final List<ClientTooltipComponent> components, final int x, final int y, final ClientTooltipPositioner positioner, final CallbackInfoReturnable<Boolean> callback) {
        if (OTF.SKIP_SIDE_PANELS) {
            OTF.SKIP_SIDE_PANELS = false;
            context.flush();
            callback.setReturnValue(true);
        }
    }
}
