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

/**
 * A checkbox in a MobileDB record.
 */
public class Checkbox extends BaseType<Boolean> implements Type<Boolean> {

    /**
     * Creates a new checkbox that is not checked.
     */
    public Checkbox() {
        this(Boolean.FALSE);
    }

    /**
     * Creates a new checkbox.
     * 
     * @param value
     *            <code>true</code> if the checkbox is checked; <code>false</code> otherwise
     */
    public Checkbox(Boolean value) {
        if (value == null)
            value = Boolean.FALSE;
        this.value = value;
    }

    /**
     * Creates a checkbox from the MobileDB value.
     * 
     * @param string
     *            the MobileDB value to convert
     * @return a checkbox
     */
    public static Checkbox fromMobileDB(String string) {
        if (string == null)
            string = "false";

        if ((string.equalsIgnoreCase("true")) || (string.equalsIgnoreCase("yes"))
                || (string.equalsIgnoreCase("ja")))
            return new Checkbox(Boolean.TRUE);
        else
            return new Checkbox(Boolean.FALSE);
    }

    public String toMobileDB() {
        return value.toString();
    }

    @Override
    public Boolean getValue() {
        return super.getValue();
    }
}
