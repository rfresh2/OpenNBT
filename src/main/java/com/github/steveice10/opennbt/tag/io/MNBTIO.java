package com.github.steveice10.opennbt.tag.io;

import com.github.steveice10.opennbt.mini.MNBT;
import com.github.steveice10.opennbt.mini.MNBTReader;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import com.github.steveice10.opennbt.tag.limiter.TagLimiter;

import java.io.*;

/**
 * Micro-NBT that hasn't been deserialized into POJOs yet
 * Can be used for quickly reading/writing NBT when the contents don't need to be accessed immediately
 * Also included is utility methods for deserializing MNBT to NBT for deferred parsing
 */
public class MNBTIO {

    public static MNBT read(DataInputStream in, boolean named) throws IOException {
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(byteOutStream)) {
            boolean b = named ? MNBTReader.readTag(in, out) : MNBTReader.readNameless(in, out);
            var data = byteOutStream.toByteArray();
            var empty = !b;
            return new MNBT(data, empty, named);
        }
    }

    public static Tag read(MNBT mnbt) throws IOException {
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(mnbt.getData()))) {
            return NBTIO.readTag(in, TagLimiter.noop(), mnbt.isNamed(), null);
        }
    }

    public static void write(DataOutput out, MNBT nbt) throws IOException {
        if (nbt != null) {
            out.write(nbt.getData());
        } else {
            out.writeByte(0);
        }
    }

    // this is NOT efficient, try not to use this in performance critical areas
    public static MNBT write(Tag tag, boolean named) throws IOException {
        if (tag == null) return null;
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(byteOut)) {
            NBTIO.writeTag(out, tag, named);
        }
        return new MNBT(byteOut.toByteArray(), named);
    }
}
