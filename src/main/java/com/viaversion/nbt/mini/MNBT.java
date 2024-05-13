package com.viaversion.nbt.mini;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "data")
public class MNBT {
    private byte[] data;
    private boolean empty;
    private final boolean named;

    public MNBT() {
        this(new byte[0], true);
    }

    public MNBT(byte[] data) {
        this(data, true);
    }

    public MNBT(byte[] data, boolean named) {
        this.data = data;
        this.empty = data.length == 0;
        this.named = named;
    }

    public MNBT(byte[] data, boolean empty, boolean named) {
        this.data = data;
        this.empty = empty;
        this.named = named;
    }
}
