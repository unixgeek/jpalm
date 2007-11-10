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
import java.io.InputStream;
import java.util.ArrayList;

import net.sourceforge.jpalm.Utilities;

/**
 * Reads Palm databases from PalmDB format.
 */
public class PalmReader {
    /**
     * Reads a Palm database from a PalmDB stream.
     * 
     * @param stream
     *            an input stream
     * @param database
     *            a Palm database
     * @throws IOException
     *             if the file could not be read
     */
    public void load(InputStream stream, PalmDB database) throws IOException {
        byte[] data = readFully(stream);
        Header header = new Header(Utilities.subbyte(data, 0, Header.DATA_LENGTH));
        int position = Header.DATA_LENGTH;

        // These values will be used to parse the rest of the file.
        int numberOfRecords = header.getNumberOfRecords().uint16Value();
        long applicationInfoOffset = header.getApplicationInfoOffset().uint32Value();
        long sortInfoOffset = header.getSortInfoOffset().uint32Value();

        // The PalmDB header.
        database.setHeader(header);

        // The record headers.
        ArrayList<RecordHeader> recordHeaders = new ArrayList<RecordHeader>(numberOfRecords);
        for (int i = 0; i != numberOfRecords; i++) {
            recordHeaders.add(new RecordHeader(Utilities.subbyte(data, position,
                    RecordHeader.DATA_LENGTH)));
            position += RecordHeader.DATA_LENGTH;
        }

        // If there aren't any records, then use the file length.
        long firstRecordOffset = data.length;
        if ((recordHeaders != null) && (!recordHeaders.isEmpty()))
            firstRecordOffset = recordHeaders.get(0).getOffset().uint32Value();

        // Determine the size of the ApplicationInfo block.
        int applicationInfoSize = 0;
        if ((applicationInfoOffset != 0) && (sortInfoOffset == 0)) {
            /*
             * The SortInfo block isn't present, so the size of the ApplicationInfo block is
             * determined by the offset of the first record.
             */
            applicationInfoSize = (int) (firstRecordOffset - applicationInfoOffset);
        }
        else if ((applicationInfoOffset != 0) && (sortInfoOffset != 0)) {
            /*
             * The SortInfo block is present, so the size of the ApplicationInfo block is determined
             * by the offset of the SortInfo block.
             */
            applicationInfoSize = (int) (sortInfoOffset - applicationInfoOffset);
        }

        // The ApplicationInfo block, if present.
        ApplicationInfo applicationInfo = new ApplicationInfo();
        if ((applicationInfoOffset != 0) && (applicationInfoSize != 0)) {
            position = (int) applicationInfoOffset;
            applicationInfo = new ApplicationInfo(Utilities.subbyte(data, position,
                    applicationInfoSize));
            position += applicationInfoSize;
        }
        else
            applicationInfo = null;

        database.setApplicationInfo(applicationInfo);

        // Determine the size of the SortInfo block.
        int sortInfoSize = 0;
        if (sortInfoOffset != 0) {
            sortInfoSize = (int) (firstRecordOffset - sortInfoOffset);
        }
        // The SortInfo block, if present.
        SortInfo sortInfo;
        if ((sortInfoOffset != 0) && (sortInfoSize != 0)) {
            position = (int) sortInfoOffset;
            sortInfo = new SortInfo(Utilities.subbyte(data, position, sortInfoSize));
            position += sortInfoSize;
        }
        else
            sortInfo = null;

        database.setSortInfo(sortInfo);

        // The records.
        ArrayList<Record> records = new ArrayList<Record>();
        for (int i = 0; i != numberOfRecords; i++) {
            long offset = recordHeaders.get(i).getOffset().uint32Value();
            int size = 0;
            if (i != (numberOfRecords - 1))
                size = (int) (recordHeaders.get(i + 1).getOffset().uint32Value() - offset);
            else
                size = (int) (data.length - offset);
            Record record = new RecordImpl(Utilities.subbyte(data, position, size));
            record.setHeader(recordHeaders.get(i));
            records.add(record);
            position += size;
        }

        database.setRecords(records);
    }

    private byte[] readFully(InputStream stream) throws IOException {
        // This is stupid and ineffecient!
        ArrayList<Byte> dataList = new ArrayList<Byte>(1024);
        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = stream.read(buffer)) != -1) {
            for (int i = 0; i != read; i++) {
                dataList.add(Byte.valueOf(buffer[i]));
            }
        }

        byte[] data = new byte[dataList.size()];
        for (int i = 0; i != dataList.size(); i++) {
            data[i] = dataList.get(i);
        }

        dataList = null;

        return data;
    }
}
