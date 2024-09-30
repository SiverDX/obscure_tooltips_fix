package de.cadentem.obscure_tooltips_fix.mixin.ftbquests;

import de.cadentem.obscure_tooltips_fix.OTF;
import dev.ftb.mods.ftblibrary.icon.AtlasSpriteIcon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestButton;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenWrapper.class)
public abstract class ScreenWrapperMixin {
    @Shadow @Final private BaseScreen wrappedGui;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", shift = At.Shift.BEFORE))
    private void obscure_tooltips_fix$setContext(final GuiGraphics graphics, final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo callback) {
        for (Widget widget : wrappedGui.getWidgets()) {
            if (widget instanceof QuestPanelAccess panel) {
                QuestButton quest = panel.obscure_tooltips_fix$getMouseOverQuest();

                if (quest instanceof ButtonAccess button && button.obscure_tooltips_fix$getIcon() instanceof AtlasSpriteIcon) {
                    OTF.FTBQUESTS_SKIP = true;
                    break;
                }
            }
        }
    }
}