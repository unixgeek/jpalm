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

import net.sourceforge.jpalm.Utilities;
import net.sourceforge.jpalm.palmdb.RecordHeader;
import net.sourceforge.jpalm.palmdb.RecordImpl;
import junit.framework.TestCase;

// Prove to myself that Record is immutable.
public class TestRecordImpl extends TestCase {
    private byte[] data = "test".getBytes();
    private RecordImpl record;

    protected void setUp() throws Exception {
        super.setUp();
        record = new RecordImpl(data);
    }

    public void testRecordImpl() {
        record = new RecordImpl();
        byte[] expected = { 0x00 };
        assertTrue(Utilities.isEqual(expected, record.data));
    }

    public void testGetRecordHeader() {
        RecordHeader header = new RecordHeader();
        record.header = header;
        assertEquals(header, record.getHeader());
    }

    public void testSetRecordHeader() {
        RecordHeader header = new RecordHeader();
        record.setHeader(header);
        assertEquals(header, record.header);
    }

    public void testRecordImplByteArray() {
        byte[] expected = "test".getBytes();
        assertTrue(Utilities.isEqual(expected, record.data));
    }

    public void testSerialize() {
        byte[] expected = "test".getBytes();
        assertTrue(Utilities.isEqual(expected, record.serialize()));
    }

    public void testDeserialize() {
        byte[] expected = "test".getBytes();
        record.deserialize(data);
        assertTrue(Utilities.isEqual(expected, record.data));
    }

    public void testToString() {
        assertEquals("74657374", record.toString());
    }

    public void testEqualsNotInstanceof() {
        assertFalse(record.equals(new Object()));
    }
}
