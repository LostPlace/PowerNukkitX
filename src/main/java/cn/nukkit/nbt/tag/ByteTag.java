package cn.nukkit.nbt.tag;

public class ByteTag extends NumberTag<Byte> {
    public byte data;

    public ByteTag() {
    }

    public ByteTag(int data) {
        this.data = (byte) data;
    }

    @Override
    public Byte getData() {
        return data;
    }

    @Override
    public void setData(Byte data) {
        this.data = data == null ? 0 : data;
    }

    @Override
    public byte getId() {
        return TAG_Byte;
    }

    @Override
    public Byte parseValue() {
        return data;
    }

    @Override
    public String toString() {
        String hex = Integer.toHexString(Byte.toUnsignedInt(data));
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return "ByteTag " + " (data: 0x" + hex + ")";
    }

    @Override
    public String toSNBT() {
        return data + "b";
    }

    @Override
    public String toSNBT(int space) {
        return data + "b";
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            ByteTag byteTag = (ByteTag) obj;
            return data == byteTag.data;
        }
        return false;
    }

    @Override
    public Tag copy() {
        return new ByteTag(data);
    }
}
