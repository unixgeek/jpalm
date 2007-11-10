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

import net.sourceforge.jpalm.Utilities;
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
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordHeader;
import net.sourceforge.juint.UInt8;

/**
 * The field definitions record in a MobileDB database.
 * <p>
 * There should be exactly one of these records in a MobileDB database and should always have 20
 * fields.
 * <p>
 * When this class is serialized, if the field count is not 20, fields will be added or removed
 * until the count is 20. If fields need to be added, they will be {@link TextDefinition} fields.
 */
public class FieldDefinitionsRecord extends AbstractRecord {
    /**
     * The category identifier for this record.
     */
    public static final UInt8 CATEGORY_ID = new UInt8(0x05);

    /**
     * Creates a new field definitions record.
     */
    public FieldDefinitionsRecord() {
        super();
        header = new RecordHeader();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new field definitions record from a <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     * @see #deserialize(byte[])
     */
    public FieldDefinitionsRecord(byte[] data) {
        super(data);
        header = new RecordHeader();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new field definitions record from a <code>Record</code>.
     * 
     * @param record
     *            the record
     */
    public FieldDefinitionsRecord(Record record) {
        super(record);
    }

    /**
     * Creates a new field definitions record from the specified field definitions.
     * 
     * @param fieldDefinitions
     *            the field definitions
     * @see #setFieldDefinitions(List)
     */
    public FieldDefinitionsRecord(List<Definition> fieldDefinitions) {
        this();
        setFieldDefinitions(fieldDefinitions);
    }

    /**
     * Gets the field definitions.
     * 
     * @return the field definitions
     */
    public List<Definition> getFieldDefinitions() {
        // Can't I do something fancy with enums? I hate this!
        ArrayList<Definition> definitions = new ArrayList<Definition>();

        for (String field : getInternalFields()) {
            Definition definition = new TextDefinition();
            if (Utilities.isMatch(field, definition.getRegex())) {
                definitions.add(new TextDefinition(field));
                continue;
            }

            definition = new DateDefinition();
            if (Utilities.isMatch(field, definition.getRegex())) {
                definitions.add(new DateDefinition(field));
                continue;
            }

            definition = new TimeDefinition();
            if (Utilities.isMatch(field, definition.getRegex())) {
                definitions.add(new TimeDefinition(field));
                continue;
            }

            definition = new ListDefinition();
            if (Utilities.isMatch(field, definition.getRegex())) {
                definitions.add(new ListDefinition(field));
                continue;
            }

            definition = new NumberDefinition();
            if (Utilities.isMatch(field, definition.getRegex())) {
                definitions.add(new NumberDefinition());
                continue;
            }

            definition = new SequenceDefinition();
            if (Utilities.isMatch(field, definition.getRegex())) {
                definitions.add(new SequenceDefinition(field));
                continue;
            }

            definition = new CheckboxDefinition();
            if (Utilities.isMatch(field, definition.getRegex())) {
                definitions.add(new CheckboxDefinition(field));
                continue;
            }

            definition = new EmailDefinition();
            if (Utilities.isMatch(field, definition.getRegex())) {
                definitions.add(new EmailDefinition());
                continue;
            }

            definition = new URLDefinition();
            if (Utilities.isMatch(field, definition.getRegex())) {
                definitions.add(new URLDefinition());
                continue;
            }

            definition = new PhoneDefinition();
            if (Utilities.isMatch(field, definition.getRegex())) {
                definitions.add(new PhoneDefinition());
                continue;
            }

            definitions.add(new TextDefinition());
        }

        return definitions;
    }

    /**
     * Sets the field definitions.
     * 
     * @param fields
     *            the definitions
     */
    public void setFieldDefinitions(List<Definition> fields) {
        ArrayList<String> definitionsAsStrings = new ArrayList<String>(fields.size());
        for (Definition definition : fields) {
            definitionsAsStrings.add(definition.getIndicator());
        }
        setInternalFields(definitionsAsStrings);
    }

    @Override
    public byte[] serialize() {
        setInternalFields(trimOrFillList(20, new TextDefinition().getIndicator()));
        return super.serialize();
    }
}
