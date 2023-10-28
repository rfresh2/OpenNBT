package com.github.steveice10.opennbt;

import com.github.steveice10.opennbt.mini.MNBT;
import com.github.steveice10.opennbt.tag.builtin.Tag;

import java.io.*;

/**
 * Micro-NBT that hasn't been deserialized into POJOs yet
 * Can be used for quickly reading/writing NBT when the contents don't need to be accessed immediately
 * Also included is utility methods for deserializing MNBT to NBT for deferred parsing
 */
public class MNBTIO {

    public static MNBT read(DataInputStream in) throws IOException {
        return new MNBT(in);
    }

    public static void write(DataOutput out, MNBT nbt) throws IOException {
        if (nbt != null) {
            nbt.write(out);
        } else {
            out.writeByte(0);
        }
    }

    public static Tag read(MNBT mnbt) throws IOException {
        return NBTIO.readTag(new ByteArrayInputStream(mnbt.getData()));
    }

    // this is NOT efficient, try not to use this in performance critical areas
    public static MNBT write(Tag tag) throws IOException {
        if (tag == null) return null;
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(byteOut)) {
            NBTIO.writeTag((DataOutput) out, tag);
        }
        return new MNBT(byteOut.toByteArray());
    }
}
