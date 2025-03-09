package de.cadentem.obscure_tooltips_fix.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue FTB_QUESTS_SKIP_SIDE_PANELS;
    public static final ForgeConfigSpec.ConfigValue<List<String>> SCREEN_BLACKLIST;

    static {
        FTB_QUESTS_SKIP_SIDE_PANELS = BUILDER.comment("Don't render side panels (weapon / armor) when viewed in FTB Quests").define("ftb_quests_skip_side_panels", false);
        SCREEN_BLACKLIST = BUILDER.comment("Screens which have issues (items showing when hovering in the area EMI is present even though it is hidden").define("screen_blacklist", List.of());

        SPEC = BUILDER.build();
    }
}
