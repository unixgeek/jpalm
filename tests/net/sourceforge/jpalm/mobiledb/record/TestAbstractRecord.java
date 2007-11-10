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
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.jpalm.mobiledb.record.AbstractRecord;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordHeader;
import net.sourceforge.jpalm.palmdb.RecordImpl;

public class TestAbstractRecord extends TestCase {
    private Dummy dummy;
    private byte[] dummyData = { (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01, (byte) 0xff, 0x00,
            0x00, 0x00, 0x66, 0x69, 0x65, 0x6c, 0x64, 0x31, 0x00, 0x01, 0x00, 0x02, 0x66, 0x69,
            0x65, 0x6c, 0x64, 0x32, 0x00, (byte) 0xff };
    private byte[] data = { (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01, (byte) 0xff, 0x00, 0x00,
            0x00, 0x66, 0x69, 0x65, 0x6c, 0x64, 0x31, 0x00, 0x01, 0x00, 0x02, 0x66, 0x69, 0x65,
            0x6c, 0x64, 0x32, 0x00, (byte) 0xff };

    private class Dummy extends AbstractRecord {
        public Dummy() {
            super();
        }

        public Dummy(byte[] data) {
            super(data);
        }

        public Dummy(Record record) {
            super(record);
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dummy = new Dummy();
    }

    public void testDeserialize() {
        dummy.deserialize(dummyData);
        byte[] field1 = { 0x66, 0x69, 0x65, 0x6c, 0x64, 0x31 };
        byte[] field2 = {};
        byte[] field3 = { 0x66, 0x69, 0x65, 0x6c, 0x64, 0x32 };
        assertEquals(3, dummy.fields.length);
        assertTrue(Utilities.isEqual(field1, dummy.fields[0]));
        assertTrue(Utilities.isEqual(field2, dummy.fields[1]));
        assertTrue(Utilities.isEqual(field3, dummy.fields[2]));
    }

    public void testSerialize() {
        dummy = new Dummy(dummyData);
        byte[] actual = dummy.serialize();
        assertTrue(Utilities.isEqual(dummyData, actual));
    }

    public void testEqualsObject() {
        Dummy dummy2 = new Dummy();
        assertEquals(dummy2, dummy);
    }

    public void testEqualsObjectNotInstanceof() {
        assertFalse(dummy.equals(new Object()));
    }

    public void testAbstractRecordRecord() {
        RecordImpl record = new RecordImpl(dummyData);
        dummy = new Dummy(dummyData);
        Dummy dummy2 = new Dummy(record);
        assertEquals(dummy, dummy2);
    }

    public void testGetHeader() {
        RecordHeader header = new RecordHeader();
        dummy.header = header;
        assertEquals(header, dummy.getHeader());
    }

    public void testSetHeader() {
        RecordHeader header = new RecordHeader();
        dummy.setHeader(header);
        assertEquals(header, dummy.header);
    }

    public void testGetInternalFields() {
        dummy = new Dummy(data);
        assertEquals(3, dummy.getInternalFields().size());
        assertEquals("field1", dummy.getInternalFields().get(0));
        assertEquals("", dummy.getInternalFields().get(1));
        assertEquals("field2", dummy.getInternalFields().get(2));
    }

    public void testsetInternalFields() {
        ArrayList<String> fields = new ArrayList<String>();
        fields.add(0, "field1");
        fields.add(1, null);
        fields.add(2, "field2");
        fields.add(3, null);
        dummy.setInternalFields(fields);
        byte[] field1 = { 0x66, 0x69, 0x65, 0x6c, 0x64, 0x31 };
        byte[] field2 = { 0x66, 0x69, 0x65, 0x6c, 0x64, 0x32 };
        assertTrue(Utilities.isEqual(field1, dummy.fields[0]));
        assertEquals(0, dummy.fields[1].length);
        assertTrue(Utilities.isEqual(field2, dummy.fields[2]));
        assertEquals(0, dummy.fields[3].length);
    }

    public void testTrimOrFillList() {
        dummy.setInternalFields(Arrays.asList("1", "2", "3", "4"));

        List<String> list = dummy.trimOrFillList(4, "");

        assertEquals(4, list.size());
        assertTrue(list.contains("1"));
        assertTrue(list.contains("2"));
        assertTrue(list.contains("3"));
        assertTrue(list.contains("4"));
    }

    public void testTrimOrFillListFill() {
        dummy.setInternalFields(Arrays.asList("1", "2"));

        List<String> list = dummy.trimOrFillList(4, "x");

        assertEquals(4, list.size());
        assertTrue(list.contains("1"));
        assertTrue(list.contains("2"));
        assertTrue(list.contains("x"));
        assertTrue(list.contains("x"));
    }

    public void testTrimOrFillListTrim() {
        dummy.setInternalFields(Arrays.asList("1", "2", "3", "4"));

        List<String> list = dummy.trimOrFillList(2, "x");

        assertEquals(2, list.size());
        assertTrue(list.contains("1"));
        assertTrue(list.contains("2"));
    }

    public void testTrimOrFillListMaxFieldWidth() {
        char[] test = new char[AbstractRecord.MAX_FIELD_WIDTH + 3];

        for (int i = 0; i != test.length; i++) {
            test[i] = 'x';
        }
        test[AbstractRecord.MAX_FIELD_WIDTH] = 'e';
        test[AbstractRecord.MAX_FIELD_WIDTH + 1] = 'n';
        test[AbstractRecord.MAX_FIELD_WIDTH + 2] = 'd';

        String testString = String.valueOf(test);
        assertTrue(testString.endsWith("end"));
        assertEquals(AbstractRecord.MAX_FIELD_WIDTH + 3, testString.length());

        dummy.setInternalFields(Arrays.asList(testString));
        List<String> list = dummy.trimOrFillList(1, "y");

        assertEquals(1, list.size());
        assertTrue(list.get(0).endsWith("x"));
        assertEquals(AbstractRecord.MAX_FIELD_WIDTH, list.get(0).length());
    }

    public void testGetInternalFieldsNullFields() {
        assertEquals(0, dummy.getInternalFields().size());
    }
}
