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
package net.sourceforge.jpalm.mobiledb.field.type;

import java.util.GregorianCalendar;

import net.sourceforge.jpalm.mobiledb.field.type.Time;
import junit.framework.TestCase;

public class TestTime extends TestCase {
    public void testPalmTime() {
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
        int minute = calendar.get(GregorianCalendar.MINUTE);
        int second = calendar.get(GregorianCalendar.SECOND);
        int seconds = (hour * 60 * 60) + (minute * 60) + second;
        Time time = new Time();
        assertTrue(time.getSeconds() >= seconds);
    }

    public void testPalmTimeInt() {
        Time time = new Time(100);
        assertEquals(100, time.getSeconds());
    }

    public void testGetSeconds() {
        Time time = new Time(50000);
        assertEquals(50000, time.getSeconds());
    }

    public void testCompareTo() {
        Time one = new Time(100);
        Time two = new Time(500);
        assertTrue((one.compareTo(two) < 0));
    }

    public void testToString() {
        Time time = new Time(65580);
        assertEquals("18:13", time.toString());
    }

    public void testToStringPadded() {
        Time time = new Time(22080);
        assertEquals("06:08", time.toString());
    }

    public void testToStringZero() {
        Time time = new Time(0);
        assertEquals("00:00", time.toString());
    }

    public void testToStringMax() {
        Time time = new Time(Time.MAX_SECONDS);
        assertEquals("23:59", time.toString());
    }

    public void testToStringMin() {
        Time time = new Time(Time.MIN_SECONDS);
        assertEquals("00:00", time.toString());
    }

    public void testToStringTooSmall() {
        Time time = new Time(Integer.MIN_VALUE);
        assertEquals("00:00", time.toString());
    }

    public void testToStringTooBig() {
        Time time = new Time(Integer.MAX_VALUE);
        assertEquals("00:00", time.toString());
    }

    public void testToMobileDB() {
        assertEquals("300", new Time(300).toMobileDB());
    }

    public void testFromMobileDB() {
        assertEquals(new Time(300), Time.fromMobileDB("300"));
        assertEquals(new Time(0), Time.fromMobileDB(null));
    }

    public void testGetValue() {
        Time time = new Time(300);
        assertEquals(time, time.getValue());
    }

    public void testEquals() {
        assertFalse(new Time(0).equals(new Object()));
    }
}
