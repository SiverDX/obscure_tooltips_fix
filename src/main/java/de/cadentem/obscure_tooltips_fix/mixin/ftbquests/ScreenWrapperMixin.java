package de.cadentem.obscure_tooltips_fix.mixin.ftbquests;

import de.cadentem.obscure_tooltips_fix.OTF;
import de.cadentem.obscure_tooltips_fix.config.ClientConfig;
import dev.ftb.mods.ftblibrary.icon.AtlasSpriteIcon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.util.TooltipList;
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
    @Shadow(remap = false) @Final private BaseScreen wrappedGui;
    @Shadow(remap = false) @Final private TooltipList tooltipList;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", shift = At.Shift.BEFORE))
    private void obscure_tooltips_fix$setSkip(final GuiGraphics graphics, final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo callback) {
        for (Widget widget : wrappedGui.getWidgets()) {
            if (widget instanceof QuestPanelAccess panel) {
                QuestButton quest = panel.obscure_tooltips_fix$getMouseOverQuest();

                if (quest instanceof ButtonAccess button && button.obscure_tooltips_fix$getIcon() instanceof AtlasSpriteIcon) {
                    OTF.FTBQUESTS_SKIP = true;
                    break;
                }
            }
        }

        if (!OTF.FTBQUESTS_SKIP && ClientConfig.FTB_QUESTS_SKIP_SIDE_PANELS.get()) {
            OTF.SKIP_SIDE_PANELS = true;
        }
    }

    /** When clicking a task the viewed stack stays as the last hovered item */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Ldev/ftb/mods/ftblibrary/util/TooltipList;reset()V", shift = At.Shift.BEFORE, remap = false))
    private void obscure_tooltips_fix$setContext(final GuiGraphics graphics, final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo callback) {
        OTF.CLEAR_LAST_HOVERED = !tooltipList.shouldRender();
    }
}
