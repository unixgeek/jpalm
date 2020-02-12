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
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.jpalm.mobiledb.DatabaseInfo;
import net.sourceforge.jpalm.mobiledb.FilterCriterion;
import net.sourceforge.jpalm.mobiledb.MobileDB;
import net.sourceforge.jpalm.mobiledb.SortCriterion;
import net.sourceforge.jpalm.mobiledb.field.definition.Definition;
import net.sourceforge.jpalm.mobiledb.field.type.Text;
import net.sourceforge.jpalm.mobiledb.field.type.Type;
import net.sourceforge.jpalm.palmdb.PalmReader;

public class ReadMobileDB {

    public static void main(String[] args) throws Exception {

        /*
         * This example reads the MobileDB database created in the create MobileDB database example.
         * It could be easily modified to be more generic.
         */

        MobileDB database = new MobileDB();

        // Load the database from a file.
        PalmReader reader = new PalmReader();
        reader.load(new FileInputStream("TestMobileDB.pdb"), database);

        // The name of the database.
        System.out.println("=> name: " + database.getName());
        // The database info note.
        System.out.println("=> note: " + database.getNote());
        // The embedded Java class.
        System.out.println("=> user data: " + ((String) database.getUserData()));
        // The creation date.
        System.out.println("=> created: " + database.getCreationDate());

        // Database information.
        DatabaseInfo info = database.getDatabaseInfo();
        System.out.println("=> search on global find: " + info.isSearchOnGlobalFind());
        System.out.println("=> edit on select: " + info.isEditOnSelect());
        System.out.println("=> display long dates: " + info.isDisplayLongDates());

        // The first filter criterion.
        FilterCriterion filter = info.getFilter1();
        // The field number that is being filtered.
        System.out.println("=> filter on field: "
                + (filter.getFieldNumber().int8Value() - FilterCriterion.FIELD_OFFSET.int8Value()));
        // The filter string.
        System.out.println("=> filter: " + filter.getFilter());
        /*
         * The filter flags. Use hasBitmask() and FilterCriterion.FLAG_* to get a more meaningful
         * value.
         */
        System.out.println("=> filter flags: " + filter.getFlags());

        // The first sort criterion.
        SortCriterion sort = info.getSort1();
        System.out.println("=> sort on field: " + sort.getFieldNumber());

        // The field display sizes.
        System.out.println("=> field display sizes: "
                + Arrays.toString(database.getFieldDisplaySizes().toArray()));

        // The field definitions are needed to get the data records.
        List<Definition> fieldDefinitions = database.getFieldDefinitions();

        // The only data record.
        List<Type> data = database.getDataRecords().get(0).getFields(fieldDefinitions);
        for (Type type : data) {
            /*
             * Because this example is printing data, we aren't concerned about the instance of
             * type.
             */
            System.out.println("=> type: " + type + " [class " + type.getClass().getSimpleName()
                    + "]");

            // Various ways of determining the instance of type.
            if (Text.class.isInstance(type)) {
                Text text = (Text) type;
                System.out.println("==> isInstance(): " + text.toString());
            }
            if (type.getClass().isAssignableFrom(Text.class)) {
                Text text = (Text) type;
                System.out.println("==> isAssignableFrom(): " + text.toString());
            }
            if (type instanceof Text) {
                Text text = (Text) type;
                System.out.println("==> instanceof: " + text.toString());
            }
        }

        // The only filtered data record.
        data = database.getFilteredDataRecords().get(0).getFields(fieldDefinitions);
        for (Type type : data) {
            System.out.println("=> type: " + type + " [class " + type.getClass().getSimpleName()
                    + "]");
        }
    }
}
