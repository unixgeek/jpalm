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

import java.text.SimpleDateFormat;

import junit.framework.TestCase;
import net.sourceforge.jpalm.palmdb.Date;
import net.sourceforge.juint.UInt32;

public class TestDate extends TestCase {
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testDatePalmSeconds() {
        assertEquals(new UInt32(0), new Date(new UInt32(0)).getSeconds());
    }

    public void testDateJavaDate() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        java.util.Date expected = format.parse("1904-01-01 00:00:00 GMT");
        assertEquals(expected, new Date(expected).getDate());
    }

    public void testDate() throws Exception {
        long mark = System.currentTimeMillis();
        Thread.sleep(1000);
        assertTrue(new Date().getDate().getTime() >= mark);
    }

    public void testEquals() {
        assertEquals(new Date(new UInt32(10)), new Date(new UInt32(10)));
    }

    public void testEqualsNotDate() {
        assertFalse(new Date(new UInt32(10)).equals(new UInt32(10)));
    }

    public void testDatePalmEpochSeconds() {
        Date date = new Date(new java.util.Date((Date.PALM_EPOCH_MILLISECONDS)));
        assertEquals(new UInt32(0), date.getSeconds());
    }
}
