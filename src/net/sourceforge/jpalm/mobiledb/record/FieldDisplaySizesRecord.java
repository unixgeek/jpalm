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

import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordHeader;
import net.sourceforge.juint.UInt8;

/**
 * The field display sizes record in a MobileDB database.
 * <p>
 * There should be exactly one of these records in a MobileDB database and should always have 20
 * fields.
 * <p>
 * When this class is serialized, if the field count is not 20, fields will be added or removed
 * until the count is 20. If fields need to be added, they will have a value of <code>80</code>.
 */
public class FieldDisplaySizesRecord extends AbstractRecord {
    /**
     * The category identifier for this record.
     */
    public static final UInt8 CATEGORY_ID = new UInt8(0x06);

    /**
     * Creates a new field display size record with no display sizes.
     */
    public FieldDisplaySizesRecord() {
        super();
        header = new RecordHeader();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new field display size record from a <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     * @see #deserialize(byte[])
     */
    public FieldDisplaySizesRecord(byte[] data) {
        super(data);
        header = new RecordHeader();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new field display size record from a <code>Record</code>.
     * 
     * @param record
     *            the record
     */
    public FieldDisplaySizesRecord(Record record) {
        super(record);
    }

    /**
     * Creates a new field display size record from a list of field display sizes.
     * 
     * @param fieldDisplaySizes
     *            the field lengths
     * @see #setFieldDisplaySizes(List)
     */
    public FieldDisplaySizesRecord(List<Integer> fieldDisplaySizes) {
        this();
        setFieldDisplaySizes(fieldDisplaySizes);
    }

    /**
     * Sets the field display sizes.
     * 
     * @param fieldDisplaySizes
     *            the field display sizes
     */
    public void setFieldDisplaySizes(List<Integer> fieldDisplaySizes) {
        ArrayList<String> sizesAsStrings = new ArrayList<String>();
        for (Integer size : fieldDisplaySizes) {
            if (size != null) {
                sizesAsStrings.add(size.toString());
            }
            else
                sizesAsStrings.add("0");
        }
        setInternalFields(sizesAsStrings);
    }

    /**
     * Gets the field display sizes.
     * 
     * @return the field display sizes
     */
    public List<Integer> getFieldDisplaySizes() {
        ArrayList<Integer> sizesAsIntegers = new ArrayList<Integer>();
        for (String size : getInternalFields()) {
            try {
                sizesAsIntegers.add(Integer.valueOf(size));
            }
            catch (NumberFormatException e) {
                sizesAsIntegers.add(0);
            }
        }
        return sizesAsIntegers;
    }

    @Override
    public byte[] serialize() {
        setInternalFields(trimOrFillList(20, "80"));
        return super.serialize();
    }
}
