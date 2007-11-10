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

import junit.framework.TestCase;
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.jpalm.mobiledb.field.definition.SequenceDefinition;

public class TestSequenceDefinition extends TestCase {

    public void testSequence() {
        SequenceDefinition sequenceDefinition = new SequenceDefinition();
        assertEquals(Double.valueOf(1), sequenceDefinition.getInitialValue());
        assertEquals(Double.valueOf(1), sequenceDefinition.getIncrement());
        assertEquals("I1;1", sequenceDefinition.getIndicator());
    }

    public void testSequenceString() {
        SequenceDefinition sequenceDefinition = new SequenceDefinition("I0;1");
        assertEquals(Double.valueOf(0), sequenceDefinition.getInitialValue());
        assertEquals(Double.valueOf(1), sequenceDefinition.getIncrement());
        assertEquals("I0;1", sequenceDefinition.getIndicator());
    }

    public void testGetIndicator() {
        SequenceDefinition sequenceDefinition = new SequenceDefinition();
        sequenceDefinition.setInitialValue(1d);
        sequenceDefinition.setIncrement(1d);
        assertEquals("I1;1", sequenceDefinition.getIndicator());
    }

    public void testGetRegex() {
        String regex = new SequenceDefinition().getRegex();

        assertFalse("1", Utilities.isMatch("I", regex));
        assertFalse("2", Utilities.isMatch("IF", regex));
        assertFalse("3", Utilities.isMatch(" I ", regex));

        assertTrue("4", Utilities.isMatch("I;", regex));
        assertTrue("5", Utilities.isMatch("I5;5", regex));
        assertTrue("6", Utilities.isMatch("I;5", regex));
        assertTrue("7", Utilities.isMatch("I1.1;1.5", regex));
        assertTrue("8", Utilities.isMatch("I.5;5", regex));
        assertTrue("9", Utilities.isMatch("I5;", regex));
    }
}
