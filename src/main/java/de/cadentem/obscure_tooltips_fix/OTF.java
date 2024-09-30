package de.cadentem.obscure_tooltips_fix;

import de.cadentem.obscure_tooltips_fix.config.ClientConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(OTF.MODID)
public class OTF {
    public static final String MODID = "obscure_tooltips_fix";

    public static boolean SKIP_SIDE_PANELS;
    public static boolean CLEAR_LAST_HOVERED;

    public static boolean JEI_CONTEXT;
    public static boolean REI_CONTEXT;
    public static boolean APOTHEOSIS_SKIP;
    public static boolean FTBQUESTS_SKIP;

    public OTF() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}