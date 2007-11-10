/*
 * $Id$
 * 
 * Copyright (c) 2006, Gunter Wambaugh
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the author nor the names of its contributors may be
 *      used to endorse or promote products derived from this software without 
 *      specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.sourceforge.jpalm.palmdb;

import net.sourceforge.jpalm.DataBlock;
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.juint.UInt32;
import net.sourceforge.juint.UInt8;

/**
 * A record header in a Palm database.
 * 
 * @see PalmDB
 * @see Record
 */
public class RecordHeader extends DataBlock {
    /**
     * The length of this <code>DataBlock</code> in <code>byte</code>s.<br>
     * {@value}
     */
    public static final int DATA_LENGTH = 8;
    /**
     * The bits for category attributes.
     */
    public static final UInt8 CATEGORY_ATTRIBUTES = new UInt8(0x0f);
    /**
     * The bits for record attributes.
     */
    public static final UInt8 RECORD_ATTRIBUTES = new UInt8((byte) 0xf0);
    /**
     * Record is private.
     */
    public static final UInt8 RECORD_SECRET = new UInt8(0x10);
    /**
     * Record is busy (locked).
     */
    public static final UInt8 RECORD_BUSY = new UInt8(0x20);
    /**
     * Record is dirty (modified since last HotSync).
     */
    public static final UInt8 RECORD_DIRTY = new UInt8(0x40);
    /**
     * Record is deleted.
     */
    public static final UInt8 RECORD_DELETE = new UInt8((byte) 0x80);
    protected UInt32 offset;
    protected UInt8 attributes;
    protected UInt32 uniqueId;

    /**
     * Creates a new record header from the <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     * @see #deserialize(byte[])
     */
    public RecordHeader(byte[] data) {
        deserialize(data);
    }

    /**
     * Creates a new record header.
     */
    public RecordHeader() {
        offset = new UInt32(0);
        attributes = new UInt8(0);
        uniqueId = new UInt32(0);
    }

    /**
     * Gets the attributes for the record this header represents. The attribute field contains
     * record and category information.
     * 
     * @return the attributes
     * @see #CATEGORY_ATTRIBUTES
     * @see #RECORD_ATTRIBUTES
     * @see #RECORD_BUSY
     * @see #RECORD_DELETE
     * @see #RECORD_DIRTY
     * @see #RECORD_SECRET
     */
    public UInt8 getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes for the record this header represents. The attribute field contains
     * record and category information.
     * 
     * @param attributes
     *            the attributes
     * @see #CATEGORY_ATTRIBUTES
     * @see #RECORD_ATTRIBUTES
     * @see #RECORD_BUSY
     * @see #RECORD_DELETE
     * @see #RECORD_DIRTY
     * @see #RECORD_SECRET
     */
    public void setAttributes(UInt8 attributes) {
        this.attributes = attributes;
    }

    /**
     * Adds an attribute.
     * 
     * @param attribute
     *            the attribute
     * @see #RECORD_BUSY
     * @see #RECORD_DELETE
     * @see #RECORD_DIRTY
     * @see #RECORD_SECRET
     */
    public void addAttribute(UInt8 attribute) {
        this.attributes = attributes.addBitmask(attribute);
    }

    /**
     * Removes an attribute.
     * 
     * @param attribute
     *            the attribute
     * @see #RECORD_BUSY
     * @see #RECORD_DELETE
     * @see #RECORD_DIRTY
     * @see #RECORD_SECRET
     */
    public void removeAttribute(UInt8 attribute) {
        this.attributes = attributes.removeBitmask(attribute);
    }

    /**
     * Gets the category identifier for the record this header represents. The category identifier
     * can be used to lookup the category label.
     * 
     * @return the category identifier
     * @see #CATEGORY_ATTRIBUTES
     * @see #getAttributes()
     * @see ApplicationInfo#getCategoryUniqueIds()
     * @see ApplicationInfo#getCategoryLabels()
     * @see Category
     */
    public UInt8 getCategory() {
        return getAttributes().and(CATEGORY_ATTRIBUTES);
    }

    /**
     * Sets the category identifier for the record this header represents.
     * 
     * @param category
     *            the category identifier
     * @see #CATEGORY_ATTRIBUTES
     * @see #setAttributes(UInt8)
     * @see ApplicationInfo#setCategoryUniqueIds(java.util.List)
     * @see ApplicationInfo#setCategoryLabels(java.util.List)
     * @see Category
     */
    public void setCategory(UInt8 category) {
        attributes = attributes.addBitmask(category);
    }

    /**
     * Gets the offset of the record this header represents.
     * 
     * @return the offset
     */
    public UInt32 getOffset() {
        return offset;
    }

    /**
     * Sets the offset of the record this header represents.
     * 
     * @param offset
     *            the offset
     */
    public void setOffset(UInt32 offset) {
        this.offset = offset;
    }

    /**
     * Gets the unique identifier of the record this header represents. What the identifier does is
     * unknown.
     * 
     * @return the unique identifier
     */
    public UInt32 getUniqueId() {
        return uniqueId;
    }

    /**
     * Sets the unique identifier of the record this header represents. Should always be set to
     * <code>0</code>.
     * 
     * @param uniqueId
     *            the unique identifier
     */
    public void setUniqueId(UInt32 uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public void deserialize(byte[] data) {
        offset = new UInt32(Utilities.subbyte(data, 0, 4));
        attributes = new UInt8(data[4]);
        byte[] uniqueBytes = new byte[4];
        uniqueBytes[0] = 0x00;
        uniqueBytes[1] = data[5];
        uniqueBytes[2] = data[6];
        uniqueBytes[3] = data[7];
        uniqueId = new UInt32(uniqueBytes);
    }

    @Override
    public byte[] serialize() {
        byte[] bytes = new byte[DATA_LENGTH];

        System.arraycopy(offset.toBigEndian(), 0, bytes, 0, 4);

        bytes[4] = attributes.byteValue();

        byte[] uniqueIdBytes = uniqueId.toBigEndian();
        bytes[5] = uniqueIdBytes[1];
        bytes[6] = uniqueIdBytes[2];
        bytes[7] = uniqueIdBytes[3];

        return bytes;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RecordHeader))
            return false;
        return super.equals(object);
    }
}
