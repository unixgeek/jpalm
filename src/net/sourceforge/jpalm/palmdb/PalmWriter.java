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
package net.sourceforge.jpalm.palmdb;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.juint.UInt16;
import net.sourceforge.juint.UInt32;

/**
 * Writes a Palm database to PalmDB format.
 */
public class PalmWriter {
    /**
     * Writes a Palm database to a PalmDB stream.
     * 
     * @param stream
     *            an output stream
     * @param database
     *            a Palm database
     * @throws IOException
     *             if the file could not be written
     */
    public void save(OutputStream stream, PalmDB database) throws IOException {

        Header header = database.getHeader();

        int numberOfRecords = database.getRecords().size();

        header.setNumberOfRecords(new UInt16(numberOfRecords));

        // Calculate offsets.
        ApplicationInfo applicationInfo = database.getApplicationInfo();
        SortInfo sortInfo = database.getSortInfo();
        long applicationInfoOffset = 0;
        long sortInfoOffset = 0;
        if (applicationInfo != null)
            applicationInfoOffset = Header.DATA_LENGTH
                    + (RecordHeader.DATA_LENGTH * numberOfRecords);
        if (sortInfo != null)
            sortInfoOffset = applicationInfoOffset + applicationInfo.serialize().length;

        header.setApplicationInfoOffset(new UInt32(applicationInfoOffset));
        header.setSortInfoOffset(new UInt32(sortInfoOffset));

        List<Record> records = database.getRecords();
        for (int i = 0; i != numberOfRecords; i++) {
            long offset;
            if (i != 0) {
                offset = records.get(i - 1).getHeader().getOffset().uint32Value()
                        + records.get(i - 1).serialize().length;
            }
            else {
                if (sortInfo != null)
                    offset = sortInfoOffset + sortInfo.serialize().length;
                else if (applicationInfo != null)
                    offset = applicationInfoOffset + applicationInfo.serialize().length;
                else
                    offset = Header.DATA_LENGTH + (RecordHeader.DATA_LENGTH * numberOfRecords);
            }
            records.get(i).getHeader().setOffset(new UInt32(offset));
        }

        stream.write(header.serialize());

        for (int i = 0; i != numberOfRecords; i++) {
            stream.write(records.get(i).getHeader().serialize());
        }

        if (applicationInfo != null)
            stream.write(applicationInfo.serialize());
        if (sortInfo != null)
            stream.write(sortInfo.serialize());

        for (int i = 0; i != numberOfRecords; i++) {
            stream.write(records.get(i).serialize());
        }

        stream.flush();
    }
}
