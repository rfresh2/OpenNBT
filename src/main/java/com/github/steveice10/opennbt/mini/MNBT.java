package com.github.steveice10.opennbt.mini;

import lombok.Data;
import lombok.ToString;

import java.io.*;

@Data
@ToString(exclude = "data")
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

    public MNBT(DataInputStream in, boolean any) throws IOException {
        this.read(in, any);
    }

    public void read(DataInputStream in, final boolean any) throws IOException {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(byteOutStream)) {
            boolean b = any ? MNBTReader.readAnyTag(in, out) : MNBTReader.readTag(in, out);
            // todo: should be possible to avoid copying the array here
            //  will need to provide our own impl of the byte array output stream
            this.data = byteOutStream.toByteArray();
            this.empty = !b;
        }
    }

    public void write(DataOutput out) throws IOException {
        out.write(this.data);
    }
}
