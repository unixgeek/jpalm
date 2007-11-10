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
package net.sourceforge.jpalm.mobiledb.field.definition;

import net.sourceforge.jpalm.mobiledb.field.type.Date;

/**
 * A date field in a MobileDB record.
 * <p>
 * Use {@link Date} with this field.
 */
public class DateDefinition implements Definition {
    private boolean defaultToCurrentDate;

    /**
     * Creates a new date that does not default to the current date.
     */
    public DateDefinition() {
        defaultToCurrentDate = false;
    }

    /**
     * Creates a new date.
     * 
     * @param defaultToCurrentDate
     *            <code>true</code> if the field should default to the current date;
     *            <code>false</code> otherwise
     */
    public DateDefinition(boolean defaultToCurrentDate) {
        this.defaultToCurrentDate = defaultToCurrentDate;
    }

    /**
     * Creates a new date from the string representation for this type.
     * 
     * @param indicator
     *            the string representation
     */
    public DateDefinition(String indicator) {
        if (indicator.equals("D!"))
            defaultToCurrentDate = true;
        else
            defaultToCurrentDate = false;
    }

    public String getIndicator() {
        if (defaultToCurrentDate)
            return "D!";
        else
            return "D";
    }

    public String getRegex() {
        return "^D!?$";
    }

    /**
     * Gets the <code>defaultToCurrentDate</code> property.
     * 
     * @return <code>true</code> if the date should default to the current date;
     *         <code>false</code> if it should default to null
     */
    public boolean isDefaultToCurrentDate() {
        return defaultToCurrentDate;
    }

    /**
     * Sets the <code>defaultToCurrentDate</code> property.
     * 
     * @param defaultToCurrentDate
     *            <code>true</code> if the date should default to the current date;
     *            <code>false</code> if it should default to null
     */
    public void setDefaultToCurrentDate(boolean defaultToCurrentDate) {
        this.defaultToCurrentDate = defaultToCurrentDate;
    }
}
