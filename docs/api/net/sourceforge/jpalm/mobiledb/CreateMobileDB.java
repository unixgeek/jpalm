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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import net.sourceforge.jpalm.mobiledb.DatabaseInfo;
import net.sourceforge.jpalm.mobiledb.FilterCriterion;
import net.sourceforge.jpalm.mobiledb.MobileDB;
import net.sourceforge.jpalm.mobiledb.SortCriterion;
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
import net.sourceforge.jpalm.mobiledb.record.FilteredDataRecord;
import net.sourceforge.jpalm.palmdb.PalmWriter;
import net.sourceforge.juint.Int8;
import net.sourceforge.juint.UInt8;

public class CreateMobileDB {

    public static void main(String[] args) throws Exception {

        /*
         * This example creates a MobileDB database.
         */

        // Define a field name to field definition mapping using a LinkedHashMap to preserver order.
        LinkedHashMap<String, Definition> fieldDefinitions = new LinkedHashMap<String, Definition>();

        /*
         * The initial value parameter to the SequenceDefinition is the initial value for a new
         * record; because we will be adding two records later in the example, the initial value of
         * the sequence field (id) should be 3.
         */
        fieldDefinitions.put("Id", new SequenceDefinition(3, 1));
        fieldDefinitions.put("Name", new TextDefinition(true)); // auto capitalize
        fieldDefinitions.put("Age", new NumberDefinition());
        fieldDefinitions.put("Phone", new PhoneDefinition());
        fieldDefinitions.put("Type", new ListDefinition(Arrays.asList(new ListOption("Home"),
                new ListOption("Work"), new ListOption("Mobile"))));
        fieldDefinitions.put("Email", new EmailDefinition());
        fieldDefinitions.put("Website", new URLDefinition());
        fieldDefinitions.put("Enabled", new CheckboxDefinition(true)); // default to checked
        fieldDefinitions.put("Date Added", new DateDefinition(true)); // default to current date
        fieldDefinitions.put("Time Added", new TimeDefinition(true)); // default to current time

        // Create a new database using the field definitions.
        MobileDB database = new MobileDB(fieldDefinitions);
        // The name of the database.
        database.setName("TestMobileDB");
        // The database info note.
        database.setNote("Example use of SimpleMobileDB");
        // Embed a Java class in the database (not seen by MobileDB).
        database.setUserData("Embedded String.class");

        // Database information.
        DatabaseInfo info = new DatabaseInfo();
        // Make this database visible to find.
        info.setSearchOnGlobalFind(true);
        // Don't edit on select.
        info.setEditOnSelect(false);
        // Display dates in long format.
        info.setDisplayLongDates(true);
        // Scroll lock the Name column.
        info.setLockedColumn(new UInt8(1));
        // Password protect the database.
        info.setPasswordHash(info.hashPassword("test"));
        // Make the database read-only without the password.
        info.setPasswordProtection(DatabaseInfo.PASSWORD_PROTECTION_READ_ONLY);

        // Create a filter criterion.
        FilterCriterion filter = new FilterCriterion();
        // Filter on the Enabled field (field 7 starting from 0).
        filter.setFieldNumber(new Int8(FilterCriterion.FIELD_OFFSET.int8Value() + 7));
        // Filter where Enabled is true (or checked).
        filter.setFilter("true");
        filter.setFlags(FilterCriterion.FLAG_IS);
        info.setFilter1(filter);

        // Create a sort criterion.
        SortCriterion sort = new SortCriterion();
        // Sort by id, ascending.
        sort.setFieldNumber(new Int8(0));
        sort.setDescending(false);
        info.setSort1(sort);

        // Set the database information on the database.
        database.setDatabaseInfo(info);

        // Display sizes of each field in pixels.
        database.setFieldDisplaySizes(Arrays.asList(10, 40, 20, 40, 40, 40, 40, 20, 40, 40));

        // Create a record.
        ArrayList<DataRecord> dataRecords = new ArrayList<DataRecord>();
        ArrayList<Type> data = new ArrayList<Type>();
        data.add(new Number(1));
        data.add(new Text("Bill"));
        data.add(new Number(32));
        data.add(new Phone("555-1234"));
        data.add(new ListOption("Mobile"));
        data.add(new Email("bill@example.com"));
        data.add(new URL("http://bill.com"));
        data.add(new Checkbox(true));
        data.add(new Date());
        data.add(new Time());
        dataRecords.add(new DataRecord(data));

        // Add the record to the database.
        database.setDataRecords(dataRecords);

        // Create a filtered record.
        ArrayList<FilteredDataRecord> filteredDataRecords = new ArrayList<FilteredDataRecord>();
        ArrayList<Type> filteredData = new ArrayList<Type>();
        filteredData.add(new Number(2));
        filteredData.add(new Text("Jane"));
        filteredData.add(new Number(26));
        filteredData.add(new Phone("555-0011"));
        filteredData.add(new ListOption("Home"));
        filteredData.add(new Email("jane@example.com"));
        filteredData.add(new URL("http://jane.com"));
        filteredData.add(new Checkbox(false));
        filteredData.add(new Date());
        filteredData.add(new Time());
        filteredDataRecords.add(new FilteredDataRecord(filteredData));

        // Add the record to the database.
        database.setFilteredDataRecords(filteredDataRecords);

        // Write the database to a file.
        PalmWriter writer = new PalmWriter();
        writer.save(new FileOutputStream("TestMobileDB.pdb"), database);
    }
}
