package de.cadentem.obscure_tooltips_fix.mixin;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.JsonOps;
import com.obscuria.tooltips.client.style.StyleFilter;
import de.cadentem.obscure_tooltips_fix.OTF;
import de.cadentem.obscure_tooltips_fix.utils.NBTFilter;
import de.cadentem.obscure_tooltips_fix.utils.NBTUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(value = StyleFilter.class, remap = false)
public abstract class StyleFilterMixin implements NBTFilter {
    @Unique @Nullable public CompoundTag obscure_tooltips_fix$NBT;

    @Unique @Nullable public List<TagKey<Item>> obscure_tooltips_fix$tags;

    @ModifyReturnValue(method = "fromJson", at = @At("RETURN"))
    private static StyleFilter setObscure_tooltips_fix$parseTag(final StyleFilter filter, @Local(argsOnly = true) final JsonObject root) {
        if (root.has("tag")) {
            CompoundTag tag = NBTUtils.toCompound(root.getAsJsonObject("tag"));
            ((NBTFilter) filter).obscure_tooltips_fix$setTag(tag);
        }

        if (root.has("item_tags")) {
            TagKey.codec(Registries.ITEM).listOf().decode(JsonOps.INSTANCE, root.getAsJsonArray("item_tags"))
                    .resultOrPartial(OTF.LOG::error)
                    .ifPresent(result -> ((NBTFilter) filter).obscure_tooltips_fix$setTags(result.getFirst()));
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

    @ModifyReturnValue(method = "test", at = @At("RETURN"))
    private boolean obscure_tooltips_fix$testItems(final boolean isValid, @Local(argsOnly = true) final ItemStack stack) {
        if (obscure_tooltips_fix$tags != null && !obscure_tooltips_fix$tags.isEmpty()) {
            for (TagKey<Item> tag : obscure_tooltips_fix$tags) {
                if (!stack.getItemHolder().is(tag)) {
                    return false;
                }
            }
        }

        return isValid;
    }

    @Override
    public void obscure_tooltips_fix$setTag(final CompoundTag tag) {
        obscure_tooltips_fix$NBT = tag;
    }

    @Override
    public @Nullable CompoundTag obscure_tooltips_fix$getTag() {
        return obscure_tooltips_fix$NBT;
    }

    @Override
    public void obscure_tooltips_fix$setTags(final List<TagKey<Item>> tags) {
        this.obscure_tooltips_fix$tags = tags;
    }
}
