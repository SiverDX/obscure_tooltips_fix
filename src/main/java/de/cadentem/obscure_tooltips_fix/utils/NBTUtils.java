package de.cadentem.obscure_tooltips_fix.utils;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import de.cadentem.obscure_tooltips_fix.OTF;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;

/** Totally not AI generated */
public class NBTUtils {
    /**
     * Performs a fuzzy comparison between two CompoundTags.
     *
     * @param base        The base CompoundTag.
     * @param toCompare   The CompoundTag to compare against the base.
     * @return True if the tags are considered equal within the fuzzy constraints, false otherwise.
     */
    public static boolean matches(final CompoundTag base, final CompoundTag toCompare) {
        if (base == null || base.isEmpty()) {
            return toCompare == null || toCompare.isEmpty();
        }

        for (String key : base.getAllKeys()) {
            if (!toCompare.contains(key)) {
                return false;
            }

            Tag baseTag = base.get(key);
            Tag compareTag = toCompare.get(key);

            if (baseTag == null || compareTag == null) {
                return false;
            }

            if (!matches(baseTag, compareTag)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Fuzzy compares two NBT Tags (can be primitive, lists, or compound).
     *
     * @param baseTag        The first Tag to compare.
     * @param otherTag        The second Tag to compare.
     * @return True if the tags are considered equal within the fuzzy constraints, false otherwise.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") // ignore
    private static boolean matches(final Tag baseTag, final Tag otherTag) {
        if (baseTag instanceof NumericTag first && otherTag instanceof NumericTag second) {
            // Since there is no proper way to specify an integer in the style filter
            // It will therefor convert numbers from 0 to 8 to bytes (0d to 8d) and make it a ByteTag, no longer matching properly
            return first.getAsDouble() == second.getAsDouble();
        }

        if (baseTag.getType() != otherTag.getType()) {
            return false;
        }

        if (baseTag instanceof CompoundTag) {
            // Recursive comparison for CompoundTags
            return matches((CompoundTag) baseTag, (CompoundTag) otherTag);
        } else if (baseTag instanceof ListTag) {
            // Compare lists by content, ignoring order if necessary
            return matches((ListTag) baseTag, (ListTag) otherTag);
        } else {
            // For primitive tags, compare their values directly
            return baseTag.equals(otherTag);
        }
    }

    /**
     * Fuzzy compares two ListTags.
     *
     * @param baseList       The first ListTag.
     * @param otherList       The second ListTag.
     * @return True if the lists are considered equal within the fuzzy constraints, false otherwise.
     */
    private static boolean matches(final ListTag baseList, final ListTag otherList) {
        if (baseList.size() != otherList.size()) {
            return false;
        }

        for (int i = 0; i < baseList.size(); i++) {
            Tag baseTag = baseList.get(i);
            Tag otherTag = otherList.get(i);

            if (!matches(baseTag, otherTag)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Converts a JsonElement to a CompoundTag.
     *
     * @param jsonElement The JsonElement to convert.
     * @return CompoundTag representation of the JsonElement.
     */
    public static CompoundTag toCompound(final JsonElement jsonElement) {
        if (!jsonElement.isJsonObject()) {
            CompoundTag simple = new CompoundTag();
            simple.putString("tag", jsonElement.getAsString());
            return simple;
        }

        return CompoundTag.CODEC.parse(JsonOps.INSTANCE, jsonElement.getAsJsonObject()).resultOrPartial(OTF.LOG::error).orElse(new CompoundTag());
    }
}
