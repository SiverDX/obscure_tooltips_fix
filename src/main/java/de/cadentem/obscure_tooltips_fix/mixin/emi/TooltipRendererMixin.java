package de.cadentem.obscure_tooltips_fix.mixin.emi;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import de.cadentem.obscure_tooltips_fix.OTF;
import dev.emi.emi.api.EmiApi;
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
    @Unique private static boolean obscure_tooltips_fix$switched;

    @ModifyVariable(method = "render", at = @At(value = "HEAD"), argsOnly = true)
    private static ItemStack obscure_tooltips_fix$handleEmptyStack(final ItemStack tooltipStack, /* Method arguments */ final TooltipContext context, final ItemStack ignored, final Font font, final List<ClientTooltipComponent> components, final int x, final int y) {
        if (tooltipStack.isEmpty()) {
            if (OTF.CLEAR_LAST_HOVERED) {
                EmiScreenManager.lastStackTooltipRendered = null;
                OTF.CLEAR_LAST_HOVERED = false;
            }

            List<EmiStack> stacks = EmiApi.getHoveredStack(x, y, true).getStack().getEmiStacks();

            if (!stacks.isEmpty()) {
                obscure_tooltips_fix$switched = true;
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
        if (!original && obscure_tooltips_fix$switched) {
            obscure_tooltips_fix$switched = false;

            // In certain cases (e.g. with 'Tom's Simple Storage Mod') EMI will provide the item with a stack count of 1 while the actual stack size is 64 or sth. else
            // Therefor skip checking if the count matches to avoid display issues
            if (ItemStack.isSameItemSameTags(stack, renderStack)) {
                return true;
            }
        }

        return original;
    }
}