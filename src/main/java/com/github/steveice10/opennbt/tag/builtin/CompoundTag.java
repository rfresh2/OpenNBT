package com.github.steveice10.opennbt.tag.builtin;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.SNBTIO.StringifiedNBTReader;
import com.github.steveice10.opennbt.SNBTIO.StringifiedNBTWriter;
import com.google.common.collect.Maps;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A compound tag containing other tags.
 */
public class CompoundTag extends Tag implements Iterable<Tag> {
    private Map<String, Tag> value;

    /**
     * Creates a tag with the specified name.
     *
     * @param name The name of the tag.
     */
    public CompoundTag(String name) {
        // todo: we could use fastutil maps here
        this(name, Maps.newHashMap());
    }

    /**
     * Creates a tag with the specified name.
     *
     * @param name  The name of the tag.
     * @param value The value of the tag.
     */
    public CompoundTag(String name, Map<String, Tag> value) {
        super(name);
        this.value = value;
    }

    @Override
    public Map<String, Tag> getValue() {
        return this.value;
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     */
    public void setValue(Map<String, Tag> value) {
        this.value = value;
    }

    /**
     * Checks whether the compound tag is empty.
     *
     * @return Whether the compound tag is empty.
     */
    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    /**
     * Checks whether the compound tag contains a tag with the specified name.
     *
     * @param tagName Name of the tag to check for.
     * @return Whether the compound tag contains a tag with the specified name.
     */
    public boolean contains(String tagName) {
        return this.value.containsKey(tagName);
    }

    /**
     * Gets the tag with the specified name.
     *
     * @param <T>     Type of tag to get.
     * @param tagName Name of the tag.
     * @return The tag with the specified name.
     */
    public <T extends Tag> T get(String tagName) {
        return (T) this.value.get(tagName);
    }

    /**
     * Puts the tag into this compound tag.
     *
     * @param <T> Type of tag to put.
     * @param tag Tag to put into this compound tag.
     * @return The previous tag associated with its name, or null if there wasn't one.
     */
    public <T extends Tag> T put(T tag) {
        return (T) this.value.put(tag.getName(), tag);
    }

    /**
     * Removes a tag from this compound tag.
     *
     * @param <T>     Type of tag to remove.
     * @param tagName Name of the tag to remove.
     * @return The removed tag.
     */
    public <T extends Tag> T remove(String tagName) {
        return (T) this.value.remove(tagName);
    }

    /**
     * Gets a set of keys in this compound tag.
     *
     * @return The compound tag's key set.
     */
    public Set<String> keySet() {
        return this.value.keySet();
    }

    /**
     * Gets a collection of tags in this compound tag.
     *
     * @return This compound tag's tags.
     */
    public Collection<Tag> values() {
        return this.value.values();
    }

    /**
     * Gets the number of tags in this compound tag.
     *
     * @return This compound tag's size.
     */
    public int size() {
        return this.value.size();
    }

    /**
     * Clears all tags from this compound tag.
     */
    public void clear() {
        this.value.clear();
    }

    @Override
    public Iterator<Tag> iterator() {
        return this.values().iterator();
    }

    @Override
    public void read(DataInput in) throws IOException {
        try {
            Tag tag;
            while ((tag = NBTIO.readTag(in)) != null) {
                this.put(tag);
            }
        } catch (EOFException e) {
            throw new IOException("Closing EndTag was not found!");
        }
        // todo: the original code doesn't write the entire tag to the map if there is no closing tag
        //  this version is optimistic and assumes that the entire tag will be read and writes it all the map incrementally
    }

    @Override
    public void write(DataOutput out) throws IOException {
        for(Tag tag : this.value.values()) {
            NBTIO.writeTag(out, tag);
        }

        out.writeByte(0);
    }

    @Override
    public void destringify(StringifiedNBTReader in) throws IOException {
        in.readSkipWhitespace();
        while(true) {
            String tagName = "";
            if((tagName += in.readSkipWhitespace()).equals("\"")) {
                tagName = in.readUntil(false, '"');
                in.read();
            }
            tagName += in.readUntil(false, ':');
            in.read();

            put(in.readNextTag(tagName));

            char endChar = in.readSkipWhitespace();
            if(endChar == ',')
                continue;
            if(endChar == '}')
                break;
        }
    }

    @Override
    public void stringify(StringifiedNBTWriter out, boolean linebreak, int depth) throws IOException {
        out.append('{');

        boolean first = true;
        for(Tag t: value.values()) {
            if(first) {
                first = false;
            } else {
                out.append(',');
                if(!linebreak) {
                    out.append(' ');
                }
            }
            out.writeTag(t, linebreak, depth + 1);
        }

        if(linebreak) {
            out.append('\n');
            out.indent(depth);
        }
        out.append('}');
    }

    @Override
    public CompoundTag clone() {
        Map<String, Tag> newMap = Maps.newHashMap();
        for(Entry<String, Tag> entry : this.value.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue().clone());
        }

        return new CompoundTag(this.getName(), newMap);
    }
}
