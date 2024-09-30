package de.cadentem.obscure_tooltips_fix.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue FTB_QUESTS_SKIP_SIDE_PANELS;

    static {
        FTB_QUESTS_SKIP_SIDE_PANELS = BUILDER.comment("Don't render side panels (weapon / armor) when viewed in FTB Quests").define("ftb_quests_skip_side_panels", false);

        SPEC = BUILDER.build();
    }
}
