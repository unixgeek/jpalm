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
package net.sourceforge.jpalm.mobiledb;

import net.sourceforge.jpalm.BaseTestFromResource;
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.jpalm.mobiledb.DatabaseInfo;
import net.sourceforge.jpalm.mobiledb.FilterCriterion;
import net.sourceforge.jpalm.mobiledb.SortCriterion;
import net.sourceforge.jpalm.palmdb.ApplicationInfo;
import net.sourceforge.juint.Int8;
import net.sourceforge.juint.UInt16;
import net.sourceforge.juint.UInt32;
import net.sourceforge.juint.UInt8;

public class TestDatabaseInfo extends BaseTestFromResource {
    protected DatabaseInfo info;

    public TestDatabaseInfo() {
        /*
         * The DatabaseInfo block is the application specific data found in the ApplicationInfo
         * block. The offset is after the first 276 bytes within the ApplicationInfo block and ends
         * at the offset of the first record in RESOURCE.
         */
        long applicationInfoOffset = 152;
        // offset = applicationInfoOffset + 276;
        offset = applicationInfoOffset;
        length = ApplicationInfo.DATA_LENGTH + DatabaseInfo.DATA_LENGTH;
    }

    protected void setUp() throws Exception {
        super.setUp();
        info = new DatabaseInfo(data);
    }

    public void testHashPassword() {
        UInt32 expected = new UInt32(192028703L);
        UInt32 result = info.hashPassword("gunter");
        assertEquals(expected, result);
    }

    public void testHashPasswordEmpty() {
        UInt32 expected = new UInt32(0);
        UInt32 result = info.hashPassword(null);
        assertEquals(expected, result);
        result = info.hashPassword("");
        assertEquals(expected, result);
    }

    public void testGetVersion() {
        UInt16 expected = new UInt16(3);
        assertEquals(expected, info.getVersion());
    }

    public void testGetPasswordHash() {
        UInt32 expected = new UInt32(192028703L);
        assertEquals(expected, info.getPasswordHash());
    }

    public void testIsSearchOnGlobalFind() {
        assertTrue(info.isSearchOnGlobalFind());
    }

    public void testIsEditOnSelect() {
        assertTrue(info.isEditOnSelect());
    }

    public void testIsDisplayLongDates() {
        assertTrue(info.isDisplayLongDates());
    }

    public void testGetLockedColumn() {
        assertEquals(new UInt8(0), info.getLockedColumn());
    }

    public void testGetPasswordProtection() {
        assertEquals(DatabaseInfo.PASSWORD_PROTECTION_NONE, info.getPasswordProtection());
    }

    public void testGetReserved() {
        byte[] expected = { 0x00, 0x00, 0x00 };

        assertTrue(Utilities.isEqual(expected, info.getReserved()));
    }

    public void testGetSetFilter1() {
        FilterCriterion filter = new FilterCriterion();
        filter.setFieldNumber(new Int8(1));
        info.setFilter1(filter);
        assertEquals(filter, info.getFilter1());
    }

    public void testGetSetFilter2() {
        FilterCriterion filter = new FilterCriterion();
        filter.setFieldNumber(new Int8(2));
        info.setFilter2(filter);
        assertEquals(filter, info.getFilter2());
    }

    public void testGetSetFilter3() {
        FilterCriterion filter = new FilterCriterion();
        filter.setFieldNumber(new Int8(1));
        info.setFilter3(filter);
        assertEquals(filter, info.getFilter3());
    }

    public void testGetSetSort1() {
        SortCriterion sort = new SortCriterion();
        sort.setFieldNumber(new Int8(1));
        info.setSort1(sort);
        assertEquals(sort, info.getSort1());
    }

    public void testGetSetSort2() {
        SortCriterion sort = new SortCriterion();
        sort.setFieldNumber(new Int8(2));
        info.setSort2(sort);
        assertEquals(sort, info.getSort2());
    }

    public void testGetSetSort3() {
        SortCriterion sort = new SortCriterion();
        sort.setFieldNumber(new Int8(3));
        info.setSort3(sort);
        assertEquals(sort, info.getSort3());
    }

    public void testGetFilterCriterionFromResource() {
        FilterCriterion filter = info.getFilter1();
        assertEquals(new Int8(8), filter.getFieldNumber());
        assertEquals("Item three", filter.getFilter());
        assertEquals(FilterCriterion.FLAG_CONTAINS.or(FilterCriterion.FLAG_MATCH_ANY), filter
                .getFlags());
    }

    // This tests serialize() and deserialize().
    public void testEquals() {
        DatabaseInfo info2 = new DatabaseInfo(data);
        assertEquals(info2, info);
    }
}
