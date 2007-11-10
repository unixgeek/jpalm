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

import net.sourceforge.jpalm.BaseTestFromResource;
import net.sourceforge.jpalm.mobiledb.record.FieldDisplaySizesRecord;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;

public class TestFieldDisplaySizesRecord extends BaseTestFromResource {
    protected FieldDisplaySizesRecord fieldDisplaySizesRecord;

    public TestFieldDisplaySizesRecord() {
        // The offset of the FieldDisplaySizesRecord in RESOURCE.
        offset = 867;
        /*
         * This record ends at the offset of the next record in RESOURCE and 764 is the offset of
         * the next record.
         */
        length = 959 - (int) offset;
    }

    protected void setUp() throws Exception {
        super.setUp();
        fieldDisplaySizesRecord = new FieldDisplaySizesRecord(data);
    }

    public void testFieldLengthsRecord() {
        fieldDisplaySizesRecord = new FieldDisplaySizesRecord();
        for (Integer field : fieldDisplaySizesRecord.getFieldDisplaySizes()) {
            assertEquals(Integer.valueOf(80), field);
        }
    }

    public void testFieldLengthsRecordByteArray() {
        for (Integer field : fieldDisplaySizesRecord.getFieldDisplaySizes()) {
            assertEquals(Integer.valueOf(80), field);
        }
    }

    public void testFieldLengthsRecordRecord() {
        Record record = new RecordImpl(data);
        fieldDisplaySizesRecord = new FieldDisplaySizesRecord(record);
        for (Integer field : fieldDisplaySizesRecord.getFieldDisplaySizes()) {
            assertEquals(Integer.valueOf(80), field);
        }
    }

    public void testSetFieldDisplaySizes() {
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        lengths.add(1);
        lengths.add(null);

        FieldDisplaySizesRecord record = new FieldDisplaySizesRecord();
        record.setFieldDisplaySizes(lengths);

        List<String> result = record.getInternalFields();
        assertEquals(2, result.size());
        assertEquals("1", result.get(0));
        assertEquals("0", result.get(1));
    }

    public void testGetFieldNotANumber() {
        ArrayList<String> fields = new ArrayList<String>();
        fields.add("foo");

        FieldDisplaySizesRecord record = new FieldDisplaySizesRecord();
        record.setInternalFields(fields);

        List<Integer> result = record.getFieldDisplaySizes();
        assertEquals(1, result.size());
        assertEquals(new Integer(0), result.get(0));
    }

    public void testSerialize() {
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        lengths.add(1);

        FieldDisplaySizesRecord record = new FieldDisplaySizesRecord();
        record.setFieldDisplaySizes(lengths);

        assertEquals(1, record.getInternalFields().size());
        record.serialize();
        List<String> result = record.getInternalFields();
        assertEquals(20, result.size());
        assertEquals("1", result.get(0));
        assertEquals("80", result.get(1));
    }
}
