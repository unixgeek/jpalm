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

import net.sourceforge.jpalm.BaseTestFromResource;
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.juint.UInt8;

public class TestApplicationInfo extends BaseTestFromResource {
    private ApplicationInfo info1;
    private ApplicationInfo info2;

    public TestApplicationInfo() {
        // The offset of ApplicationInfo in RESOURCE.
        offset = 152;
        /*
         * ApplicationInfo block ends at the offset of the first record in RESOURCE and 580 is the
         * offset of the first record.
         */
        length = 580 - (int) offset;
    }

    protected void setUp() throws Exception {
        super.setUp();
        info1 = new ApplicationInfo(data);
        info2 = new ApplicationInfo();
    }

    public void testGetCategoryLabels() {
        assertEquals("DataType", info1.getCategoryLabels().get(5));
        assertEquals("", info1.getCategoryLabels().get(12));
    }

    public void testSetCategoryLabels() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("One");
        labels.add(null);
        labels.add("012345678901234");

        info2.setCategoryLabels(labels);

        ArrayList<String> expected = new ArrayList<String>();
        expected.add("One");
        expected.add("");
        expected.add("012345678901234");

        assertEquals(expected, info2.categoryLabels);
    }

    public void testSetCategoryLabelsTooMany() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("One");
        labels.add(null);
        labels.add("I'm longer than 16 characters");
        for (int i = 0; i != 13; i++) {
            labels.add("");
        }
        labels.add("Beyond The Limit");

        info2.setCategoryLabels(labels);

        ArrayList<String> expected = new ArrayList<String>();
        expected.add("One");
        expected.add("");
        expected.add("I'm longer than");
        for (int i = 0; i != 13; i++) {
            expected.add("");
        }

        assertEquals(expected, info2.categoryLabels);
    }

    public void testGetCategoryUniqueIds() {
        assertEquals(3, info1.getCategoryUniqueIds().get(3).byteValue());
        assertEquals(15, info1.getCategoryUniqueIds().get(15).byteValue());
    }

    public void testSetCategoryUniqeIds() {
        ArrayList<UInt8> ids = new ArrayList<UInt8>();
        ids.add(new UInt8(10));
        ids.add(null);
        ids.add(new UInt8(255));
        for (int i = 0; i != 12; i++) {
            ids.add(null);
        }
        ids.add(new UInt8(16));
        ids.add(new UInt8(17));

        info2.setCategoryUniqueIds(ids);

        ArrayList<UInt8> expected = new ArrayList<UInt8>();
        expected.add(new UInt8(10));
        expected.add(null);
        expected.add(new UInt8(255));
        for (int i = 0; i != 12; i++) {
            expected.add(null);
        }
        expected.add(new UInt8(16));

        assertEquals(expected, info2.categoryUniqueIds);
    }

    public void testGetLastUniqueId() {
        assertEquals(15, info1.getLastUniqueId().byteValue());
    }

    public void testGetPadding() {
        assertEquals(0, info1.getPadding().uint8Value());
    }

    public void testGetRenamedCategories() {
        assertEquals(0, info1.getRenamedCategories().uint16Value());
    }

    public void testGetApplicationData() {
        byte[] expected = { 0x01, 0x02, 0x03, 0x04 };
        info2.applicationData = expected;
        byte[] actual = info2.getApplicationData();
        assertTrue(Utilities.isEqual(expected, actual));
    }

    public void testSetApplicationData() {
        byte[] expected = { 0x01, 0x02, 0x03, 0x04 };
        info2.setApplicationData(expected);
        assertTrue(Utilities.isEqual(expected, info2.applicationData));
    }

    // This tests serialize() and deserialize().
    public void testEquals() {
        ApplicationInfo info3 = new ApplicationInfo(data);
        assertEquals(info3, info1);
    }

    public void testEqualsNotApplicationInfo() {
        assertFalse(info2.equals(new Object()));
    }
}
