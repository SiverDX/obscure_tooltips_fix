package de.cadentem.obscure_tooltips_fix.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.screen.EmiScreenManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(value = TooltipRenderer.class, remap = false)
public abstract class TooltipRendererMixin {
    @Shadow private static ItemStack renderStack;
    @Unique private static boolean obscure_tooltip_fix$switched;

    @ModifyVariable(method = "render", at = @At(value = "HEAD"), argsOnly = true)
    private static ItemStack obscure_tooltips_fix$handleEmptyStack(final ItemStack tooltipStack, /* Method arguments */ final TooltipContext context, final ItemStack ignored, final Font font, final List<ClientTooltipComponent> components, final int x, final int y) {
        if (tooltipStack.isEmpty()) {
            List<EmiStack> stacks = EmiScreenManager.getHoveredStack(x, y, true, true).getStack().getEmiStacks();

            if (!stacks.isEmpty()) {
                obscure_tooltip_fix$switched = true;
                return stacks.get(0).getItemStack();
            }
        }

        return tooltipStack;
    }

    @Definition(id = "renderStack", field = "Lcom/obscuria/tooltips/client/renderer/TooltipRenderer;renderStack:Lnet/minecraft/world/item/ItemStack;")
    @Definition(id = "stack", local = @Local(type = ItemStack.class, ordinal = 0))
    @Expression("stack == renderStack")
    @ModifyExpressionValue(method = "updateStyle", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean obscure_tooltips_fix$isSameStack(final boolean original, @Local(argsOnly = true, ordinal = 0) final ItemStack stack) {
        if (!original && obscure_tooltip_fix$switched) {
            obscure_tooltip_fix$switched = false;

            if (stack.getItem() == renderStack.getItem()) {
                return true;
            }
        }

        return original;
    }
}