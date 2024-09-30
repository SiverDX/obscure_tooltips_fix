 package de.cadentem.obscure_tooltips_fix.mixin;

 import net.minecraftforge.fml.loading.LoadingModList;
 import org.objectweb.asm.tree.ClassNode;
 import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
 import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

 import java.util.List;
 import java.util.Set;

public class ApplyMixinPlugin implements IMixinConfigPlugin {
    private final static String PREFIX = ApplyMixinPlugin.class.getPackageName() + ".";

    @Override
    public void onLoad(final String mixinPackage) { /* Nothing to do */ }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
        String directory = mixinClassName.replace(PREFIX, "");
        directory = directory.replace("client.", "");
        String[] elements = directory.split("\\.");

        if (elements.length == 2) {
            String modid = elements[0];

            if (modid.equals("jei")) {
                return LoadingModList.get().getModFileById("emi") == null;
            }

            return LoadingModList.get().getModFileById(modid) != null;
        }

        return false;
    }

    @Override
    public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) { /* Nothing to do */ }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) { /* Nothing to do */ }

    @Override
    public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) { /* Nothing to do */ }
}