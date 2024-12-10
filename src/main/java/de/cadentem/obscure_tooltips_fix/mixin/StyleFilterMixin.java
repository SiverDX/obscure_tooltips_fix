package de.cadentem.obscure_tooltips_fix.mixin;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.obscuria.tooltips.client.style.StyleFilter;
import de.cadentem.obscure_tooltips_fix.utils.NBTUtils;
import de.cadentem.obscure_tooltips_fix.utils.NBTFilter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = StyleFilter.class, remap = false)
public abstract class StyleFilterMixin implements NBTFilter {
    @Unique public final List<CompoundTag> obscure_tooltips_fix$NBT = new ArrayList<>();

    @ModifyReturnValue(method = "fromJson", at = @At("RETURN"))
    private static StyleFilter setObscure_tooltips_fix$parseTag(final StyleFilter filter, @Local(argsOnly = true) final JsonObject root) {
        ((NBTFilter) filter).obscure_tooltips_fix$getNBT().clear();

        if (root.has("tag")) {
            CompoundTag tag = NBTUtils.toCompound(root.getAsJsonObject("tag"));
            ((NBTFilter) filter).obscure_tooltips_fix$getNBT().add(tag);
        }

        return filter;
    }

    /**
     * @author Cadentem
     * @reason Proper NBT check
     */
    @Overwrite
    private boolean hasTagMismatch(final ItemStack stack) {
        if (!stack.hasTag()) {
            return true;
        } else {
            CompoundTag tag = stack.getOrCreateTag();

            for (CompoundTag nbt : obscure_tooltips_fix$NBT) {
                if (!NBTUtils.matches(nbt, tag)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public List<CompoundTag> obscure_tooltips_fix$getNBT() {
        return obscure_tooltips_fix$NBT;
    }
}
