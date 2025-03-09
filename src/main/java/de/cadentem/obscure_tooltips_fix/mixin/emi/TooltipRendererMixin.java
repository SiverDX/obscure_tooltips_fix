package de.cadentem.obscure_tooltips_fix.mixin.emi;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import de.cadentem.obscure_tooltips_fix.OTF;
import de.cadentem.obscure_tooltips_fix.config.ClientConfig;
import de.cadentem.obscure_tooltips_fix.utils.Compat;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.screen.BoMScreen;
import dev.emi.emi.screen.EmiScreenManager;
import dev.emi.emi.screen.WidgetGroup;
import net.minecraft.client.Minecraft;
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

            if (EmiApi.getHandledScreen() == null || ClientConfig.SCREEN_BLACKLIST.get().contains(Compat.getScreen())) {
                return tooltipStack;
            }

            List<EmiStack> stacks = List.of();

            if (Minecraft.getInstance().screen instanceof RecipeScreenAccess access) {
                stacks = obscure_tooltips_fix$getRecipeStacks(access, x, y);
            }

            if (Minecraft.getInstance().screen instanceof BoMScreen bomScreen && bomScreen.getHoveredStack(x, y) instanceof HoverAccess access) {
                EmiIngredient ingredient = access.obscure_tooltips_fix$getStack();

                if (ingredient != null) {
                    stacks = ingredient.getEmiStacks();
                }
            }

            if (stacks.isEmpty()) {
                stacks = EmiApi.getHoveredStack(x, y, true).getStack().getEmiStacks();
            }

            // TODO :: iterate through them?
            if (!stacks.isEmpty()) {
                ItemStack stack = stacks.get(0).getItemStack();

                if (!stack.isEmpty()) {
                    obscure_tooltips_fix$switched = true;
                    return stack;
                }
            }
        }

        return tooltipStack;
    }

    @Unique
    private static List<EmiStack> obscure_tooltips_fix$getRecipeStacks(final RecipeScreenAccess access, final int x, final int y) {
        for (WidgetGroup group : access.obscure_tooltips_fix$getCurrentPage()) {
            int actualX = x - group.x();
            int actualY = y - group.y();

            for (Widget widget : group.widgets) {
                if (widget instanceof SlotWidget slotWidget && widget.getBounds().contains(actualX, actualY)) {
                    return slotWidget.getStack().getEmiStacks();
                }
            }
        }

        return List.of();
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