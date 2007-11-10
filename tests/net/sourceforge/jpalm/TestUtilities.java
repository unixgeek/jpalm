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

import net.sourceforge.jpalm.Utilities;
import junit.framework.TestCase;

public class TestUtilities extends TestCase {
    public void testSubbyte() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        byte[] expected = { 0x03, 0x04, 0x05 };
        byte[] actual = Utilities.subbyte(data, 2, 3);
        assertTrue(Utilities.isEqual(expected, actual));
    }

    public void testIndexOf() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        int actual = Utilities.indexOf(data, 1, (byte) 0x05);
        assertEquals(4, actual);
    }

    public void testIndexOfNotFound() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        int actual = Utilities.indexOf(data, 1, (byte) 0x07);
        assertEquals(-1, actual);
    }

    public void testIndexOutOfBounds() {
        byte[] data = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        int actual = Utilities.indexOf(data, 10, (byte) 0x05);
        assertEquals(-1, actual);
    }

    public void testIsMatch() {
        assertTrue(Utilities.isMatch("tests are cool and stuff", ".*stuff$"));
    }

    public void testIsMatchFalse() {
        assertFalse(Utilities.isMatch("tests are cool and stuff", "^none"));
    }

    public void testIsEqualDifferentSize() {
        byte[] one = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        byte[] two = { 0x01, 0x02, 0x03 };
        assertFalse(Utilities.isEqual(one, two));
    }

    public void testIsEqualNotEqual() {
        byte[] one = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 };
        byte[] two = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x07 };
        assertFalse(Utilities.isEqual(one, two));
    }

    public void testByteArrayToHexString() {
        byte[] data = new byte[4];
        data[0] = 0x01;
        data[1] = (byte) 0xac;
        data[2] = (byte) 0xff;
        data[3] = (byte) 0xbd;
        String hexString = Utilities.byteArrayToHexString(data);
        assertEquals("01acffbd", hexString);
    }

    public void testByteToHex() {
        byte data = 0x0a;
        String hexString = Utilities.byteToHexString(data);
        assertEquals("0a", hexString);
    }
}
