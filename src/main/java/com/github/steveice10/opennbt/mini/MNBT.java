package com.github.steveice10.opennbt.mini;

import com.google.common.base.Objects;

import java.io.*;
import java.util.Arrays;

public class MNBT {
    private byte[] data;
    private boolean empty = true;

    public MNBT() {
        this.data = new byte[0];
    }

    public MNBT(byte[] data) {
        this.data = data;
        this.empty = data.length == 0;
    }

    public MNBT(DataInputStream in) throws IOException {
        this.read(in);
    }

    public void read(DataInputStream in) throws IOException {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(byteOutStream)) {
            boolean b = MNBTReader.readTag(in, out);
            // todo: should be possible to avoid copying the array here
            //  will need to provide our own impl of the byte array output stream
            this.data = byteOutStream.toByteArray();
            this.empty = !b;
        }
    }

    public void write(DataOutput out) throws IOException {
        out.write(this.data);
    }

    public byte[] getData() {
        return this.data;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MNBT mnbt = (MNBT) o;
        return isEmpty() == mnbt.isEmpty() && Arrays.equals(getData(), mnbt.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getData(), isEmpty());
    }
}
