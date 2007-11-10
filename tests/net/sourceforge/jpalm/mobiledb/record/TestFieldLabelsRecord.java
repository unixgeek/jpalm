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
import net.sourceforge.jpalm.mobiledb.record.FieldLabelsRecord;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;

public class TestFieldLabelsRecord extends BaseTestFromResource {
    protected FieldLabelsRecord fieldLabelsRecord;

    public TestFieldLabelsRecord() {
        // The offset of the FieldLabels record in RESOURCE.
        offset = 580;
        /*
         * This record ends at the offset of the next record in RESOURCE and 682 is the offset of
         * the next record.
         */
        length = 682 - (int) offset;
    }

    protected void setUp() throws Exception {
        super.setUp();
        fieldLabelsRecord = new FieldLabelsRecord(data);
    }

    public void testFieldLabelsRecordRecord() {
        Record record = new RecordImpl(data);
        fieldLabelsRecord = new FieldLabelsRecord(record);
        assertEquals("Seventh Field", fieldLabelsRecord.getFieldLabels().get(6));
    }

    public void testGetFieldLabels() {
        assertEquals("Seventh Field", fieldLabelsRecord.getFieldLabels().get(6));
    }

    public void testSetFieldLabels() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Field1");
        labels.add("Field2");
        fieldLabelsRecord.setFieldLabels(labels);
        List labelsAfter = fieldLabelsRecord.getFieldLabels();
        String label = (String) labelsAfter.get(1);
        assertEquals("Field2", label);
    }

    public void testSetFieldLabelsTooLong() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("A field label that is too long");
        fieldLabelsRecord.setFieldLabels(labels);
        List labelsAfter = fieldLabelsRecord.getFieldLabels();
        String label = (String) labelsAfter.get(0);
        assertEquals("A field label that is too", label);
    }

    public void testFieldLabelsRecord() {
        FieldLabelsRecord record = new FieldLabelsRecord();
        assertEquals(FieldLabelsRecord.CATEGORY_ID, record.getHeader().getAttributes());
    }
}
