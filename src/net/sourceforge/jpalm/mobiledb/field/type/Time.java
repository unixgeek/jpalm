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

/**
 * A time field in a MobileDB record.
 */
public class Time implements Type<Time>, Comparable<Time> {
    /**
     * The maximum number of seconds.<br>
     * {@value}
     */
    public static final int MAX_SECONDS = 86399;
    /**
     * The minimum number of seconds.<br>
     * {@value}
     */
    public static final int MIN_SECONDS = 0;
    private int seconds;

    /**
     * Creates a new time representing the current time.
     */
    public Time() {
        GregorianCalendar calendar = new GregorianCalendar();
        int hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
        int minute = calendar.get(GregorianCalendar.MINUTE);
        int second = calendar.get(GregorianCalendar.SECOND);
        seconds = (hour * 60 * 60) + (minute * 60) + second;
    }

    /**
     * Creates a new time from <code>seconds</code>. If <code>seconds</code> is not between
     * {@value #MIN_SECONDS} and {@value #MAX_SECONDS} (inclusive) then <code>seconds</code>
     * defaults to {@value #MIN_SECONDS}.
     * 
     * @param seconds
     *            the number of seconds since 00:00.
     */
    public Time(int seconds) {
        this.seconds = ((MIN_SECONDS <= seconds) && (seconds <= MAX_SECONDS)) ? seconds
                : MIN_SECONDS;
    }

    /**
     * Gets this time as the number of seconds since 00:00.
     * 
     * @return the number of seconds since 00:00
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Gets a string representation of this time in HH:mm format.
     * 
     * @return the string representation of this time
     */
    @Override
    public String toString() {
        int hours = seconds / 3600;
        int minutes = (seconds - (hours * 3600)) / 60;

        String theHour = Integer.toString(hours);
        // Pad the string.
        if (theHour.length() == 1)
            theHour = "0" + theHour;

        String theMinute = Integer.toString(minutes);
        // Pad the string.
        if (theMinute.length() == 1)
            theMinute = "0" + theMinute;

        return theHour + ":" + theMinute;
    }

    @Override
    public boolean equals(Object object) {
        if (!Time.class.isInstance(object))
            return false;
        Time time = (Time) object;
        return (time.getSeconds() == getSeconds());
    }

    /**
     * Compares this time to to another time based on the number of seconds since 00:00.
     * 
     * @param time
     *            a time to compare to
     * @return less than <code>0</code> if <code>time</code> is less than this time,
     *         <code>0</code> if <code>time</code> is equal to this time, or greater than
     *         <code>0</code> if <code>time</code> is greater than this time
     */
    public int compareTo(Time time) {
        return seconds - time.getSeconds();
    }

    public String toMobileDB() {
        return Integer.toString(seconds);
    }

    /**
     * Creates a time from the MobileDB value.
     * 
     * @param string
     *            the MobileDB value to convert
     * @return a time
     */
    public static Time fromMobileDB(String string) {
        if (string == null)
            return new Time(0);

        return new Time(Integer.valueOf(string));
    }

    public Time getValue() {
        return this;
    }
}
