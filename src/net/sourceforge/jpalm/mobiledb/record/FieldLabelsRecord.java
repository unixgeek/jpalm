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

import java.util.List;

import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordHeader;
import net.sourceforge.juint.UInt8;

/**
 * The field labels record in a MobileDB database.
 * <p>
 * There should be exactly one of these records in a MobileDB database. The number of fields in this
 * record determines the number of fields in the database.
 */
public class FieldLabelsRecord extends AbstractRecord {
    /**
     * The maximum length of a field label.<br>
     * {@value}
     */
    public static final int MAX_FIELD_WIDTH = 25;
    /**
     * The category identifier for this record.
     */
    public static final UInt8 CATEGORY_ID = new UInt8(0x01);

    /**
     * Creates a new field labels record with no fields.
     */
    public FieldLabelsRecord() {
        super();
        header = new RecordHeader();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new field labels record from a <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     * @see #deserialize(byte[])
     */
    public FieldLabelsRecord(byte[] data) {
        super(data);
        header = new RecordHeader();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new field labels record from a <code>Record</code>.
     * 
     * @param record
     *            the record
     */
    public FieldLabelsRecord(Record record) {
        super(record);
    }

    /**
     * Creates a new field labels record from a list of field labels.
     * 
     * @param fieldLabels
     *            the field labels
     * @see #setFieldLabels(List)
     */
    public FieldLabelsRecord(List<String> fieldLabels) {
        this();
        setFieldLabels(fieldLabels);
    }

    /**
     * Sets the field labels.
     * <p>
     * If the length of a label is longer than {@value #MAX_FIELD_WIDTH} characters, then it will be
     * truncated to {@value #MAX_FIELD_WIDTH} characters.
     */
    public void setFieldLabels(List<String> fieldLabels) {
        for (int i = 0; i != fieldLabels.size(); i++) {
            String fieldLabel = fieldLabels.get(i);
            if (fieldLabel.length() > MAX_FIELD_WIDTH) {
                fieldLabels.remove(i);
                fieldLabels.add(i, fieldLabel.substring(0, MAX_FIELD_WIDTH));
            }
        }
        setInternalFields(fieldLabels);
    }

    /**
     * Gets the field labels.
     */
    public List<String> getFieldLabels() {
        return getInternalFields();
    }
}
