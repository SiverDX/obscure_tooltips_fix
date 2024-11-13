package de.cadentem.obscure_tooltips_fix.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import de.cadentem.obscure_tooltips_fix.OTF;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = TooltipRenderer.class, remap = false)
public abstract class TooltipRendererMixin {
    @Shadow private static ItemStack renderStack;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", ordinal = 0, shift = At.Shift.AFTER, remap = true), cancellable = true)
    private static void obscure_tooltips_fix$skipSidePanel(final TooltipContext context, final ItemStack stack, final Font font, final List<ClientTooltipComponent> components, final int x, final int y, final ClientTooltipPositioner positioner, final CallbackInfoReturnable<Boolean> callback) {
        if (OTF.SKIP_SIDE_PANELS) {
            OTF.SKIP_SIDE_PANELS = false;
            context.flush();
            callback.setReturnValue(true);
        }
    }

    @Definition(id = "renderStack", field = "Lcom/obscuria/tooltips/client/renderer/TooltipRenderer;renderStack:Lnet/minecraft/world/item/ItemStack;")
    @Definition(id = "stack", local = @Local(type = ItemStack.class, ordinal = 0))
    @Expression("stack == renderStack")
    @ModifyExpressionValue(method = "updateStyle", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean obscure_tooltips_fix$isSameStack(final boolean original, @Local(argsOnly = true, ordinal = 0) final ItemStack stack) {
        if (!original) {
            // Mods may re-create the rendered item stack resulting in '==' compare not working as intended
            // This may result in the animation not being played for different item stacks which match completely
            // The only way to fix that would be to add specific compatibility for each mod doing rendering this way
            return ItemStack.matches(stack, renderStack);
        }

        return true;
    }
}
