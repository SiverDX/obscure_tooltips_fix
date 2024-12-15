package de.cadentem.obscure_tooltips_fix.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;

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

        return processJsonObject(jsonElement.getAsJsonObject());
    }

    /**
     * Processes a JsonObject and converts it to a CompoundTag.
     *
     * @param jsonObject The JsonObject to convert.
     * @return CompoundTag representation of the JsonObject.
     */
    private static CompoundTag processJsonObject(final JsonObject jsonObject) {
        CompoundTag compoundTag = new CompoundTag();

        for (String key : jsonObject.keySet()) {
            JsonElement value = jsonObject.get(key);

            if (value.isJsonObject()) {
                // Nested object
                compoundTag.put(key, processJsonObject(value.getAsJsonObject()));
            } else if (value.isJsonArray()) {
                // Array
                compoundTag.put(key, processJsonArray(value.getAsJsonArray()));
            } else if (value.isJsonPrimitive()) {
                // Primitive types
                processJsonPrimitive(compoundTag, key, value);
            } else if (value.isJsonNull()) {
                // Null values ignored or handle specifically.
                compoundTag.putString(key, "null");
            }
        }

        return compoundTag;
    }

    /**
     * Processes a JsonArray and converts it to a ListTag.
     *
     * @param jsonArray The JsonArray to convert.
     * @return ListTag representation of the JsonArray.
     */
    private static ListTag processJsonArray(final JsonArray jsonArray) {
        ListTag listTag = new ListTag();

        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                listTag.add(processJsonObject(element.getAsJsonObject()));
            } else if (element.isJsonArray()) {
                listTag.add(processJsonArray(element.getAsJsonArray()));
            } else if (element.isJsonPrimitive()) {
                addPrimitiveToListTag(listTag, element.getAsJsonPrimitive());
            } else if (element.isJsonNull()) {
                // Handle null case if needed, e.g., add placeholder value
                listTag.add(StringTag.valueOf("null"));
            }
        }

        return listTag;
    }

    /**
     * Processes a JsonPrimitive and adds it to a CompoundTag.
     *
     * @param compoundTag The CompoundTag to add the primitive to.
     * @param key         The key in the CompoundTag.
     * @param value       The JsonPrimitive value.
     */
    private static void processJsonPrimitive(final CompoundTag compoundTag, final String key, final JsonElement value) {
        JsonPrimitive primitive = value.getAsJsonPrimitive();

        if (primitive.isNumber()) {
            Number number = value.getAsNumber();

            if (number instanceof Integer) {
                compoundTag.putInt(key, number.intValue());
            } else if (number instanceof Long) {
                compoundTag.putLong(key, number.longValue());
            } else if (number instanceof Float) {
                compoundTag.putFloat(key, number.floatValue());
            } else {
                // Assume double for other cases
                compoundTag.putDouble(key, number.doubleValue());
            }
        } else if (primitive.isBoolean()) {
            compoundTag.putBoolean(key, value.getAsBoolean());
        } else if (primitive.isString()) {
            compoundTag.putString(key, value.getAsString());
        }
    }

    /**
     * Adds a JsonPrimitive to a ListTag.
     *
     * @param listTag   The ListTag to add the primitive to.
     * @param primitive The JsonPrimitive to add.
     */
    private static void addPrimitiveToListTag(final ListTag listTag, final JsonElement primitive) {
        if (primitive.getAsJsonPrimitive().isNumber()) {
            Number number = primitive.getAsNumber();

            if (number instanceof Integer) {
                listTag.add(IntTag.valueOf(number.intValue()));
            } else if (number instanceof Long) {
                listTag.add(LongTag.valueOf(number.longValue()));
            } else if (number instanceof Float) {
                listTag.add(FloatTag.valueOf(number.floatValue()));
            } else {
                listTag.add(DoubleTag.valueOf(number.doubleValue()));
            }
        } else if (primitive.getAsJsonPrimitive().isBoolean()) {
            listTag.add(ByteTag.valueOf((byte) (primitive.getAsBoolean() ? 1 : 0)));
        } else if (primitive.getAsJsonPrimitive().isString()) {
            listTag.add(StringTag.valueOf(primitive.getAsString()));
        }
    }
}
