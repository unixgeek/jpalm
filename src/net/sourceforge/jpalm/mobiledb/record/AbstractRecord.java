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
package net.sourceforge.jpalm.mobiledb.record;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jpalm.DataBlock;
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordHeader;
import net.sourceforge.juint.UInt8;

/**
 * Base class for MobileDB database records.
 */
public abstract class AbstractRecord extends DataBlock implements Record {
    /**
     * The record header.
     */
    protected RecordHeader header;
    /**
     * The maximum length a field can be. {@value}
     */
    protected static final int MAX_FIELD_WIDTH = 1024;
    /**
     * The data header in a record.
     */
    protected static final byte[] HEADER = { (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01,
            (byte) 0xff, 0x00 };
    /**
     * The field terminator in a record.
     */
    protected static final byte TERIMINATOR = 0x00;
    /**
     * The data trailer in a record.
     */
    protected static final byte TRAILER = (byte) 0xff;
    /**
     * The data fields as byte arrays.
     */
    protected byte[][] fields;

    AbstractRecord() {

    }

    AbstractRecord(Record record) {
        setHeader(record.getHeader());
        deserialize(record.serialize());
    }

    AbstractRecord(byte[] data) {
        deserialize(data);
    }

    public RecordHeader getHeader() {
        return header;
    }

    public void setHeader(RecordHeader header) {
        this.header = header;
    }

    @Override
    public void deserialize(byte[] data) {
        ArrayList<byte[]> fieldsList = new ArrayList<byte[]>(20);

        // Skip the header.
        int position = HEADER.length;

        // There is an undocumented extra NULL after the header.
        position++;

        // Don't look at last byte as it's the TRAILER.
        while (position < (data.length - 1)) {
            // First byte is the field number.
            UInt8 fieldNumber = new UInt8(data[position]);
            position++;

            // Every field is terminated.
            int terminatorIndex = Utilities.indexOf(data, position, TERIMINATOR);
            byte[] field = Utilities.subbyte(data, position, terminatorIndex - position);
            fieldsList.add(fieldNumber.intValue(), field);

            // Increment for field length and 1 for TERMINATOR.
            position += field.length + 1;
        }

        fields = new byte[fieldsList.size()][];
        fields = (byte[][]) fieldsList.toArray(fields);
    }

    @Override
    public byte[] serialize() {
        // Calculate the size of the needed byte array.
        int size = 0;
        if (fields != null) {
            for (int i = 0; i != fields.length; i++) {
                size += fields[i].length;
                // Add 1 for the TERMINATOR.
                size++;
            }
            // Each record needs a byte to indicate the record number.
            size += fields.length;
        }

        // Add HEADER and TRAILER.
        size += HEADER.length + 1;

        // Add 1 for undocumented NULL.
        size++;

        byte[] bytes = new byte[size];

        System.arraycopy(HEADER, 0, bytes, 0, HEADER.length);

        int position = HEADER.length;

        // Add undocumented NULL.
        bytes[position] = 0x00;
        position++;

        if (fields != null) {
            for (int i = 0; i != fields.length; i++) {
                bytes[position] = (byte) i;
                position++;
                System.arraycopy(fields[i], 0, bytes, position, fields[i].length);
                position += fields[i].length;
                bytes[position] = TERIMINATOR;
                position++;
            }
        }
        bytes[position] = TRAILER;

        return bytes;
    }

    /**
     * Gets the internal fields as a list of strings.
     * 
     * @return list of fields
     */
    protected List<String> getInternalFields() {
        int size = (fields != null) ? fields.length : 0;
        ArrayList<String> fieldsList = new ArrayList<String>(size);
        for (int i = 0; i != size; i++) {
            fieldsList.add(new String(fields[i]));
        }

        return fieldsList;
    }

    /**
     * Sets the internal fields from a list of strings.
     * 
     * @param fieldsList
     *            the list of strings
     */
    protected void setInternalFields(List<String> fieldsList) {
        fields = new byte[fieldsList.size()][];
        for (int i = 0; i != fieldsList.size(); i++) {
            fields[i] = trim(fieldsList.get(i)).getBytes();
        }
    }

    /**
     * Trim or fill a list to the specified size.
     * <p>
     * If a list needs to be filled, it will have the <code>defaultValue</code> appended to it. If
     * a list needs to be trimmed, it will be truncated at <code>size</code>.
     * 
     * @param size
     *            the size the list should be
     * @param defaultValue
     *            the value to use for filling
     * @return a list of <code>size</code>
     */
    protected List<String> trimOrFillList(int size, String defaultValue) {
        List<String> fieldsList = getInternalFields();
        List<String> newList = new ArrayList<String>(size);

        if ((fields != null) && (fields.length == size))
            return fieldsList;

        if (fieldsList.size() > size) {
            newList = fieldsList.subList(0, size);
        }
        else {
            newList.addAll(fieldsList);
            for (int i = 0; i != (size - fieldsList.size()); i++) {
                newList.add(defaultValue);
            }
        }
        return newList;
    }

    private String trim(String string) {
        // Allow nulls, but don't use them internally.
        if (string == null)
            string = "";
        int length = (string.length() < MAX_FIELD_WIDTH) ? string.length() : MAX_FIELD_WIDTH;
        return string.substring(0, length);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AbstractRecord))
            return false;
        return super.equals(object);
    }
}
