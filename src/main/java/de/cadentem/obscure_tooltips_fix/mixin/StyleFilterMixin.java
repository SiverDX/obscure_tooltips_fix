package de.cadentem.obscure_tooltips_fix.mixin;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.obscuria.tooltips.client.style.StyleFilter;
import de.cadentem.obscure_tooltips_fix.utils.NBTFilter;
import de.cadentem.obscure_tooltips_fix.utils.NBTUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(value = StyleFilter.class, remap = false)
public abstract class StyleFilterMixin implements NBTFilter {
    @Unique @Nullable public CompoundTag obscure_tooltips_fix$NBT;

    @ModifyReturnValue(method = "fromJson", at = @At("RETURN"))
    private static StyleFilter setObscure_tooltips_fix$parseTag(final StyleFilter filter, @Local(argsOnly = true) final JsonObject root) {
        if (root.has("tag")) {
            CompoundTag tag = NBTUtils.toCompound(root.getAsJsonObject("tag"));
            ((NBTFilter) filter).obscure_tooltips_fix$setTag(tag);
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
            return !NBTUtils.matches(obscure_tooltips_fix$getTag(), tag);
        }
    }

    @Override
    public void obscure_tooltips_fix$setTag(final CompoundTag tag) {
        obscure_tooltips_fix$NBT = tag;
    }

    @Override
    public @Nullable CompoundTag obscure_tooltips_fix$getTag() {
        return obscure_tooltips_fix$NBT;
    }
}
