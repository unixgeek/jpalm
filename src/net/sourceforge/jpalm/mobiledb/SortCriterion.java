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
package net.sourceforge.jpalm.mobiledb;

import net.sourceforge.jpalm.DataBlock;
import net.sourceforge.juint.Int8;

/**
 * The sort criterion in a MobileDB database. This contains information regarding how the database
 * was last sorted and does not enforce sorting.
 */
public class SortCriterion extends DataBlock {
    private static final int BYTES_RESERVED = 2;
    /**
     * The length of this <code>DataBlock</code> in <code>byte</code>s.
     */
    public static final int DATA_LENGH = 2 + BYTES_RESERVED;
    /**
     * This sort doesn't apply to any fields (Disables the sort).
     */
    public static final Int8 FIELD_NONE = new Int8(0xff);
    private Int8 fieldNumber;
    private boolean descending;
    private byte[] reserved;

    /**
     * Creates a new sort criterion. Defaults to an unused criterion.
     */
    public SortCriterion() {
        fieldNumber = FIELD_NONE;
        descending = false;
        reserved = new byte[BYTES_RESERVED];
    }

    /**
     * Creates a new criterion from the <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     */
    public SortCriterion(byte[] data) {
        deserialize(data);
    }

    /**
     * Gets the descending flag.
     * 
     * @return <code>true</code> if the sort is descending; <code>false</code> if the sort is
     *         ascending
     */
    public boolean isDescending() {
        return descending;
    }

    /**
     * Sets the descending flag. Default is <code>false</code>.
     * 
     * @param descending
     *            <code>true</code> if the sort is descending; <code>false</code> if the sort is
     *            ascending
     */
    public void setDescending(boolean descending) {
        this.descending = descending;
    }

    /**
     * Gets the field number to sort on.
     * 
     * @return the field number or {@link #FIELD_NONE}
     */
    public Int8 getFieldNumber() {
        return fieldNumber;
    }

    /**
     * Sets the field number to sort on.
     * 
     * @param fieldNumber
     *            the field number or {@link #FIELD_NONE}
     */
    public void setFieldNumber(Int8 fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    /**
     * Gets the reserved bytes. These bytes are reserved for future versions of MobileDB.
     * 
     * @return the reserved bytes
     */
    public byte[] getReserved() {
        return reserved;
    }

    /**
     * Sets the reserved bytes. These bytes are reserved for future versions of MobileDB.
     * 
     * @param reserved
     *            the reserved bytes
     */
    public void setReserved(byte[] reserved) {
        this.reserved = reserved;
    }

    @Override
    public void deserialize(byte[] data) {
        fieldNumber = new Int8(data[0]);

        if (data[1] == 1)
            descending = true;
        else
            descending = false;

        reserved = new byte[BYTES_RESERVED];
        for (int i = 0; i != BYTES_RESERVED; i++) {
            reserved[i] = data[1 + i];
        }
    }

    @Override
    public byte[] serialize() {
        byte[] bytes = new byte[DATA_LENGH];

        bytes[0] = fieldNumber.byteValue();

        if (descending)
            bytes[1] = 1;
        else
            bytes[1] = 0;

        System.arraycopy(reserved, 0, bytes, 2, reserved.length);

        return bytes;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SortCriterion))
            return false;
        return super.equals(object);
    }
}
