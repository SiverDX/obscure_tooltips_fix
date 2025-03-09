package de.cadentem.obscure_tooltips_fix.utils;

import de.cadentem.obscure_tooltips_fix.OTF;
import dev.ftb.mods.ftblibrary.ui.ScreenWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class Compat {
    public static String getScreen() {
        Screen screen = Minecraft.getInstance().screen;

        if (screen == null) {
            return "";
        }

        if (OTF.FTB_LIBRARY && screen instanceof ScreenWrapper wrapper) {
            return wrapper.getGui().getClass().getName();
        }

        return screen.getClass().getName();
    }
}
