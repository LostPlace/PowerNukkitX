package cn.nukkit.nbt.tag;

import org.cloudburstmc.nbt.NbtList;
import org.cloudburstmc.nbt.NbtType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ListTag<T extends Tag> extends Tag {
    private List<T> list = new ArrayList<>();
    public byte type = TAG_End;
    private NbtType<?> nbtType = NbtType.END;

    public ListTag() {
    }

    public ListTag(NbtType<?> type) {
        setType(type);
    }

    public ListTag(Collection<T> tags) {
        setAll(tags);
    }

    public ListTag(int type, Collection<T> tags) {
        this.type = (byte) type;
        this.nbtType = Tag.toNbtType(this.type);
        this.list.addAll(tags);
    }

    public static ListTag<Tag> fromRaw(List<?> values) {
        ListTag<Tag> result;
        if (values instanceof NbtList<?> nbtList) {
            result = new ListTag<>(nbtList.getType());
        } else {
            result = new ListTag<>();
            if (!values.isEmpty()) {
                result.setType(Tag.toNbtType(Tag.getTagType(values.get(0))));
            }
        }
        for (Object value : values) {
            result.add(Tag.fromRaw(value));
        }
        return result;
    }

    @Override
    public byte getId() {
        return TAG_List;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",\n\t");
        list.forEach(tag -> joiner.add(tag.toString().replace("\n", "\n\t")));
        return "ListTag (" + list.size() + " entries of type " + Tag.getTagName(type) + ") {\n\t" + joiner + "\n}";
    }

    @Override
    public String toSNBT() {
        return "[" + list.stream().map(Tag::toSNBT).collect(Collectors.joining(",")) + "]";
    }

    @Override
    public String toSNBT(int space) {
        String indent = " ".repeat(Math.max(0, space));
        if (list.isEmpty()) {
            return "[]";
        }
        if (list.get(0) instanceof StringTag || list.get(0) instanceof CompoundTag || list.get(0) instanceof ListTag<?>) {
            StringJoiner joiner = new StringJoiner(",\n" + indent);
            list.forEach(tag -> joiner.add(tag.toSNBT(space).replace("\n", "\n" + indent)));
            return "[\n" + indent + joiner + "\n]";
        }
        StringJoiner joiner = new StringJoiner(", ");
        list.forEach(tag -> joiner.add(tag.toSNBT(space)));
        return "[" + joiner + "]";
    }

    public ListTag<T> add(T tag) {
        updateType(tag);
        list.add(tag);
        return this;
    }

    public ListTag<T> add(int index, T tag) {
        updateType(tag);
        if (index >= list.size()) {
            list.add(tag);
        } else {
            list.set(index, tag);
        }
        return this;
    }

    @Override
    public List<Object> parseValue() {
        List<Object> value = new ArrayList<>(list.size());
        for (T tag : list) {
            value.add(Tag.toRaw(tag));
        }
        return value;
    }

    public T get(int index) {
        return list.get(index);
    }

    public List<T> getAll() {
        return new ArrayList<>(list);
    }

    public void setAll(Collection<T> tags) {
        list = new ArrayList<>();
        type = TAG_End;
        nbtType = NbtType.END;
        for (T tag : tags) {
            add(tag);
        }
    }

    public void remove(T tag) {
        list.remove(tag);
        refreshTypeIfEmpty();
    }

    public void remove(int index) {
        list.remove(index);
        refreshTypeIfEmpty();
    }

    public void removeAll(Collection<T> tags) {
        list.removeAll(tags);
        refreshTypeIfEmpty();
    }

    public int size() {
        return list.size();
    }

    public NbtType<?> getElementType() {
        return nbtType;
    }

    public NbtList<?> toNbtList() {
        return new NbtList(nbtType, parseValue());
    }

    @Override
    public Tag copy() {
        ListTag<T> result = new ListTag<>(nbtType);
        for (T tag : list) {
            @SuppressWarnings("unchecked")
            T copy = (T) tag.copy();
            result.list.add(copy);
        }
        result.type = type;
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        ListTag other = (ListTag) obj;
        return type == other.type && list.equals(other.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, list);
    }

    private void updateType(Tag tag) {
        byte newType = tag.getId();
        if (type != TAG_End && type != newType) {
            throw new IllegalArgumentException("ListTag type mismatch: expected " + Tag.getTagName(type) + " but got " + Tag.getTagName(newType));
        }
        setType(Tag.toNbtType(newType));
    }

    private void setType(NbtType<?> type) {
        this.nbtType = type == null ? NbtType.END : type;
        this.type = (byte) this.nbtType.getId();
    }

    private void refreshTypeIfEmpty() {
        if (list.isEmpty()) {
            type = TAG_End;
            nbtType = NbtType.END;
        }
    }
}
