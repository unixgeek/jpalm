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

import java.text.DecimalFormat;

import net.sourceforge.jpalm.mobiledb.field.type.Number;

/**
 * A sequence field in a MobileDB record.
 * <p>
 * Use {@link Number} with this field.
 */
public class SequenceDefinition implements Definition {
    private DecimalFormat formatter;
    private Double initialValue;
    private Double increment;

    /**
     * Creates a new sequence type with an initial value of 1 and an increment of 1.
     */
    public SequenceDefinition() {
        this(1, 1);
    }

    /**
     * Creates a new sequence type.
     * 
     * @param initialValue
     *            the initial value
     * @param increment
     *            the increment
     */
    public SequenceDefinition(Float initialValue, Float increment) {
        this(initialValue.doubleValue(), increment.doubleValue());
    }

    /**
     * Creates a new sequence type.
     * 
     * @param initialValue
     *            the initial value
     * @param increment
     *            the increment
     */
    public SequenceDefinition(Double initialValue, Double increment) {
        this.initialValue = initialValue;
        this.increment = increment;
        formatter = new DecimalFormat();
        formatter.setDecimalSeparatorAlwaysShown(false);
    }

    /**
     * Creates a new sequence type.
     * 
     * @param initialValue
     *            the initial value
     * @param increment
     *            the increment
     */
    public SequenceDefinition(Byte initialValue, Byte increment) {
        this(initialValue.doubleValue(), increment.doubleValue());
    }

    /**
     * Creates a new sequence type.
     * 
     * @param initialValue
     *            the initial value
     * @param increment
     *            the increment
     */
    public SequenceDefinition(Short initialValue, Short increment) {
        this(initialValue.doubleValue(), increment.doubleValue());
    }

    /**
     * Creates a new sequence type.
     * 
     * @param initialValue
     *            the initial value
     * @param increment
     *            the increment
     */
    public SequenceDefinition(Integer initialValue, Integer increment) {
        this(initialValue.doubleValue(), increment.doubleValue());
    }

    /**
     * Creates a new sequence type.
     * 
     * @param initialValue
     *            the initial value
     * @param increment
     *            the increment
     */
    public SequenceDefinition(Long initialValue, Long increment) {
        this(initialValue.doubleValue(), increment.doubleValue());
    }

    /**
     * Creates a new sequence type from the string representation for this type.
     * 
     * @param indicator
     *            the string representation
     */
    public SequenceDefinition(String indicator) {
        formatter = new DecimalFormat();
        formatter.setDecimalSeparatorAlwaysShown(false);
        String[] tokens = indicator.substring(1).split(";");
        initialValue = Double.valueOf(tokens[0]);
        increment = Double.valueOf(tokens[1]);
    }

    public String getIndicator() {
        String initialValueAsString = "";
        if (initialValue != null)
            initialValueAsString = formatter.format(initialValue);

        String incrementAsString = "";
        if (increment != null)
            incrementAsString = formatter.format(increment);
        return "I" + initialValueAsString + ";" + incrementAsString;
    }

    public String getRegex() {
        return "^I\\d*\\.?\\d*;\\d*\\.?\\d*$";
    }

    /**
     * Gets the increment.
     * 
     * @return the increment
     */
    public Double getIncrement() {
        return increment;
    }

    /**
     * Sets the increment.
     * 
     * @param increment
     *            the increment
     */
    public void setIncrement(Double increment) {
        this.increment = increment;
    }

    /**
     * Sets the increment.
     * 
     * @param increment
     *            the increment
     */
    public void setIncrement(Float increment) {
        this.increment = increment.doubleValue();
    }

    /**
     * Sets the increment.
     * 
     * @param increment
     *            the increment
     */
    public void setIncrement(Byte increment) {
        this.increment = increment.doubleValue();
    }

    /**
     * Sets the increment.
     * 
     * @param increment
     *            the increment
     */
    public void setIncrement(Short increment) {
        this.increment = increment.doubleValue();
    }

    /**
     * Sets the increment.
     * 
     * @param increment
     *            the increment
     */
    public void setIncrement(Integer increment) {
        this.increment = increment.doubleValue();
    }

    /**
     * Sets the increment.
     * 
     * @param increment
     *            the increment
     */
    public void setIncrement(Long increment) {
        this.increment = increment.doubleValue();
    }

    /**
     * Gets the initial value.
     * 
     * @return the initial value
     */
    public Double getInitialValue() {
        return initialValue;
    }

    /**
     * Sets the initial value.
     * 
     * @param initialValue
     *            the initial value
     */
    public void setInitialValue(Double initialValue) {
        this.initialValue = initialValue;
    }

    /**
     * Sets the initial value.
     * 
     * @param initialValue
     *            the initial value
     */
    public void setInitialValue(Float initialValue) {
        this.initialValue = initialValue.doubleValue();
    }

    /**
     * Sets the initial value.
     * 
     * @param initialValue
     *            the initial value
     */
    public void setInitialValue(Byte initialValue) {
        this.initialValue = initialValue.doubleValue();
    }

    /**
     * Sets the initial value.
     * 
     * @param initialValue
     *            the initial value
     */
    public void setInitialValue(Short initialValue) {
        this.initialValue = initialValue.doubleValue();
    }

    /**
     * Sets the initial value.
     * 
     * @param initialValue
     *            the initial value
     */
    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue.doubleValue();
    }

    /**
     * Sets the initial value.
     * 
     * @param initialValue
     *            the initial value
     */
    public void setInitialValue(Long initialValue) {
        this.initialValue = initialValue.doubleValue();
    }
}
