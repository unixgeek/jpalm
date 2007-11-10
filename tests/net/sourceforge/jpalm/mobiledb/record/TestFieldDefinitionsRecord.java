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

import net.sourceforge.jpalm.BaseTestFromResource;
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
import net.sourceforge.jpalm.mobiledb.field.type.ListOption;
import net.sourceforge.jpalm.mobiledb.record.FieldDefinitionsRecord;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;

public class TestFieldDefinitionsRecord extends BaseTestFromResource {
    protected FieldDefinitionsRecord fieldDefinitionsRecord;

    public TestFieldDefinitionsRecord() {
        // The offset of the DataTypeRecord record in RESOURCE.
        offset = 766;
        /*
         * This record ends at the offset of the next record in RESOURCE and 867 is the offset of
         * the next record.
         */
        length = 867 - (int) offset;
    }

    protected void setUp() throws Exception {
        super.setUp();
        fieldDefinitionsRecord = new FieldDefinitionsRecord(data);
    }

    public void testDataTypeRecordRecord() {
        Record record = new RecordImpl(data);
        fieldDefinitionsRecord = new FieldDefinitionsRecord(record);
        assertEquals(20, fieldDefinitionsRecord.getFieldDefinitions().size());
        assertTrue(fieldDefinitionsRecord.getFieldDefinitions().get(0).getClass().isInstance(
                new TextDefinition()));
    }

    public void testGetFieldDefinitionsDefaultText() {
        /*
         * First field: e; Second field: F. F is no longer used, so this now defaults to TextField
         * instead of the old NumericFloat (now NumberField).
         */
        byte[] data = { (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01, (byte) 0xff, 0x00, 0x00, 0x00,
                0x65, 0x00, 0x01, 0x46, 0x00, (byte) 0xff };
        fieldDefinitionsRecord = new FieldDefinitionsRecord(data);
        assertTrue(fieldDefinitionsRecord.getFieldDefinitions().get(0).getClass().isInstance(
                new TextDefinition()));
        assertTrue(fieldDefinitionsRecord.getFieldDefinitions().get(1).getClass().isInstance(
                new TextDefinition()));
    }

    public void testGetFieldDefinitions() {
        List<Definition> definitions = fieldDefinitionsRecord.getFieldDefinitions();

        assertEquals("size", 20, definitions.size());

        assertTrue(((TextDefinition) definitions.get(0)).isAutoCapitalize());
        assertTrue(definitions.get(1).getClass().isInstance(new NumberDefinition()));
        assertEquals(6d, ((SequenceDefinition) definitions.get(2)).getInitialValue());
        assertEquals(1d, ((SequenceDefinition) definitions.get(2)).getIncrement());
        assertFalse(((CheckboxDefinition) definitions.get(3)).isDefaultToTrue());
        assertTrue(((DateDefinition) definitions.get(4)).isDefaultToCurrentDate());
        assertTrue(((TimeDefinition) definitions.get(5)).isDefaultToCurrentTime());
        List<ListOption> options = ((ListDefinition) definitions.get(6)).getOptions();
        assertEquals(3, options.size());
        assertEquals("Item three", options.get(0).getValue());
        assertEquals("Item one", options.get(1).getValue());
        assertEquals("Item two", options.get(2).getValue());

        // Fields 8 - 20 are text.
        for (int i = 7; i != definitions.size(); i++) {

            assertTrue("8 - 20", definitions.get(i).getClass().isInstance(new TextDefinition()));
        }
    }

    public void testGetFieldDefinitionsNew() {
        FieldDefinitionsRecord record = new FieldDefinitionsRecord();
        record.setInternalFields(Arrays.asList("M", "W", "P"));
        List<Definition> definitions = record.getFieldDefinitions();

        assertTrue(definitions.get(0).getClass().isInstance(new EmailDefinition()));
        assertTrue(definitions.get(1).getClass().isInstance(new URLDefinition()));
        assertTrue(definitions.get(2).getClass().isInstance(new PhoneDefinition()));
    }

    public void testSetFieldDefinitions() {
        ArrayList<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new TextDefinition());
        definitions.add(new SequenceDefinition(1.0, 1.1));

        FieldDefinitionsRecord record = new FieldDefinitionsRecord();
        record.setFieldDefinitions(definitions);

        List<String> result = record.getInternalFields();
        assertEquals(2, result.size());
        assertEquals(new TextDefinition().getIndicator(), result.get(0));
        assertEquals(new SequenceDefinition(1.0, 1.1).getIndicator(), result.get(1));
    }

    public void testSerialize() {
        ArrayList<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new TextDefinition());
        definitions.add(new SequenceDefinition(1.0, 1.1));

        FieldDefinitionsRecord record = new FieldDefinitionsRecord();
        record.setFieldDefinitions(definitions);

        assertEquals(2, record.getInternalFields().size());
        record.serialize();
        assertEquals(20, record.getInternalFields().size());
    }
}
