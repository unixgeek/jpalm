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

import java.util.ArrayList;

import net.sourceforge.jpalm.palmdb.ApplicationInfo;
import net.sourceforge.jpalm.palmdb.Header;
import net.sourceforge.jpalm.palmdb.PalmDBImpl;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;
import net.sourceforge.jpalm.palmdb.SortInfo;

import junit.framework.TestCase;

public class TestPalmDBImpl extends TestCase {
    private PalmDBImpl database;

    protected void setUp() throws Exception {
        super.setUp();
        database = new PalmDBImpl();
    }

    public void testGetApplicationInfo() {
        ApplicationInfo info = new ApplicationInfo();
        database.applicationInfo = info;
        assertEquals(info, database.getApplicationInfo());
    }

    public void testSetApplicationInfo() {
        ApplicationInfo info = new ApplicationInfo();
        database.setApplicationInfo(info);
        assertEquals(info, database.applicationInfo);
    }

    public void testGetHeader() {
        Header header = new Header();
        database.header = header;
        assertEquals(header, database.getHeader());
    }

    public void testSetHeader() {
        Header header = new Header();
        database.setHeader(header);
        assertEquals(header, database.header);
    }

    public void testGetRecords() {
        ArrayList<Record> records = new ArrayList<Record>();
        records.add(new RecordImpl("test".getBytes()));
        database.records = records;
        assertEquals(records, database.getRecords());
    }

    public void testSetRecords() {
        ArrayList<Record> records = new ArrayList<Record>();
        records.add(new RecordImpl("test".getBytes()));
        database.setRecords(records);
        assertEquals(records, database.records);
    }

    public void testGetSortInfo() {
        SortInfo info = new SortInfo("test".getBytes());
        database.sortInfo = info;
        assertEquals(info, database.getSortInfo());
    }

    public void testSetSortInfo() {
        SortInfo info = new SortInfo("test".getBytes());
        database.setSortInfo(info);
        assertEquals(info, database.sortInfo);
    }
}
