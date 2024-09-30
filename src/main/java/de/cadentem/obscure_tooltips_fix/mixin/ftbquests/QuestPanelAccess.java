package de.cadentem.obscure_tooltips_fix.mixin.ftbquests;

import dev.ftb.mods.ftbquests.client.gui.quests.QuestButton;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestPanel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(QuestPanel.class)
public interface QuestPanelAccess {
    @Accessor(value = "mouseOverQuest", remap = false)
    QuestButton obscure_tooltips_fix$getMouseOverQuest();
}
