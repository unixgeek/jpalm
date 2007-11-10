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

import net.sourceforge.jpalm.mobiledb.field.type.Type;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.juint.UInt8;

/**
 * A data record in a MobileDB database that is being filtered from view.
 */
public class FilteredDataRecord extends DataRecord {
    /**
     * The category identifier for this record.
     */
    public static final UInt8 CATEGORY_ID = new UInt8(0x03);

    /**
     * Creates a new filtered data record with no data.
     */
    public FilteredDataRecord() {
        super();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new filtered data record from a <code>Record</code>.
     * 
     * @param record
     *            the record
     */
    public FilteredDataRecord(Record record) {
        super(record);
    }

    /**
     * Creates a new filtered data record from a <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     * @see #deserialize(byte[])
     */
    public FilteredDataRecord(byte[] data) {
        super(data);
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new data record from the specified fields.
     * 
     * @param fields
     *            the fields
     * @see #setFields(List)
     */
    public FilteredDataRecord(List<Type> fields) {
        this();
        setFields(fields);
    }
}
