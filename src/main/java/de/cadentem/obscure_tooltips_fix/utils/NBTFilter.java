package de.cadentem.obscure_tooltips_fix.utils;

import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;

public interface NBTFilter {
    @Nullable CompoundTag obscure_tooltips_fix$getTag();
    void obscure_tooltips_fix$setTag(final CompoundTag tag);
}
