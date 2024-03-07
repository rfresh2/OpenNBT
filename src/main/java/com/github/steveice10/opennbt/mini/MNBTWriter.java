package com.github.steveice10.opennbt.mini;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class MNBTWriter implements AutoCloseable {

    private ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
    private DataOutputStream out = new DataOutputStream(byteOutStream);
    private boolean named = false;

    @SneakyThrows
    public void writeEndTag() {
        out.writeByte(0);
    }

    @SneakyThrows
    public void writeByteTag(final String name, final byte b) {
        out.writeByte(1);
        out.writeUTF(name);
        out.writeByte(b);
    }

    @SneakyThrows
    public void writeByteTag(final byte b) {
        out.writeByte(b);
    }

    @SneakyThrows
    public void writeShortTag(final String name, final short s) {
        out.writeByte(2);
        out.writeUTF(name);
        out.writeShort(s);
    }

    @SneakyThrows
    public void writeShortTag(final short s) {
        out.writeShort(s);
    }

    @SneakyThrows
    public void writeIntTag(final String name, final int i) {
        out.writeByte(3);
        out.writeUTF(name);
        out.writeInt(i);
    }

    @SneakyThrows
    public void writeIntTag(final int i) {
        out.writeInt(i);
    }

    @SneakyThrows
    public void writeLongTag(final String name, final long l) {
        out.writeByte(4);
        out.writeUTF(name);
        out.writeLong(l);
    }

    @SneakyThrows
    public void writeLongTag(final long l) {
        out.writeLong(l);
    }

    @SneakyThrows
    public void writeFloatTag(final String name, final float f) {
        out.writeByte(5);
        out.writeUTF(name);
        out.writeFloat(f);
    }

    @SneakyThrows
    public void writeFloatTag(final float f) {
        out.writeFloat(f);
    }

    @SneakyThrows
    public void writeDoubleTag(final String name, final double d) {
        out.writeByte(6);
        out.writeUTF(name);
        out.writeDouble(d);
    }

    @SneakyThrows
    public void writeDoubleTag(final double d) {
        out.writeDouble(d);
    }

    @SneakyThrows
    public void writeByteArrayTag(final String name, final byte[] b) {
        out.writeByte(7);
        out.writeUTF(name);
        out.writeInt(b.length);
        for (int i = 0; i < b.length; i++) {
            out.writeByte(b[i]);
        }
    }

    @SneakyThrows
    public void writeByteArrayTag(final byte[] b) {
        out.writeInt(b.length);
        for (int i = 0; i < b.length; i++) {
            out.writeByte(b[i]);
        }
    }

    @SneakyThrows
    public void writeStringTag(final String name, final String s) {
        out.writeByte(8);
        out.writeUTF(name);
        out.writeUTF(s);
    }

    @SneakyThrows
    public void writeStringTag(final String s) {
        out.writeUTF(s);
    }

    @SneakyThrows
    public void writeNamelessStringTag(final String s) {
        out.writeByte(8);
        out.writeUTF(s);
    }

    @SneakyThrows
    public void writeListTag(final String name, int type, int len) {
        out.writeByte(9);
        out.writeUTF(name);
        out.writeByte(type);
        out.writeInt(len);
    }

    @SneakyThrows
    public void writeCompoundTag(String name) {
        out.writeByte(10);
        out.writeUTF(name);
    }

    @SneakyThrows
    public void writeCompoundTag() {
        out.writeByte(10);
    }

    public void writeStartTag() {
        this.named = false;
        writeCompoundTag();
    }

    public void writeStartTag(String name) {
        this.named = true;
        writeCompoundTag(name);
    }

    @SneakyThrows
    public void writeIntArrayTag(final String name, final int[] i) {
        out.writeByte(11);
        out.writeUTF(name);
        out.writeInt(i.length);
        for (int j = 0; j < i.length; j++) {
            out.writeInt(i[j]);
        }
    }

    @SneakyThrows
    public void writeIntArrayTag(final int[] i) {
        out.writeInt(i.length);
        for (int j = 0; j < i.length; j++) {
            out.writeInt(i[j]);
        }
    }

    @SneakyThrows
    public void writeLongArrayTag(final String name, final long[] l) {
        out.writeByte(12);
        out.writeUTF(name);
        out.writeInt(l.length);
        for (int i = 0; i < l.length; i++) {
            out.writeLong(l[i]);
        }
    }

    @SneakyThrows
    public void writeLongArrayTag(final long[] l) {
        out.writeInt(l.length);
        for (int i = 0; i < l.length; i++) {
            out.writeLong(l[i]);
        }
    }

    @Override
    public void close() throws Exception {
        out.close();
    }

    public MNBT toMNBT() {
        return new MNBT(byteOutStream.toByteArray(), named);
    }
}
