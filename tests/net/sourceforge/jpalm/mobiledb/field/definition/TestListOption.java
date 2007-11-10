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

import junit.framework.TestCase;
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.jpalm.mobiledb.field.definition.ListDefinition;
import net.sourceforge.jpalm.mobiledb.field.type.ListOption;

public class TestListOption extends TestCase {
    public void testList() {
        ListDefinition listDefinition = new ListDefinition();
        assertEquals(0, listDefinition.getOptions().size());
        assertEquals("L", listDefinition.getIndicator());
    }

    public void testListString() {
        ListDefinition listDefinition = new ListDefinition("Lone;two;three");
        assertEquals(3, listDefinition.getOptions().size());
        assertTrue(listDefinition.getOptions().contains(new ListOption("one")));
        assertTrue(listDefinition.getOptions().contains(new ListOption("two")));
        assertTrue(listDefinition.getOptions().contains(new ListOption("three")));
        assertEquals("Lone;two;three", listDefinition.getIndicator());
    }

    public void testGetIndicator() {
        ListDefinition listDefinition = new ListDefinition();
        ArrayList<ListOption> options = new ArrayList<ListOption>(3);
        options.add(new ListOption("foo"));
        options.add(new ListOption("bar"));
        options.add(new ListOption("baz"));
        listDefinition.setOptions(options);

        assertEquals(3, listDefinition.getOptions().size());
        assertTrue(listDefinition.getOptions().contains(new ListOption("foo")));
        assertTrue(listDefinition.getOptions().contains(new ListOption("bar")));
        assertTrue(listDefinition.getOptions().contains(new ListOption("baz")));
        assertEquals("Lfoo;bar;baz", listDefinition.getIndicator());
    }

    public void testGetRegex() {
        String regex = new ListDefinition().getRegex();

        assertTrue("1", Utilities.isMatch("Lone", regex));
        assertTrue("2", Utilities.isMatch("Lone;two;three", regex));
        assertTrue("3", Utilities.isMatch("Langie daddy;joe momma; john deer", regex));
        assertFalse("4", Utilities.isMatch("Lsomething;", regex));
        assertTrue("5", Utilities.isMatch("L", regex));
        assertFalse("6", Utilities.isMatch("L;", regex));
        assertFalse("7", Utilities.isMatch("one;two;three;", regex));
    }
}
