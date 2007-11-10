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

import net.sourceforge.jpalm.mobiledb.field.type.Checkbox;

/**
 * A checkbox field in a MobileDB record.
 * <p>
 * Use {@link Checkbox} with this field.
 */
public class CheckboxDefinition implements Definition {
    private boolean defaultToTrue;

    /**
     * Creates a new checkbox that defaults to false (unchecked).
     */
    public CheckboxDefinition() {
        defaultToTrue = false;
    }

    /**
     * Creates a new checkbox.
     * 
     * @param defaultToTrue
     *            <code>true</code> if the checkbox should default to true (checked);
     *            <code>false</code> otherwise
     */
    public CheckboxDefinition(boolean defaultToTrue) {
        setDefaultToTrue(defaultToTrue);
    }

    /**
     * Creates a new checkbox from the string representation for this type.
     * 
     * @param indicator
     *            the string representation
     */
    public CheckboxDefinition(String indicator) {
        if (indicator.equals("B!"))
            defaultToTrue = true;
        else
            defaultToTrue = false;
    }

    public String getIndicator() {
        if (defaultToTrue)
            return "B!";
        else
            return "B";
    }

    public String getRegex() {
        return "^B!?$";
    }

    /**
     * Gets the <code>defaultToTrue</code> property.
     * 
     * @return <code>true</code> if the checkbox should default to true (checked);
     *         <code>false</code> otherwise
     */
    public boolean isDefaultToTrue() {
        return defaultToTrue;
    }

    /**
     * Sets the <code>defaultToTrue</code> property.
     * 
     * @param defaultToTrue
     *            <code>true</code> if the checkbox should default to true (checked);
     *            <code>false</code> otherwise
     */
    public void setDefaultToTrue(boolean defaultToTrue) {
        this.defaultToTrue = defaultToTrue;
    }
}
