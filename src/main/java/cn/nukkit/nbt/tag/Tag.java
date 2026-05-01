package cn.nukkit.nbt.tag;

import org.cloudburstmc.nbt.NbtType;

import java.util.List;
import java.util.Objects;

public abstract class Tag {
    public static final byte TAG_End = 0;
    public static final byte TAG_Byte = 1;
    public static final byte TAG_Short = 2;
    public static final byte TAG_Int = 3;
    public static final byte TAG_Long = 4;
    public static final byte TAG_Float = 5;
    public static final byte TAG_Double = 6;
    public static final byte TAG_Byte_Array = 7;
    public static final byte TAG_String = 8;
    public static final byte TAG_List = 9;
    public static final byte TAG_Compound = 10;
    public static final byte TAG_Int_Array = 11;

    @Override
    public abstract String toString();

    public abstract String toSNBT();

    public abstract String toSNBT(int space);

    public abstract byte getId();

    protected Tag() {
    }


    public abstract Tag copy();

    public abstract <T> T parseValue();

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tag o)) {
            return false;
        }
        return getId() == o.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public static Tag newTag(byte type) {
        return switch (type) {
            case TAG_End -> new EndTag();
            case TAG_Byte -> new ByteTag();
            case TAG_Short -> new ShortTag();
            case TAG_Int -> new IntTag();
            case TAG_Long -> new LongTag();
            case TAG_Float -> new FloatTag();
            case TAG_Double -> new DoubleTag();
            case TAG_Byte_Array -> new ByteArrayTag();
            case TAG_Int_Array -> new IntArrayTag();
            case TAG_String -> new StringTag();
            case TAG_List -> new ListTag<>();
            case TAG_Compound -> new CompoundTag();
            default -> new EndTag();
        };
    }

    public static String getTagName(byte type) {
        return switch (type) {
            case TAG_End -> "TAG_End";
            case TAG_Byte -> "TAG_Byte";
            case TAG_Short -> "TAG_Short";
            case TAG_Int -> "TAG_Int";
            case TAG_Long -> "TAG_Long";
            case TAG_Float -> "TAG_Float";
            case TAG_Double -> "TAG_Double";
            case TAG_Byte_Array -> "TAG_Byte_Array";
            case TAG_Int_Array -> "TAG_Int_Array";
            case TAG_String -> "TAG_String";
            case TAG_List -> "TAG_List";
            case TAG_Compound -> "TAG_Compound";
            default -> "UNKNOWN";
        };
    }

    public static int getTagType(Class<?> type) {
        if (ListTag.class.isAssignableFrom(type)) {
            return TAG_List;
        } else if (CompoundTag.class.isAssignableFrom(type)) {
            return TAG_Compound;
        } else if (EndTag.class.isAssignableFrom(type)) {
            return TAG_End;
        } else if (ByteTag.class.isAssignableFrom(type)) {
            return TAG_Byte;
        } else if (ShortTag.class.isAssignableFrom(type)) {
            return TAG_Short;
        } else if (IntTag.class.isAssignableFrom(type)) {
            return TAG_Int;
        } else if (FloatTag.class.isAssignableFrom(type)) {
            return TAG_Float;
        } else if (LongTag.class.isAssignableFrom(type)) {
            return TAG_Long;
        } else if (DoubleTag.class.isAssignableFrom(type)) {
            return TAG_Double;
        } else if (ByteArrayTag.class.isAssignableFrom(type)) {
            return TAG_Byte_Array;
        } else if (IntArrayTag.class.isAssignableFrom(type)) {
            return TAG_Int_Array;
        } else if (StringTag.class.isAssignableFrom(type)) {
            return TAG_String;
        } else {
            throw new IllegalArgumentException("Unknown tag type " + type.getSimpleName());
        }
    }

    public static byte getTagType(Object value) {
        if (value == null) {
            return TAG_End;
        }
        if (value instanceof Tag tag) {
            return tag.getId();
        }
        if (value instanceof Byte) {
            return TAG_Byte;
        }
        if (value instanceof Short) {
            return TAG_Short;
        }
        if (value instanceof Integer) {
            return TAG_Int;
        }
        if (value instanceof Long) {
            return TAG_Long;
        }
        if (value instanceof Float) {
            return TAG_Float;
        }
        if (value instanceof Double) {
            return TAG_Double;
        }
        if (value instanceof byte[]) {
            return TAG_Byte_Array;
        }
        if (value instanceof int[]) {
            return TAG_Int_Array;
        }
        if (value instanceof String) {
            return TAG_String;
        }
        if (value instanceof List<?>) {
            return TAG_List;
        }
        if (value instanceof org.cloudburstmc.nbt.NbtMap) {
            return TAG_Compound;
        }
        throw new IllegalArgumentException("Unknown raw NBT type: " + value.getClass().getName());
    }

    public static Tag fromRaw(Object value) {
        if (value == null) {
            return new EndTag();
        }
        if (value instanceof Tag tag) {
            return tag;
        }
        if (value instanceof Byte b) {
            return new ByteTag(b);
        }
        if (value instanceof Short s) {
            return new ShortTag(s);
        }
        if (value instanceof Integer i) {
            return new IntTag(i);
        }
        if (value instanceof Long l) {
            return new LongTag(l);
        }
        if (value instanceof Float f) {
            return new FloatTag(f);
        }
        if (value instanceof Double d) {
            return new DoubleTag(d);
        }
        if (value instanceof byte[] bytes) {
            return new ByteArrayTag(bytes);
        }
        if (value instanceof int[] ints) {
            return new IntArrayTag(ints);
        }
        if (value instanceof String string) {
            return new StringTag(string);
        }
        if (value instanceof List<?> list) {
            return ListTag.fromRaw(list);
        }
        if (value instanceof org.cloudburstmc.nbt.NbtMap map) {
            return new CompoundTag(map);
        }
        throw new IllegalArgumentException("Unsupported raw NBT value type: " + value.getClass().getName());
    }

    public static Object toRaw(Tag tag) {
        if (tag == null) {
            return null;
        }
        if (tag instanceof CompoundTag compoundTag) {
            return compoundTag.toNbtMap();
        }
        if (tag instanceof ListTag<?> listTag) {
            return listTag.toNbtList();
        }
        return tag.parseValue();
    }

    public static NbtType<?> toNbtType(byte type) {
        return NbtType.byId(type);
    }
}
