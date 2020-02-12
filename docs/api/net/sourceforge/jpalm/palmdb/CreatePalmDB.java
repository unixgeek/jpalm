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

import net.sourceforge.jpalm.palmdb.ApplicationInfo;
import net.sourceforge.jpalm.palmdb.Category;
import net.sourceforge.jpalm.palmdb.Header;
import net.sourceforge.jpalm.palmdb.PalmDB;
import net.sourceforge.jpalm.palmdb.PalmDBImpl;
import net.sourceforge.jpalm.palmdb.PalmWriter;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;
import net.sourceforge.juint.UInt8;

public class CreatePalmDB {

    public static void main(String[] args) throws Exception {

        /*
         * This example creates a Palm Todo database.
         */

        String name = "ToDoDB";
        PalmDB database = new PalmDBImpl();

        // Setup the header.
        Header header = database.getHeader();
        header.setName(name);
        header.setCreator("todo");
        header.setType("DATA");

        // Add category labels.
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.setCategoryLabels(Arrays.asList("Honeydo", "Geekstuff"));

        // Generate category ids for the labels.
        ArrayList<UInt8> categoryIds = new ArrayList<UInt8>();
        for (int i = 0; i != Category.NUMBER_OF_CATEGORIES; i++) {
            categoryIds.add(new UInt8(i));
        }
        applicationInfo.setCategoryUniqueIds(categoryIds);
        applicationInfo.setLastUniqueId(new UInt8(15));

        database.setApplicationInfo(applicationInfo);

        // Create a todo item.

        String todo = "Finish this code.";
        String note = "...for a certain value of finish.";

        /*
         * Add 2 for null terminators on todo and note. Add 3 for the date, priority, and completed
         * fields.
         */
        byte[] data = new byte[todo.length() + note.length() + 2 + 3];

        // 0xffff = no due date.
        data[0] = (byte) 0xff;
        data[1] = (byte) 0xff;

        // Set the todo as "completed".
        data[2] = (byte) 0x80;

        // Set the priority as 4.
        data[2] = (byte) (data[2] | 0x04);

        // Copy the todo.
        System.arraycopy(todo.getBytes(), 0, data, 3, todo.length());
        data[todo.length() + 3 + 1] = 0x00;

        // Copy the note.
        System.arraycopy(note.getBytes(), 0, data, todo.length() + 3 + 1, note.length());
        data[todo.length() + note.length() + 3 + 1] = 0x00;

        Record record = new RecordImpl(data);

        // Set the category as "Geekstuff".
        record.getHeader().setCategory(new UInt8(1));

        // Add the record.
        database.setRecords(Arrays.asList(record));

        PalmWriter writer = new PalmWriter();
        writer.save(new FileOutputStream(name + ".pdb"), database);
    }
}
