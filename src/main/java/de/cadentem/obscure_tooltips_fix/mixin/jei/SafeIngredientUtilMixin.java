package de.cadentem.obscure_tooltips_fix.mixin.jei;

import de.cadentem.obscure_tooltips_fix.OTF;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.gui.JeiTooltip;
import mezz.jei.common.util.SafeIngredientUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SafeIngredientUtil.class, remap = false)
public abstract class SafeIngredientUtilMixin {
    @Inject(method = "renderTooltip", at = @At("HEAD"))
    private static <T> void obscure_tooltips_fix$setContext(final GuiGraphics graphics, final JeiTooltip tooltip, final int x, final int y, final Font font, final ItemStack stack, final ITypedIngredient<T> ingredient, final IIngredientManager manager, final CallbackInfo callback) {
        OTF.JEI_CONTEXT = true;
    }
}
