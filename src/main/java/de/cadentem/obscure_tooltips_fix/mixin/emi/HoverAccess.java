package de.cadentem.obscure_tooltips_fix.mixin.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "dev.emi.emi.screen.BoMScreen$Hover", remap = false)
public interface HoverAccess {
    @Accessor("stack")
    EmiIngredient obscure_tooltips_fix$getStack();
}
