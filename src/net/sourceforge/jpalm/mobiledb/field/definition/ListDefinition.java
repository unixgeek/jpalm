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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jpalm.mobiledb.field.type.ListOption;

/**
 * A list field in a MobileDB record.
 * <p>
 * Use {@link ListOption} with this field.
 */
public class ListDefinition implements Definition {
    private List<ListOption> options;

    /**
     * Creates a new list without any options.
     */
    public ListDefinition() {

    }

    /**
     * Creates a new list using the specified options.
     * 
     * @param options
     *            the options
     */
    public ListDefinition(List<ListOption> options) {
        setOptions(options);
    }

    /**
     * Creates a new list from the string representation for this type.
     * 
     * @param indicator
     *            the string representation
     */
    public ListDefinition(String indicator) {
        String[] tokens = indicator.substring(1).split(";");
        options = new ArrayList<ListOption>(tokens.length);
        for (int i = 0; i != tokens.length; i++) {
            options.add(new ListOption(tokens[i]));
        }
    }

    public String getIndicator() {
        StringBuffer string = new StringBuffer("L");
        for (ListOption option : getOptions()) {
            string.append(option.getValue());
            string.append(";");
        }
        // Remove the last ";".
        int index = string.lastIndexOf(";");
        if (index != -1)
            string.delete(index, string.length() + 1);

        return string.toString();
    }

    public String getRegex() {
        return "(^L|^L([a-zA-Z0-9 ]+;)*[a-zA-Z0-9 ]+$)";
    }

    /**
     * Gets the options in this list.
     * 
     * @return the options
     */
    public List<ListOption> getOptions() {
        if (options != null)
            return options;
        else
            return new ArrayList<ListOption>();
    }

    /**
     * Sets the options in this list.
     * 
     * @param options
     *            the options
     */
    public void setOptions(List<ListOption> options) {
        this.options = options;
    }
}
