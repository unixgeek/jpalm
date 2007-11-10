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

import net.sourceforge.jpalm.BaseTestFromResource;
import net.sourceforge.jpalm.palmdb.Date;
import net.sourceforge.jpalm.palmdb.Header;
import net.sourceforge.juint.UInt16;
import net.sourceforge.juint.UInt32;

public class TestHeader extends BaseTestFromResource {
    protected Header header1;
    protected Header header2;

    public TestHeader() {
        offset = 0;
        length = Header.DATA_LENGTH;
        header2 = new Header();
    }

    protected void setUp() throws Exception {
        super.setUp();
        header1 = new Header(data);
    }

    public void testGetApplicationInfoOffset() {
        assertEquals(152, header1.getApplicationInfoOffset().uint32Value());
    }

    public void testSetApplicationInfoOffset() {
        header2.setApplicationInfoOffset(new UInt32(100));
        assertEquals(100, header2.getApplicationInfoOffset().uint32Value());
    }

    public void testGetAttributes() {
        assertTrue(header1.getAttributes().hasBitmask(Header.ATTRIBUTE_BACKUP_DATABASE));
        assertTrue(header1.getAttributes().hasBitmask(Header.ATTRIBUTE_COPY_PREVENTION));
    }

    public void testSetAttributes() {
        header2.setAttributes(Header.ATTRIBUTE_OK_TO_INSTALL_NEWER);
        assertTrue(header2.attributes.hasBitmask(Header.ATTRIBUTE_OK_TO_INSTALL_NEWER));
    }

    public void testAddAttribute() {
        header2.attributes = Header.ATTRIBUTE_BACKUP_DATABASE.or(Header.ATTRIBUTE_BUNDLE);
        header2.addAttribute(Header.ATTRIBUTE_OK_TO_INSTALL_NEWER);
        assertTrue(header2.attributes.hasBitmask(Header.ATTRIBUTE_OK_TO_INSTALL_NEWER));
        assertTrue(header2.attributes.hasBitmask(Header.ATTRIBUTE_BUNDLE));
        assertTrue(header2.attributes.hasBitmask(Header.ATTRIBUTE_BACKUP_DATABASE));
    }

    public void testRemoveAttribute() {
        header2.attributes = Header.ATTRIBUTE_BACKUP_DATABASE
                .addBitmask(Header.ATTRIBUTE_COPY_PREVENTION);
        header2.removeAttribute(Header.ATTRIBUTE_COPY_PREVENTION);
        assertFalse(header2.attributes.hasBitmask(Header.ATTRIBUTE_COPY_PREVENTION));
        assertTrue(header2.attributes.hasBitmask(Header.ATTRIBUTE_BACKUP_DATABASE));
    }

    public void testGetCreationDate() {
        assertEquals(3240144649L, header1.getCreationDate().getSeconds().uint32Value());
    }

    public void testSetCreationDate() {
        header2.setCreationDate(new Date(new UInt32(1000)));
        assertEquals(1000, header2.creationDate.getSeconds().uint32Value());
    }

    public void testGetCreator() {
        assertEquals("Mdb1", header1.getCreator());
    }

    public void testSetCreator() {
        header2.setCreator("pdiddy");
        assertEquals("pdid", header2.creator);
    }

    public void testGetLastBackupDate() {
        assertEquals(0, header1.getLastBackupDate().getSeconds().uint32Value());
    }

    public void testSetLastBackupDate() {
        header2.setLastBackupDate(new Date(new UInt32(1000)));
        assertEquals(1000, header2.lastBackupDate.getSeconds().uint32Value());
    }

    public void testGetModificationDate() {
        assertEquals(3240145402L, header1.getModificationDate().getSeconds().uint32Value());
    }

    public void testSetModificationDate() {
        header2.setModificationDate(new Date(new UInt32(1000)));
        assertEquals(1000, header2.modificationDate.getSeconds().uint32Value());
    }

    public void testGetModificationNumber() {
        assertEquals(93, header1.getModificationNumber().uint32Value());
    }

    public void testSetModificationNumber() {
        header2.setModificationNumber(new UInt32(7));
        assertEquals(7, header2.modificationNumber.uint32Value());
    }

    public void testGetName() {
        assertEquals("test", header1.getName());
    }

    public void testSetName() {
        header2.setName("My Database of Stuff That Doesn't Exist");
        assertEquals("My Database of Stuff That Doesn", header2.name);
    }

    public void testGetNextRecordListOffset() {
        assertEquals(0, header1.getNextRecordListOffset().uint32Value());
    }

    public void testSetNextRecordListOffset() {
        header2.setNextRecordListOffset(new UInt32(30));
        assertEquals(30, header2.nextRecordListOffset.uint32Value());
    }

    public void testGetNumberOfRecords() {
        assertEquals(9, header1.getNumberOfRecords().uint16Value());
    }

    public void testSetNumberOfRecords() {
        header2.setNumberOfRecords(new UInt16(10));
        assertEquals(10, header2.numberOfRecords.uint16Value());
    }

    public void testGetSortInfoOffset() {
        assertEquals(0, header1.getSortInfoOffset().uint32Value());
    }

    public void testSetSortInfoOffset() {
        header2.setSortInfoOffset(new UInt32(107));
        assertEquals(107, header2.sortInfoOffset.uint32Value());
    }

    public void testGetType() {
        assertEquals("Mdb1", header1.getType());
    }

    public void testSetType() {
        header2.setType("sometype");
        assertEquals("some", header2.type);
    }

    public void testGetUniqueIdSeed() {
        assertEquals(0, header1.getUniqueIdSeed().uint32Value());
    }

    public void testSetUniqueIdSeed() {
        header2.setUniqueIdSeed(new UInt32(10000));
        assertEquals(10000, header2.uniqueIdSeed.longValue());
    }

    public void testGetVersion() {
        assertEquals(0, header1.getVersion().uint16Value());
    }

    public void testSetVersion() {
        header2.setVersion(new UInt16(2));
        assertEquals(2, header2.version.uint16Value());
    }

    // This tests serialize() and deserialize().
    public void testEquals() {
        Header header2 = new Header(data);
        assertEquals(header1, header2);
    }

    public void testEqualsNotInstanceof() {
        assertFalse(header1.equals(new Object()));
    }
}
