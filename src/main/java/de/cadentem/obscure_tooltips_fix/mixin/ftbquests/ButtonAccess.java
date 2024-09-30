package de.cadentem.obscure_tooltips_fix.mixin.ftbquests;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Button.class)
public interface ButtonAccess {
    @Accessor(value = "icon", remap = false)
    Icon obscure_tooltips_fix$getIcon();
}
