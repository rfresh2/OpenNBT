package com.github.steveice10.opennbt.mini;

import java.io.*;

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

}
