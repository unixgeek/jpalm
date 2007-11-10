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
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.jpalm.palmdb.RecordHeader;
import net.sourceforge.juint.UInt32;
import net.sourceforge.juint.UInt8;

public class TestRecordHeader extends BaseTestFromResource {
    // Records in the current RESOURCE.
    protected static final int NUMBER_OF_RECORDS = 9;
    protected RecordHeader[] recordHeaders;

    public TestRecordHeader() {
        // Record list begins after header.
        offset = 78;
        length = NUMBER_OF_RECORDS * RecordHeader.DATA_LENGTH;
    }

    protected void setUp() throws Exception {
        super.setUp();
        recordHeaders = new RecordHeader[NUMBER_OF_RECORDS];
        for (int i = 0; i != NUMBER_OF_RECORDS; i++) {
            recordHeaders[i] = new RecordHeader(Utilities.subbyte(data, i
                    * RecordHeader.DATA_LENGTH, RecordHeader.DATA_LENGTH));
        }
    }

    public void testGetAttributes() {
        assertTrue(recordHeaders[0].getAttributes().hasBitmask(RecordHeader.RECORD_DIRTY));
    }

    public void testSetAttributes() {
        RecordHeader header = new RecordHeader();
        UInt8 attributes = RecordHeader.RECORD_SECRET.or(RecordHeader.RECORD_DIRTY);
        header.setAttributes(attributes);
        assertEquals(attributes, header.attributes);
    }

    public void testAddAttribute() {
        RecordHeader header = new RecordHeader();
        UInt8 attributes = RecordHeader.RECORD_SECRET.addBitmask(RecordHeader.RECORD_DIRTY);
        header.attributes = attributes;
        header.addAttribute(RecordHeader.RECORD_DELETE);
        assertTrue(header.attributes.hasBitmask(RecordHeader.RECORD_DELETE));
    }

    public void testRemoveAttribute() {
        RecordHeader header = new RecordHeader();
        UInt8 attributes = RecordHeader.RECORD_SECRET.or(RecordHeader.RECORD_DIRTY);
        header.attributes = attributes;
        assertTrue(header.attributes.hasBitmask(RecordHeader.RECORD_DIRTY));
        header.removeAttribute(RecordHeader.RECORD_DIRTY);
        assertFalse(header.attributes.hasBitmask(RecordHeader.RECORD_DIRTY));
    }

    public void testGetCategory() {
        assertEquals(new UInt8(1), recordHeaders[0].getCategory());
    }

    public void testSetCategory() {
        RecordHeader header = new RecordHeader();
        header.setCategory(new UInt8(14));
        assertEquals(new UInt8(14), header.attributes.and(RecordHeader.CATEGORY_ATTRIBUTES));
    }

    public void testGetOffset() {
        assertEquals(766, recordHeaders[2].getOffset().uint32Value());
    }

    public void testGetUniqueId() {
        assertEquals(7393282L, recordHeaders[3].getUniqueId().uint32Value());
    }

    public void testSetUniqueId() {
        RecordHeader header = new RecordHeader();
        header.setUniqueId(new UInt32(1000));
        assertEquals(1000, header.uniqueId.uint32Value());
    }

    public void testSerialze() {
        byte[] bytes = new byte[NUMBER_OF_RECORDS * RecordHeader.DATA_LENGTH];
        for (int i = 0; i != NUMBER_OF_RECORDS; i++) {
            System.arraycopy(recordHeaders[i].serialize(), 0, bytes, i * RecordHeader.DATA_LENGTH,
                    RecordHeader.DATA_LENGTH);
        }

        for (int i = 0; i != bytes.length; i++) {
            assertEquals(data[i], bytes[i]);
        }
    }

    public void testEqualsNotInstanceof() {
        assertFalse(recordHeaders[0].equals(new Object()));
    }
}
