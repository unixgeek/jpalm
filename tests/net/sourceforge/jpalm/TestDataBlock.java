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
package net.sourceforge.jpalm;

import net.sourceforge.jpalm.DataBlock;
import junit.framework.TestCase;

public class TestDataBlock extends TestCase {
    private DataBlock block;

    public TestDataBlock(String name) {
        super(name);
    }

    private class Dummy extends DataBlock {
        private byte[] data;

        @Override
        public void deserialize(byte[] data) {
            this.data = data.clone();
        }

        @Override
        public byte[] serialize() {
            return data.clone();
        }
    }

    public void setUp() throws Exception {
        super.setUp();
        byte[] dummyData = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        block = new Dummy();
        block.deserialize(dummyData);
    }

    public void testEqualsObject() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 };
        DataBlock newBlock = new Dummy();
        newBlock.deserialize(data);
        assertEquals(block, newBlock);
    }

    public void testEqualsObjectNotEquals() {
        byte[] data = { 0x01, 0x02, 0x03, 0x05, 0x04, 0x06, 0x07 };
        DataBlock newBlock = new Dummy();
        newBlock.deserialize(data);
        assertFalse(block.equals(newBlock));
    }

    public void testEqualsObjectNotDataBlock() {
        assertFalse(block.equals(new Object()));
    }

    public void testToString() {
        assertEquals("01020304050607", block.toString());
    }
}
