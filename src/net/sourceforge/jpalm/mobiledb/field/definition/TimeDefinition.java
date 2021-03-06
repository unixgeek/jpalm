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

import net.sourceforge.jpalm.mobiledb.field.type.Time;

/**
 * A time field in a MobileDB record.
 * <p>
 * Use {@link Time} with this field.
 */
public class TimeDefinition implements Definition {
    private boolean defaultToCurrentTime;

    /**
     * Creates a new time field that does not default to the current time.
     */
    public TimeDefinition() {
        defaultToCurrentTime = false;
    }

    /**
     * Creates a new time field.
     * 
     * @param defaultToCurrentTime
     *            <code>true</code> if the field should default to the current time;
     *            <code>false</code> otherwise
     */
    public TimeDefinition(boolean defaultToCurrentTime) {
        this.defaultToCurrentTime = defaultToCurrentTime;
    }

    /**
     * Creates a new time field from the string representation for this type.
     * 
     * @param indicator
     *            the string representation
     */
    public TimeDefinition(String indicator) {
        if (indicator.equals("d!"))
            defaultToCurrentTime = true;
        else
            defaultToCurrentTime = false;
    }

    public String getIndicator() {
        if (defaultToCurrentTime)
            return "d!";
        else
            return "d";
    }

    public String getRegex() {
        return "^d!?$";
    }

    /**
     * Gets the <code>defaultToCurrentTime</code> property.
     * 
     * @return <code>true</code> if the time should default to the current time;
     *         <code>false</code> if it should default to null
     */
    public boolean isDefaultToCurrentTime() {
        return defaultToCurrentTime;
    }

    /**
     * Sets the <code>defaultToCurrentTime</code> property.
     * 
     * @param defaultToCurrentDate
     *            <code>true</code> if the time should default to the current time;
     *            <code>false</code> if it should default to null
     */
    public void setDefaultToCurrentTime(boolean defaultToCurrentDate) {
        this.defaultToCurrentTime = defaultToCurrentDate;
    }
}
