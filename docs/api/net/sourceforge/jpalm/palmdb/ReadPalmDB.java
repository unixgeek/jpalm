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
import java.util.HashMap;
import java.util.List;

import net.sourceforge.jpalm.Utilities;
import net.sourceforge.jpalm.palmdb.Category;
import net.sourceforge.jpalm.palmdb.PalmDB;
import net.sourceforge.jpalm.palmdb.PalmDBImpl;
import net.sourceforge.jpalm.palmdb.PalmReader;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.juint.UInt8;

public class ReadPalmDB {

    public static void main(String[] args) throws Exception {

        /*
         * This example reads a Palm Todo database.
         */

        PalmDB database = new PalmDBImpl();

        // Load the database from a file.
        PalmReader reader = new PalmReader();
        reader.load(new FileInputStream("ToDoDB.pdb"), database);

        // Put the categories and category ids into a map for easy lookup.
        List<String> categoryLabels = database.getApplicationInfo().getCategoryLabels();
        List<UInt8> categoryIds = database.getApplicationInfo().getCategoryUniqueIds();
        HashMap<Integer, String> categories = new HashMap<Integer, String>();
        for (int i = 0; i != Category.NUMBER_OF_CATEGORIES; i++) {
            categories.put(categoryIds.get(i).intValue(), categoryLabels.get(i));
        }

        // Loop through the records.
        int number = 0;
        for (Record record : database.getRecords()) {
            number++;

            byte[] data = record.serialize();

            // First two bytes are date and I'm lazy, so ignore them.

            // The todo item status.
            boolean completed = ((byte) (data[2] & (byte) 0x80)) == (byte) 0x80;
            // The todo item priority.
            byte priority = (byte) (data[2] & 0x7f);

            // Split the remaining bytes using the null terminator.
            int index = Utilities.indexOf(data, 3, (byte) 0x00);
            // First token is the todo.
            String todo = new String(data, 3, index - 3);
            // Second token is the note (if one exists).
            String note = new String(data, index + 1, data.length - index - 1);
            // The todo item category.
            String category = categories.get(record.getHeader().getCategory().intValue());

            System.out.println("[ record " + number + "]");
            System.out.println("=> completed: " + completed);
            System.out.println("=> priority: " + priority);
            System.out.println("=> todo: " + todo);
            System.out.println("=> note: " + note);
            System.out.println("=> category: " + category);
        }
    }
}
