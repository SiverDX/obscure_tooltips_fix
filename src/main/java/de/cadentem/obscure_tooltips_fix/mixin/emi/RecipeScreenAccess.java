package de.cadentem.obscure_tooltips_fix.mixin.emi;

import dev.emi.emi.screen.RecipeScreen;
import dev.emi.emi.screen.WidgetGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = RecipeScreen.class, remap = false)
public interface RecipeScreenAccess {
    @Accessor("currentPage")
    List<WidgetGroup> obscure_tooltips_fix$getCurrentPage();
}
