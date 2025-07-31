package de.cadentem.obscure_tooltips_fix.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.List;

public interface NBTFilter {
    @Nullable CompoundTag obscure_tooltips_fix$getTag();
    void obscure_tooltips_fix$setTag(final CompoundTag tag);

    void obscure_tooltips_fix$setTags(final List<TagKey<Item>> tags);
}
