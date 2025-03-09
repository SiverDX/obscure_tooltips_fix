package de.cadentem.obscure_tooltips_fix.utils;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class KeyHandler {
    private static final KeyMapping PRINT_SCREEN = new KeyMapping("obscure_tooltips_fix.print_screen", KeyConflictContext.GUI, InputConstants.UNKNOWN, "Obscure Tooltips Fix");

    public static void registerKey(final RegisterKeyMappingsEvent event) {
        event.register(PRINT_SCREEN);
    }

    @SubscribeEvent
    public static void handleMouse(final InputEvent.MouseButton.Pre event) {
        printCurrentScreen(InputConstants.Type.MOUSE.getOrCreate(event.getButton()), event.getAction());
    }

    @SubscribeEvent
    public static void handleKey(final InputEvent.Key event) {
        printCurrentScreen(InputConstants.getKey(event.getKey(), event.getScanCode()), event.getAction());
    }

    private static void printCurrentScreen(final InputConstants.Key input, final int action) {
        if (action != InputConstants.PRESS) {
            return;
        }

        if (Minecraft.getInstance().screen == null || Minecraft.getInstance().player == null) {
            return;
        }

        if (PRINT_SCREEN.isConflictContextAndModifierActive() && PRINT_SCREEN.getKey().equals(input)) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal(Compat.getScreen()), false);
        }
    }
}
