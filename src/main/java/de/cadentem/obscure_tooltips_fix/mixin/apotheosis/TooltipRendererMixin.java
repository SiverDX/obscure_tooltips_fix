package de.cadentem.obscure_tooltips_fix.mixin.apotheosis;

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

/** These calls are only meant to render some side-tooltips which are not for the actual item (which is why the stack is 'ItemStack.EMPTY') */
@Mixin(value = TooltipRenderer.class, remap = false)
public abstract class TooltipRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private static void obscure_tooltips_fix$skip(final TooltipContext context, final ItemStack stack, final Font font, final List<ClientTooltipComponent> components, final int x, final int y, final ClientTooltipPositioner positioner, final CallbackInfoReturnable<Boolean> callback) {
        if (OTF.APOTHEOSIS_SKIP) {
            OTF.APOTHEOSIS_SKIP = false;
            callback.setReturnValue(false);
        }
    }
}
