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

import java.util.Arrays;

import net.sourceforge.jpalm.BaseTestFromResource;
import net.sourceforge.jpalm.mobiledb.record.PreferencesRecord;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;

public class TestPreferencesRecord extends BaseTestFromResource {
    protected PreferencesRecord preferencesRecord;

    public TestPreferencesRecord() {
        // The offset of the PreferencesRecord in RESOURCE.
        offset = 683;
        /*
         * This record ends at the offset of the next record in RESOURCE and 764 is the offset of
         * the next record.
         */
        length = 766 - (int) offset;
    }

    protected void setUp() throws Exception {
        super.setUp();
        preferencesRecord = new PreferencesRecord(data);
    }

    public void testPreferencesRecord() {
        preferencesRecord = new PreferencesRecord();
        for (String field : preferencesRecord.getInternalFields()) {
            assertEquals("", field);
        }
    }

    public void testPreferencesRecordByteArray() {
        assertEquals("What the frack goes here?", preferencesRecord.getInternalFields().get(0));
        // This field isn't used in version 4.
        assertEquals("-281889789", preferencesRecord.getInternalFields().get(1));
    }

    public void testPreferencesRecordRecord() {
        Record record = new RecordImpl(data);
        preferencesRecord = new PreferencesRecord(record);
        assertEquals("What the frack goes here?", preferencesRecord.getInternalFields().get(0));
        // This field isn't used in version 4.
        assertEquals("-281889789", preferencesRecord.getInternalFields().get(1));
    }

    public void testGetNote() {
        PreferencesRecord record = new PreferencesRecord();
        record.setInternalFields(Arrays.asList("noted"));

        assertEquals("noted", record.getNote());
        assertEquals("", new PreferencesRecord().getNote());
    }

    public void testSetNote() {
        PreferencesRecord record = new PreferencesRecord();
        record.setNote("noted");

        assertEquals("noted", record.getInternalFields().get(0));
        record.setNote("notedAgain");
        assertEquals("notedAgain", record.getInternalFields().get(0));
    }

    public void testSerialize() {
        PreferencesRecord record = new PreferencesRecord();
        record.setInternalFields(Arrays.asList("noted"));

        assertEquals(1, record.getInternalFields().size());
        record.serialize();
        assertEquals(20, record.getInternalFields().size());
        assertEquals("noted", record.getInternalFields().get(0));
        assertEquals("", record.getInternalFields().get(1));
    }
}
