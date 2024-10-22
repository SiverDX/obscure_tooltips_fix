package de.cadentem.obscure_tooltips_fix.mixin.ftbquests;

import de.cadentem.obscure_tooltips_fix.OTF;
import dev.ftb.mods.ftbquests.client.gui.quests.ChapterImageButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** When images are hovered while they are above the EMI side panel it will show the icon of the "hovered" EMI item */
@Mixin(value = ChapterImageButton.class, remap = false)
public abstract class ChapterImageButtonMixin {
    @Inject(method = "checkMouseOver", at = @At("RETURN"))
    private void obscure_tooltips_fix$shouldSkip(int mouseX, int mouseY, final CallbackInfoReturnable<Boolean> callback) {
        if (callback.getReturnValue()) {
            OTF.FTBQUESTS_SKIP = true;
        }
    }
}
