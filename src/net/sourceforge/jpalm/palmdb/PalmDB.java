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

import java.util.List;

/**
 * A Palm database.
 */
public interface PalmDB {
    /**
     * Gets the application info block.
     * 
     * @return the application info block
     */
    public ApplicationInfo getApplicationInfo();

    /**
     * Sets the application info block.
     * 
     * @param applicationInfo
     *            the application info block
     */
    public void setApplicationInfo(ApplicationInfo applicationInfo);

    /**
     * Gets the header.
     * 
     * @return the header
     */
    public Header getHeader();

    /**
     * Sets the header.
     * 
     * @param header
     *            the header
     */
    public void setHeader(Header header);

    /**
     * Gets the records.
     * 
     * @return the records
     */
    public List<Record> getRecords();

    /**
     * Sets the records.
     * 
     * @param records
     *            the records
     */
    public void setRecords(List<Record> records);

    /**
     * Gets the sort info block.
     * 
     * @return the sort info block
     */
    public SortInfo getSortInfo();

    /**
     * Sets the sort info block.
     * 
     * @param sortInfo
     *            the sort info block
     */
    public void setSortInfo(SortInfo sortInfo);
}
