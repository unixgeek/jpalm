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
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordHeader;
import net.sourceforge.juint.UInt8;

/**
 * A data record in a MobileDB database.
 */
public class DataRecord extends AbstractRecord {
    /**
     * The category identifier for this record.
     */
    public static final UInt8 CATEGORY_ID = new UInt8(0x02);

    /**
     * Creates a new data record with no fields.
     */
    public DataRecord() {
        super();
        header = new RecordHeader();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new data record from a <code>Record</code>.
     * 
     * @param record
     *            the record
     */
    public DataRecord(Record record) {
        super(record);
    }

    /**
     * Creates a new data record from a <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     * @see #deserialize(byte[])
     */
    public DataRecord(byte[] data) {
        super(data);
        header = new RecordHeader();
        header.setAttributes(CATEGORY_ID);
    }

    /**
     * Creates a new data record from the specified fields.
     * 
     * @param fields
     *            the fields
     * @see #setFields(List)
     */
    public DataRecord(List<Type> fields) {
        this();
        setFields(fields);
    }

    /**
     * Gets the fields using the specified field definitions. If the field definitions list is
     * smaller than the number of fields, the remaining fields will be treated as a
     * {@link TextDefinition}.
     * 
     * @param fieldDefinitions
     *            the field definitions
     * @return the fields
     */
    public List<Type> getFields(List<Definition> fieldDefinitions) {
        List<String> typesAsStrings = getInternalFields();
        if (fieldDefinitions.size() < typesAsStrings.size()) {
            int difference = typesAsStrings.size() - fieldDefinitions.size();
            for (int i = 0; i != difference; i++) {
                fieldDefinitions.add(new TextDefinition());
            }
        }

        ArrayList<Type> types = new ArrayList<Type>(typesAsStrings.size());
        for (int i = 0; i != typesAsStrings.size(); i++) {
            String typeAsString = typesAsStrings.get(i);
            Definition definition = fieldDefinitions.get(i);

            Type type;
            // Default to Text.
            if (definition == null)
                definition = new TextDefinition();

            if (CheckboxDefinition.class.isInstance(definition)) {
                type = Checkbox.fromMobileDB(typeAsString);
            }
            else if (DateDefinition.class.isInstance(definition)) {
                type = Date.fromMobileDB(typeAsString);
            }
            else if (EmailDefinition.class.isInstance(definition)) {
                type = Email.fromMobileDB(typeAsString);
            }
            else if (ListDefinition.class.isInstance(definition)) {
                type = ListOption.fromMobileDB(typeAsString);
            }
            else if (SequenceDefinition.class.isInstance(definition)) {
                type = Number.fromMobileDB(typeAsString);
            }
            else if (NumberDefinition.class.isInstance(definition)) {
                type = Number.fromMobileDB(typeAsString);
            }
            else if (PhoneDefinition.class.isInstance(definition)) {
                type = Phone.fromMobileDB(typeAsString);
            }
            else if (TextDefinition.class.isInstance(definition)) {
                type = Text.fromMobileDB(typeAsString);
            }
            else if (TimeDefinition.class.isInstance(definition)) {
                type = Time.fromMobileDB(typeAsString);
            }
            else if (URLDefinition.class.isInstance(definition)) {
                type = URL.fromMobileDB(typeAsString);
            }
            else {
                type = Text.fromMobileDB(typeAsString);
            }
            types.add(type);
        }
        return types;
    }

    /**
     * Sets the fields.
     * 
     * @param fields
     *            the fields
     */
    public void setFields(List<Type> fields) {
        ArrayList<String> typesAsStrings = new ArrayList<String>(fields.size());
        for (Type type : fields) {
            if (type != null)
                typesAsStrings.add(type.toMobileDB());
            else
                typesAsStrings.add("");
        }
        setInternalFields(typesAsStrings);
    }
}
