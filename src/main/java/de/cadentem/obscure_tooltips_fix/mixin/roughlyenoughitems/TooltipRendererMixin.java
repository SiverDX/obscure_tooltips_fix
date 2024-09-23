package de.cadentem.obscure_tooltips_fix.mixin.roughlyenoughitems;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import de.cadentem.obscure_tooltips_fix.OTF;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = TooltipRenderer.class, remap = false)
public abstract class TooltipRendererMixin {
    @Shadow private static ItemStack renderStack;

    @Definition(id = "renderStack", field = "Lcom/obscuria/tooltips/client/renderer/TooltipRenderer;renderStack:Lnet/minecraft/world/item/ItemStack;")
    @Definition(id = "stack", local = @Local(type = ItemStack.class, ordinal = 0))
    @Expression("stack == renderStack")
    @ModifyExpressionValue(method = "updateStyle", at = @At("MIXINEXTRAS:EXPRESSION"))
    private static boolean obscure_tooltips_fix$isSameStack(final boolean original, @Local(argsOnly = true, ordinal = 0) final ItemStack stack) {
        if (!original && OTF.REI_CONTEXT) {
            OTF.REI_CONTEXT = false;

            if (stack.getItem() == renderStack.getItem()) {
                return true;
            }
        }

        return original;
    }
}
