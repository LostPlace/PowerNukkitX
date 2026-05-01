package cn.nukkit.nbt.tag;

import io.netty.util.internal.EmptyArrays;
import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public class CompoundTag extends Tag {
    protected NbtMap map;

    public CompoundTag() {
        this(NbtMap.EMPTY);
    }

    public CompoundTag(NbtMap map) {
        this.map = map == null ? NbtMap.EMPTY : map;
    }

    protected CompoundTag(Map<String, Tag> tags) {
        this.map = NbtMap.fromMap(tagsToRawMap(tags));
    }

    public Collection<Object> getAllTags() {
        return map.values();
    }

    @Override
    public byte getId() {
        return TAG_Compound;
    }

    public CompoundTag put(String name, Tag tag) {
        NbtMapBuilder builder = map.toBuilder();
        builder.put(name, Tag.toRaw(tag));
        map = builder.build();
        return this;
    }

    public CompoundTag putByte(String name, int value) {
        return put(name, new ByteTag(value));
    }

    public CompoundTag putShort(String name, int value) {
        return put(name, new ShortTag(value));
    }

    public CompoundTag putInt(String name, int value) {
        return put(name, new IntTag(value));
    }

    public CompoundTag putLong(String name, long value) {
        return put(name, new LongTag(value));
    }

    public CompoundTag putFloat(String name, float value) {
        return put(name, new FloatTag(value));
    }

    public CompoundTag putDouble(String name, double value) {
        return put(name, new DoubleTag(value));
    }

    public CompoundTag putString(@Nullable String name, @NotNull String value) {
        return put(name, new StringTag(value));
    }

    public CompoundTag putByteArray(String name, byte[] value) {
        return put(name, new ByteArrayTag(value));
    }

    public CompoundTag putIntArray(String name, int[] value) {
        return put(name, new IntArrayTag(value));
    }

    public CompoundTag putList(String name, ListTag<? extends Tag> listTag) {
        return put(name, listTag);
    }

    public CompoundTag putCompound(String name, CompoundTag value) {
        return put(name, value);
    }

    public CompoundTag putBoolean(String name, boolean val) {
        return putByte(name, val ? 1 : 0);
    }

    public Tag get(String name) {
        return Tag.fromRaw(map.get(name));
    }

    public boolean contains(String name) {
        return map.containsKey(name);
    }

    public boolean containsCompound(String name) {
        return map.get(name) instanceof NbtMap;
    }

    public boolean containsString(String name) {
        return map.get(name) instanceof String;
    }

    public boolean containsIntArray(String name) {
        return map.get(name) instanceof int[];
    }

    public boolean containsByteArray(String name) {
        return map.get(name) instanceof byte[];
    }

    public boolean containsNumber(String name) {
        return map.get(name) instanceof Number;
    }

    public boolean containsList(String name) {
        return map.get(name) instanceof List<?>;
    }

    public boolean containsList(String name, byte type) {
        Object value = map.get(name);
        if (!(value instanceof NbtList<?> list)) {
            return false;
        }
        return list.getType().getId() == type || list.getType().getId() == TAG_End;
    }

    public boolean containsByte(String name) {
        return map.get(name) instanceof Byte;
    }

    public boolean containsShort(String name) {
        return map.get(name) instanceof Short;
    }

    public boolean containsInt(String name) {
        return map.get(name) instanceof Integer;
    }

    public boolean containsLong(String name) {
        return map.get(name) instanceof Long;
    }

    public boolean containsDouble(String name) {
        return map.get(name) instanceof Double;
    }

    public boolean containsFloat(String name) {
        return map.get(name) instanceof Float;
    }

    public CompoundTag remove(String name) {
        NbtMapBuilder builder = map.toBuilder();
        builder.remove(name);
        map = builder.build();
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Tag> T removeAndGet(String name) {
        Tag removed = get(name);
        remove(name);
        return (T) removed;
    }

    public byte getByte(String name) {
        return getByte(name, (byte) 0);
    }

    public short getShort(String name) {
        return getShort(name, (short) 0);
    }

    public int getInt(String name) {
        return getInt(name, 0);
    }

    public long getLong(String name) {
        return getLong(name, 0L);
    }

    public float getFloat(String name) {
        return getFloat(name, 0f);
    }

    public double getDouble(String name) {
        return getDouble(name, 0D);
    }

    public String getString(String name) {
        return getString(name, "");
    }

    public byte[] getByteArray(String name) {
        return getByteArray(name, EmptyArrays.EMPTY_BYTES);
    }

    public int[] getIntArray(String name) {
        return getIntArray(name, EmptyArrays.EMPTY_INTS);
    }

    public CompoundTag getCompound(String name) {
        return getCompound(name, new CompoundTag());
    }

    public ListTag<? extends Tag> getList(String name) {
        return getList(name, new ListTag<>());
    }

    public <T extends Tag> ListTag<T> getList(String name, Class<T> type) {
        return getList(name, type, new ListTag<>());
    }

    public <T> List<T> getList(String name, NbtType<T> type) {
        return getList(name, type, new NbtList<>(type, Collections.emptyList()));
    }

    public byte getByte(String name, byte defaultValue) {
        return map.getByte(name, defaultValue);
    }

    public short getShort(String name, short defaultValue) {
        return map.getShort(name, defaultValue);
    }

    public int getInt(String name, int defaultValue) {
        return map.getInt(name, defaultValue);
    }

    public long getLong(String name, long defaultValue) {
        return map.getLong(name, defaultValue);
    }

    public float getFloat(String name, float defaultValue) {
        return map.getFloat(name, defaultValue);
    }

    public double getDouble(String name, double defaultValue) {
        return map.getDouble(name, defaultValue);
    }

    public String getString(String name, String defaultValue) {
        Object value = map.get(name);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return String.valueOf(number);
        }
        return value instanceof String string ? string : defaultValue;
    }

    public byte[] getByteArray(String name, byte[] defaultValue) {
        return map.getByteArray(name, defaultValue);
    }

    public int[] getIntArray(String name, int[] defaultValue) {
        return map.getIntArray(name, defaultValue);
    }

    public CompoundTag getCompound(String name, CompoundTag defaultValue) {
        NbtMap compound = map.getCompound(name);
        return compound == null ? defaultValue : new CompoundTag(compound);
    }

    public <T> List<T> getList(String name, NbtType<T> type, List<T> defaultValue) {
        List<T> list = map.getList(name, type);
        return list == null ? defaultValue : list;
    }

    public ListTag<? extends Tag> getList(String name, ListTag<? extends Tag> defaultValue) {
        Object value = map.get(name);
        return value instanceof List<?> list ? ListTag.fromRaw(list) : defaultValue;
    }

    @SuppressWarnings("unchecked")
    public <T extends Tag> ListTag<T> getList(String name, Class<T> type, ListTag<T> defaultValue) {
        ListTag<? extends Tag> list = getList(name, (ListTag<? extends Tag>) defaultValue);
        if (list == defaultValue) {
            return defaultValue;
        }
        byte expectedType = (byte) Tag.getTagType(type);
        if (list.type != TAG_End && list.type != expectedType) {
            return defaultValue;
        }
        return (ListTag<T>) list;
    }

    public Map<String, Tag> getTags() {
        Map<String, Tag> tags = new HashMap<>(map.size());
        for (Entry<String, Object> entry : map.entrySet()) {
            tags.put(entry.getKey(), Tag.fromRaw(entry.getValue()));
        }
        return tags;
    }

    @UnmodifiableView
    public Set<Map.Entry<String, Tag>> getEntrySet() {
        return Collections.unmodifiableSet(getTags().entrySet());
    }

    @Override
    public Map<String, Object> parseValue() {
        Map<String, Object> value = new LinkedHashMap<>(map.size());
        for (Entry<String, Object> entry : map.entrySet()) {
            value.put(entry.getKey(), parseRawValue(entry.getValue()));
        }
        return value;
    }

    public boolean getBoolean(String name) {
        return map.getBoolean(name, false);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",\n\t");
        for (Entry<String, Object> entry : map.entrySet()) {
            Tag tag = Tag.fromRaw(entry.getValue());
            joiner.add("'" + entry.getKey() + "' : " + tag.toString().replace("\n", "\n\t"));
        }
        return "CompoundTag (" + map.size() + " entries) {\n\t" + joiner + "\n}";
    }

    @Override
    public String toSNBT() {
        StringJoiner joiner = new StringJoiner(",");
        for (Entry<String, Object> entry : map.entrySet()) {
            joiner.add("\"" + entry.getKey() + "\":" + snbtValue(entry.getValue(), 0, false));
        }
        return "{" + joiner + "}";
    }

    @Override
    public String toSNBT(int space) {
        String indent = " ".repeat(Math.max(0, space));
        StringJoiner joiner = new StringJoiner(",\n" + indent);
        for (Entry<String, Object> entry : map.entrySet()) {
            joiner.add("\"" + entry.getKey() + "\": " + snbtValue(entry.getValue(), space, true).replace("\n", "\n" + indent));
        }
        return "{\n" + indent + joiner + "\n}";
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public CompoundTag copy() {
        CompoundTag copy = new CompoundTag();
        for (Entry<String, Object> entry : map.entrySet()) {
            copy.put(entry.getKey(), Tag.fromRaw(entry.getValue()).copy());
        }
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        CompoundTag other = (CompoundTag) obj;
        return map.equals(other.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), map);
    }

    public boolean exist(String name) {
        return contains(name);
    }

    public NbtMap toNbtMap() {
        return map;
    }

    private static Map<String, Object> tagsToRawMap(Map<String, Tag> tags) {
        Map<String, Object> raw = new LinkedHashMap<>();
        for (Entry<String, Tag> entry : tags.entrySet()) {
            raw.put(entry.getKey(), Tag.toRaw(entry.getValue()));
        }
        return raw;
    }

    private static Object parseRawValue(Object value) {
        if (value instanceof NbtMap compound) {
            return new CompoundTag(compound).parseValue();
        }
        if (value instanceof List<?> list) {
            return ListTag.fromRaw(list).parseValue();
        }
        return value;
    }

    private static String snbtValue(Object value, int space, boolean pretty) {
        if (value instanceof NbtMap compound) {
            return pretty ? new CompoundTag(compound).toSNBT(space) : new CompoundTag(compound).toSNBT();
        }
        if (value instanceof List<?> list) {
            return pretty ? ListTag.fromRaw(list).toSNBT(space) : ListTag.fromRaw(list).toSNBT();
        }
        if (value instanceof String string) {
            return "\"" + string + "\"";
        }
        if (value instanceof Byte) {
            return value + "b";
        }
        if (value instanceof Short) {
            return value + "s";
        }
        if (value instanceof Long) {
            return value + "L";
        }
        if (value instanceof Float) {
            return value + "f";
        }
        if (value instanceof Double) {
            return value + "d";
        }
        if (value instanceof byte[] bytes) {
            StringJoiner joiner = new StringJoiner(",", "[B;", "]");
            for (byte b : bytes) {
                joiner.add(b + "b");
            }
            return joiner.toString();
        }
        if (value instanceof int[] ints) {
            StringJoiner joiner = new StringJoiner(",", "[I;", "]");
            for (int i : ints) {
                joiner.add(String.valueOf(i));
            }
            return joiner.toString();
        }
        if (value instanceof long[] longs) {
            StringJoiner joiner = new StringJoiner(",", "[L;", "]");
            for (long l : longs) {
                joiner.add(l + "L");
            }
            return joiner.toString();
        }
        return String.valueOf(value);
    }
}
