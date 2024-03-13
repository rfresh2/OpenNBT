# Info

This project is a fork of OpenNBT and ViaNBT, for use with ZenithProxy.

There are a few patches made on top for deferring NBT serialization to/from byte arrays, what I call MNBT.

This is to avoid unnecessary GC pressure when we don't need to read or write NBT data contained in packets.
