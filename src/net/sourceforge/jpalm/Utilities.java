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
package net.sourceforge.jpalm;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for the jpalm package.
 */
public class Utilities {
    private Utilities() {

    }

    // TODO Wrap a byte array and move these methods there.
    /**
     * Converts a <code>byte</code> array to a hexadecimal string. Hexadecimal letters are
     * lowercase.
     * 
     * @param bytes
     *            a <code>byte</code> array to convert
     * @return a hexadecimal string
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i != bytes.length; i++) {
            hexString.append(byteToHexString(bytes[i]));
        }

        return hexString.toString();
    }

    /**
     * Converts a <code>byte</code> to a hexadecimal string. Hexadecimal letters are lowercase.
     * 
     * @param bite
     *            a <code>byte</code> to convert
     * @return a hexadecimal string
     */
    public static String byteToHexString(byte bite) {
        char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
                'f' };

        StringBuffer hexString = new StringBuffer();

        hexString.append(hex[(bite >> 4) & 0x0f]);
        hexString.append(hex[bite & 0x0f]);

        return hexString.toString();
    }

    /**
     * Gets a new byte array that is a subset of <code>bytes</code>.
     * 
     * @param bytes
     *            the byte array to sub
     * @param start
     *            the start index
     * @param length
     *            the number of bytes to include
     * @return a subset byte array
     */
    public static byte[] subbyte(byte[] bytes, int start, int length) {
        byte[] sub = new byte[length];
        System.arraycopy(bytes, start, sub, 0, length);
        return sub;
    }

    /**
     * Gets the index of the first occurrence of <code>value</code> in <code>bytes</code>.
     * 
     * @param bytes
     *            the byte array
     * @param start
     *            the index to start searching from
     * @param value
     *            the value to search for
     * @return the index of <code>value</code> or <code>-1</code> if <code>value</code> could
     *         not be found.
     */
    public static int indexOf(byte[] bytes, int start, byte value) {
        if (start > bytes.length)
            return -1;

        for (int i = start; i != bytes.length; i++) {
            if (bytes[i] == value)
                return i;
        }

        return -1;
    }

    /**
     * Determines if a string matches a regular expression.
     * 
     * @param string
     *            the string to test
     * @param regex
     *            the regular expression
     * @return <code>true</code> if the string matches; <code>false</code> otherwise
     */
    public static boolean isMatch(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * Determines if two byte arrays are equal.
     * 
     * @param one
     *            a byte array
     * @param two
     *            a byte array
     * @return <code>true</code> if the arrays are equal; <code>false</code> otherwise
     */
    public static boolean isEqual(byte[] one, byte[] two) {
        return Arrays.equals(one, two);
    }
}
