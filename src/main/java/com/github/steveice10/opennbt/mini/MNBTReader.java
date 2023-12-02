package com.github.steveice10.opennbt.mini;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Simple reader for MNBT that writes the same input to an output stream
 * The reason we need this whole thing is to find where the NBT and capture the data
 */
public class MNBTReader {
    private static final Int2ObjectOpenHashMap<DataConsumer> tagIdToHandler = new Int2ObjectOpenHashMap<>();

    static {
        tagIdToHandler.put(0, MNBTReader::readEndTag);
        tagIdToHandler.put(1, MNBTReader::readByteTag);
        tagIdToHandler.put(2, MNBTReader::readShortTag);
        tagIdToHandler.put(3, MNBTReader::readIntTag);
        tagIdToHandler.put(4, MNBTReader::readLongTag);
        tagIdToHandler.put(5, MNBTReader::readFloatTag);
        tagIdToHandler.put(6, MNBTReader::readDoubleTag);
        tagIdToHandler.put(7, MNBTReader::readByteArrayTag);
        tagIdToHandler.put(8, MNBTReader::readStringTag);
        tagIdToHandler.put(9, MNBTReader::readListTag);
        tagIdToHandler.put(10, MNBTReader::readCompoundTag);
        tagIdToHandler.put(11, MNBTReader::readIntArrayTag);
        tagIdToHandler.put(12, MNBTReader::readLongArrayTag);
    }

    interface DataConsumer {
        void accept(DataInputStream in, DataOutputStream out) throws IOException;
    }

    public static void readByteTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        out.writeByte(in.readByte());
    }

    public static void readShortTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        out.writeShort(in.readShort());
    }

    public static void readIntTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        out.writeInt(in.readInt());
    }

    public static void readLongTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        out.writeLong(in.readLong());
    }

    public static void readFloatTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        out.writeFloat(in.readFloat());
    }

    public static void readDoubleTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        out.writeDouble(in.readDouble());
    }

    public static void readStringTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        readUTF(in, out);
    }

    public static void readListTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        int id = in.readUnsignedByte();
        out.writeByte(id);

        int count = in.readInt();
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            tagIdToHandler.get(id).accept(in, out);
        }
    }

    public static void readLongArrayTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        int count = in.readInt();
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            out.writeLong(in.readLong());
        }
    }

    public static void readIntArrayTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        int count = in.readInt();
        out.writeInt(count);
        for (int i = 0; i < count; i++) {
            out.writeInt(in.readInt());
        }
    }

    public static void readEndTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        out.writeByte(0);
    }

    public static boolean readTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        final int id = in.readUnsignedByte();
        out.writeByte(id);
        if (id == 0) return false;
        readStringTag(in, out);
        tagIdToHandler.get(id).accept(in, out);
        return true;
    }

    public static boolean readAnyTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        final int id = in.readUnsignedByte();
        out.writeByte(id);
        if (id == 0) return false;
        // any = no tag name ?
        tagIdToHandler.get(id).accept(in, out);
        return true;
    }

    public static void readCompoundTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        while (readTag(in, out)) { }
    }

    public static void readByteArrayTag(final DataInputStream in, final DataOutputStream out) throws IOException {
        final int length = in.readInt();
        out.writeInt(length);
        out.write(in.readNBytes(length));
    }

    public static void readUTF(DataInputStream in, DataOutputStream out) throws IOException {
        int len = in.readUnsignedShort();
        out.writeShort(len);

        out.write(in.readNBytes(len));
    }
}
