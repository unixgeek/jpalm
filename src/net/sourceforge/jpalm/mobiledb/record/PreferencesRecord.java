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
 * The preferences record in a MobileDB database.
 * <p>
 * There should always be 20 fields in this record. The first field contains the database info note.
 * The remaining fields are unused.
 * <p>
 * When this class is serialized, if the field count is not 20, fields will be added or removed
 * until the count is 20. If fields need to be added, they will be empty strings.
 */
public class PreferencesRecord extends AbstractRecord {
    /**
     * The category identifier for this record.
     */
    public static final UInt8 CATEGORY_ID = new UInt8(0x04);

    /**
     * Creates a new preferences record.
     */
    public PreferencesRecord() {
        super();
        header = new RecordHeader();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new preferences record from a <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     * @see #deserialize(byte[])
     */
    public PreferencesRecord(byte[] data) {
        super(data);
        header = new RecordHeader();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new preferences record from a <code>Record</code>.
     * 
     * @param record
     *            the record
     */
    public PreferencesRecord(Record record) {
        super(record);
    }

    /**
     * Creates a new preferences record from a note.
     * 
     * @param note
     *            the note
     * @see #setNote(String)
     */
    public PreferencesRecord(String note) {
        this();
        setNote(note);
    }

    /**
     * Gets the database info note.
     * 
     * @return the note or "" if there is not a note set
     */
    public String getNote() {
        List<String> fields = getInternalFields();
        if (fields.size() > 0)
            return fields.get(0);
        else
            return "";
    }

    /**
     * Sets the database info note.
     * 
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        List<String> fields = getInternalFields();
        if (fields.size() > 0)
            fields.add(0, note);
        else
            fields.add(note);
        setInternalFields(fields);
    }

    @Override
    public byte[] serialize() {
        setInternalFields(trimOrFillList(20, ""));
        return super.serialize();
    }
}
