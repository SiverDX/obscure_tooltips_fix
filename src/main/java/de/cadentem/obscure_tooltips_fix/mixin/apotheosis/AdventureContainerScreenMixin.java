package de.cadentem.obscure_tooltips_fix.mixin.apotheosis;

import de.cadentem.obscure_tooltips_fix.OTF;
import dev.shadowsoffire.apotheosis.adventure.client.AdventureContainerScreen;
import dev.shadowsoffire.apotheosis.util.DrawsOnLeft;
import dev.shadowsoffire.placebo.screen.PlaceboContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(AdventureContainerScreen.class)
public abstract class AdventureContainerScreenMixin<T extends AbstractContainerMenu> extends PlaceboContainerScreen<T> implements DrawsOnLeft {
    public AdventureContainerScreenMixin(final T menu, final Inventory inventory, final Component title) {
        super(menu, inventory, title);
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void drawOnLeft(final GuiGraphics graphics, final List<Component> components, final int y, final int maxWidth) {
        // Cannot call 'DrawsOnLeft.super.drawOnLeft(graphics, components, y, maxWidth)' due to 'interface method reference is in an indirect superinterface'
        if (!components.isEmpty()) {
            OTF.APOTHEOSIS_SKIP = true;

            List<FormattedText> split = new ArrayList<>();
            components.forEach(component -> split.addAll(Minecraft.getInstance().font.getSplitter().splitLines(component, maxWidth, component.getStyle())));

            int x = ths().getGuiLeft() - 16 - split.stream().map(Minecraft.getInstance().font::width).max(Integer::compare).get();
            graphics.renderComponentTooltip(Minecraft.getInstance().font, split, x, y, ItemStack.EMPTY);
        }
    }
}
