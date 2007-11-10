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

import java.text.DecimalFormat;

/**
 * A number in a MobileDB record.
 */
public class Number extends BaseType<Double> implements Type<Double> {
    private DecimalFormat formatter;

    /**
     * Creates a new number representing "".
     */
    public Number() {
        formatter = new DecimalFormat();
        formatter.setDecimalSeparatorAlwaysShown(false);
    }

    /**
     * Creates a new number.
     * 
     * @param number
     *            the number
     */
    public Number(Double number) {
        this();
        value = number;
    }

    /**
     * Creates a new number.
     * 
     * @param number
     *            the number
     */
    public Number(Float number) {
        this(number.doubleValue());
    }

    /**
     * Creates a new number.
     * 
     * @param number
     *            the number
     */
    public Number(Byte number) {
        this(number.doubleValue());
    }

    /**
     * Creates a new number.
     * 
     * @param number
     *            the number
     */
    public Number(Short number) {
        this(number.doubleValue());
    }

    /**
     * Creates a new number.
     * 
     * @param number
     *            the number
     */
    public Number(Integer number) {
        this(number.doubleValue());
    }

    /**
     * Creates a new number.
     * 
     * @param number
     *            the number
     */
    public Number(Long number) {
        this(number.doubleValue());
    }

    public String toMobileDB() {
        if (value == null)
            return "";
        return formatter.format(value);
    }

    /**
     * Creates a number from the MobileDB value.
     * 
     * @param string
     *            the MobileDB value to convert
     * @return a number
     */
    public static Number fromMobileDB(String string) {
        if (string == null)
            return new Number();
        else
            return new Number(new Double(string));
    }

    @Override
    public String toString() {
        if (value != null)
            return formatter.format(value);
        else
            return "";
    }

    @Override
    public Double getValue() {
        return super.getValue();
    }
}
