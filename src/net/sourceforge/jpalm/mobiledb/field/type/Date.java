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

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import net.sourceforge.juint.UInt32;

/**
 * A date in a MobileDB record.
 * <p>
 * A date is stored as the number of days since the Palm epoch (January 1, 1904 00:00 GMT). When
 * displaying the date returned by {@link #getValue()}, the time part should be ignored and the GMT
 * timezone should be used.
 * <p>
 * Example:
 * 
 * <pre>
 * SimpleDateFormat dateFormat = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
 * dateFormat.setTimeZone(TimeZone.getTimeZone(&quot;GMT&quot;));
 * System.out.println(dateFormat.format(new Date().getValue()));
 * </pre>
 */
public class Date extends BaseType<java.util.Date> implements Type<java.util.Date> {

    /**
     * Creates a new date representing the specified date.
     * 
     * @param date
     *            the date
     */
    public Date(java.util.Date date) {
        this.value = date;
    }

    /**
     * Creates a new date representing the current date.
     */
    public Date() {
        this(new java.util.Date());
    }

    public String toMobileDB() {
        if (value == null)
            return "";

        /*
         * Convert the date to the number of seconds since the Palm epoch and then convert that to
         * days.
         */
        UInt32 seconds = new net.sourceforge.jpalm.palmdb.Date(value).getSeconds();
        long days = seconds.longValue() / 86400L;
        return Long.toString(days);
    }

    /**
     * Creates a date from the MobileDB value.
     * 
     * @param string
     *            the MobileDB value to convert
     * @return a date
     */
    public static Date fromMobileDB(String string) {
        if (string == null)
            return new Date(null);

        /*
         * string is the number of days since the Palm epoch; convert that to seconds and use the
         * pdb date class to convert it to a Java date.
         */

        long days = 0;
        try {
            days = Long.valueOf(string);
        }
        catch (NumberFormatException e) {
            return new Date(null);
        }
        // 86400 seconds in a day.
        return new Date(new net.sourceforge.jpalm.palmdb.Date(new UInt32(days * 86400L)).getDate());
    }

    /**
     * Gets a string representation of this date in yyyy-MM-dd format.
     * 
     * @return the string representation of this date
     */
    @Override
    public String toString() {
        if (value == null) {
            return "";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // MobileDB stores the date as the number of days since the epoch, GMT time.
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(value);
    }

    @Override
    public java.util.Date getValue() {
        return super.getValue();
    }
}
