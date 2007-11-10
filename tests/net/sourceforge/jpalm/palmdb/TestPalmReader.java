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
package net.sourceforge.jpalm.palmdb;

import java.io.FileInputStream;

import junit.framework.TestCase;
import net.sourceforge.jpalm.palmdb.PalmDB;
import net.sourceforge.jpalm.palmdb.PalmDBImpl;
import net.sourceforge.jpalm.palmdb.PalmReader;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;
import net.sourceforge.juint.UInt16;
import net.sourceforge.juint.UInt8;

public class TestPalmReader extends TestCase {
    protected static final String RESOURCE = "tests/net/sourceforge/jpalm/palmdb/resources/test.pdb";

    public void testLoad() throws Exception {
        PalmDB database = new PalmDBImpl();
        PalmReader reader = new PalmReader();
        FileInputStream stream = new FileInputStream(RESOURCE);
        reader.load(stream, database);

        // ApplicationInfo
        assertTrue(database.getApplicationInfo().getCategoryLabels().contains("FieldLabels"));

        // Header
        assertEquals("test", database.getHeader().getName());
        assertEquals(new UInt16(9), database.getHeader().getNumberOfRecords());

        // Record
        byte[] expectedRecordData = { (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01, (byte) 0xff,
                0x00, 0x00, 0x00, 0x41, 0x6e, 0x6f, 0x74, 0x68, 0x65, 0x72, 0x20, 0x6f, 0x6e, 0x65,
                0x00, 0x01, 0x30, 0x00, 0x02, 0x33, 0x00, 0x03, 0x66, 0x61, 0x6c, 0x73, 0x65, 0x00,
                0x04, 0x33, 0x37, 0x35, 0x32, 0x38, 0x00, 0x05, 0x36, 0x35, 0x35, 0x38, 0x30, 0x00,
                0x06, 0x49, 0x74, 0x65, 0x6d, 0x20, 0x6f, 0x6e, 0x65, 0x00, (byte) 0xff };
        Record expected = new RecordImpl(expectedRecordData);
        Record record = database.getRecords().get(4);
        assertEquals(expected, record);

        // RecordHeader
        assertEquals(new UInt8(2), record.getHeader().getCategory());

        // SortInfo
        assertNull(database.getSortInfo());
    }
}
