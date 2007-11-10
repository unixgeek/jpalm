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

import junit.framework.TestCase;
import net.sourceforge.jpalm.mobiledb.field.definition.CheckboxDefinition;
import net.sourceforge.jpalm.mobiledb.field.definition.DateDefinition;
import net.sourceforge.jpalm.mobiledb.field.definition.Definition;
import net.sourceforge.jpalm.mobiledb.field.definition.EmailDefinition;
import net.sourceforge.jpalm.mobiledb.field.definition.ListDefinition;
import net.sourceforge.jpalm.mobiledb.field.definition.NumberDefinition;
import net.sourceforge.jpalm.mobiledb.field.definition.PhoneDefinition;
import net.sourceforge.jpalm.mobiledb.field.definition.SequenceDefinition;
import net.sourceforge.jpalm.mobiledb.field.definition.TextDefinition;
import net.sourceforge.jpalm.mobiledb.field.definition.TimeDefinition;
import net.sourceforge.jpalm.mobiledb.field.definition.URLDefinition;
import net.sourceforge.jpalm.mobiledb.field.type.Checkbox;
import net.sourceforge.jpalm.mobiledb.field.type.Date;
import net.sourceforge.jpalm.mobiledb.field.type.Email;
import net.sourceforge.jpalm.mobiledb.field.type.ListOption;
import net.sourceforge.jpalm.mobiledb.field.type.Number;
import net.sourceforge.jpalm.mobiledb.field.type.Phone;
import net.sourceforge.jpalm.mobiledb.field.type.Text;
import net.sourceforge.jpalm.mobiledb.field.type.Time;
import net.sourceforge.jpalm.mobiledb.field.type.Type;
import net.sourceforge.jpalm.mobiledb.field.type.URL;
import net.sourceforge.jpalm.mobiledb.record.DataRecord;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;

public class TestDataRecord extends TestCase {
    private byte[] data = { (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01, (byte) 0xff, 0x00, 0x00,
            0x00, 0x10, 0x00, 0x01, 0x11, 0x00, (byte) 0xff };

    public void testDataRecord() {
        DataRecord record = new DataRecord();
        assertNull(record.fields);
    }

    public void testDataRecordRecord() {
        Record record = new RecordImpl(data);
        DataRecord dataRecord = new DataRecord(record);
        assertEquals(dataRecord.fields[0][0], 0x10);
        assertEquals(dataRecord.fields[1][0], 0x11);
    }

    public void testDataRecordByteArray() {
        DataRecord dataRecord = new DataRecord(data);
        assertEquals(dataRecord.fields[0][0], 0x10);
        assertEquals(dataRecord.fields[1][0], 0x11);
    }

    public void testGetFields() {
        ArrayList<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new CheckboxDefinition());
        definitions.add(new DateDefinition());
        definitions.add(new EmailDefinition());
        definitions.add(new ListDefinition());
        definitions.add(new NumberDefinition());
        definitions.add(new PhoneDefinition());
        definitions.add(new TextDefinition());
        definitions.add(new TimeDefinition());
        definitions.add(new SequenceDefinition());
        definitions.add(new URLDefinition());
        definitions.add(null);
        definitions.add(new Definition() {
            public String getIndicator() {
                return null;
            }

            public String getRegex() {
                return null;
            }
        });

        ArrayList<String> fields = new ArrayList<String>();
        fields.add("true");
        fields.add("");
        fields.add("hi@example.com");
        fields.add("option1");
        fields.add("1");
        fields.add("123");
        fields.add("hi");
        fields.add("30");
        fields.add("1001");
        fields.add("http://www.example.com");
        fields.add("field with a null definition");
        fields.add("field with an unknown definition");
        fields.add("field without a definition");

        DataRecord record = new DataRecord();
        record.setInternalFields(fields);

        List<Type> result = record.getFields(definitions);
        assertEquals(13, result.size());
        assertEquals(new Checkbox(true), result.get(0));
        assertEquals(new Date(null), result.get(1));
        assertEquals(new Email("hi@example.com"), result.get(2));
        assertEquals(new ListOption("option1"), result.get(3));
        assertEquals(new Number(1), result.get(4));
        assertEquals(new Phone("123"), result.get(5));
        assertEquals(new Text("hi"), result.get(6));
        assertEquals(new Time(30), result.get(7));
        assertEquals(new Number(1001), result.get(8));
        assertEquals(new URL("http://www.example.com"), result.get(9));
        assertEquals(new Text("field with a null definition"), result.get(10));
        assertEquals(new Text("field with an unknown definition"), result.get(11));
        assertEquals(new Text("field without a definition"), result.get(12));
    }

    public void testSetFields() {
        ArrayList<Type> types = new ArrayList<Type>();
        types.add(new Checkbox(true));
        types.add(new Date(null));
        types.add(new Email("hi@example.com"));
        types.add(new ListOption("option1"));
        types.add(new Number(1));
        types.add(new Phone("123"));
        types.add(new Text("hi"));
        types.add(new Time(30));
        types.add(new URL("http://www.example.com"));
        types.add(null);

        DataRecord record = new DataRecord();
        record.setFields(types);

        List<String> result = record.getInternalFields();
        assertEquals(10, result.size());
        assertEquals("true", result.get(0));
        assertEquals("", result.get(1));
        assertEquals("hi@example.com", result.get(2));
        assertEquals("option1", result.get(3));
        assertEquals("1", result.get(4));
        assertEquals("123", result.get(5));
        assertEquals("hi", result.get(6));
        assertEquals("30", result.get(7));
        assertEquals("http://www.example.com", result.get(8));
        assertEquals("", result.get(9));
    }
}
