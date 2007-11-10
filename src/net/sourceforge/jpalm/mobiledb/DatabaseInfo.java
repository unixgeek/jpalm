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
package net.sourceforge.jpalm.mobiledb;

import java.util.ArrayList;

import net.sourceforge.jpalm.Utilities;
import net.sourceforge.jpalm.palmdb.ApplicationInfo;
import net.sourceforge.juint.UInt16;
import net.sourceforge.juint.UInt32;
import net.sourceforge.juint.UInt8;

/**
 * The database info block in a MobileDB database.
 * 
 * @see ApplicationInfo#getApplicationData()
 * @see ApplicationInfo#setApplicationData(byte[])
 */
public class DatabaseInfo extends ApplicationInfo {
    private static final int BYTES_RESERVED = 3;
    /**
     * The length of this <code>DataBlock</code> in <code>byte</code>s.<br>
     * {@value}
     */
    public static final int DATA_LENGTH = (FilterCriterion.DATA_LENGTH * 3)
            + (SortCriterion.DATA_LENGH * 3) + 11 + BYTES_RESERVED;

    /**
     * No locked columns.
     */
    public static UInt8 LOCKED_COLUMN_NONE = new UInt8(0xff);

    /**
     * No password protection.
     */
    public static UInt8 PASSWORD_PROTECTION_NONE = new UInt8(0x00);
    /**
     * Database is read-only.
     */
    public static UInt8 PASSWORD_PROTECTION_READ_ONLY = new UInt8(0x01);
    /**
     * Database is completely locked.
     */
    public static UInt8 PASSWORD_PROTECTION_LOCK = new UInt8(0x02);

    private UInt16 version;
    private UInt32 passwordHash;
    private boolean searchOnGlobalFind;
    private boolean displayLongDates;
    private boolean editOnSelect;
    private UInt8 lockedColumn;
    private UInt8 passwordProtection;
    private byte[] reserved;
    // Always 3 filters.
    private FilterCriterion[] filter;
    // Always 3 sorts.
    private SortCriterion[] sort;

    /**
     * Creates a new database info block.
     */
    public DatabaseInfo() {
        // Set defaults in the ApplicationInfo block.
        ArrayList<String> categoryLabels = new ArrayList<String>(16);
        categoryLabels.add("Unfiled");
        categoryLabels.add("FieldLabels");
        categoryLabels.add("DataRecords");
        categoryLabels.add("DataRecordsFout");
        categoryLabels.add("Preferences");
        categoryLabels.add("DataType");
        categoryLabels.add("FieldLengths");
        setCategoryLabels(categoryLabels);
        ArrayList<UInt8> categoryIds = new ArrayList<UInt8>(16);
        for (int i = 0; i != 16; i++)
            categoryIds.add(new UInt8(i));
        setCategoryUniqueIds(categoryIds);
        setLastUniqueId(new UInt8(15));
        setPadding(new UInt8(0));
        setRenamedCategories(new UInt16(0));

        version = new UInt16(3);
        searchOnGlobalFind = true;
        displayLongDates = false;
        editOnSelect = false;
        lockedColumn = LOCKED_COLUMN_NONE;
        passwordProtection = PASSWORD_PROTECTION_NONE;
        passwordHash = new UInt32(0);
        filter = new FilterCriterion[3];
        filter[0] = new FilterCriterion();
        filter[1] = new FilterCriterion();
        filter[2] = new FilterCriterion();
        sort = new SortCriterion[3];
        sort[0] = new SortCriterion();
        sort[1] = new SortCriterion();
        sort[2] = new SortCriterion();
        reserved = new byte[BYTES_RESERVED];
    }

    /**
     * Creates a new database info block from the <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     * @see #deserialize(byte[])
     */
    public DatabaseInfo(byte[] data) {
        deserialize(data);
    }

    @Override
    public void deserialize(byte[] data) {
        super.deserialize(data);
        if (data.length < (DATA_LENGTH + ApplicationInfo.DATA_LENGTH)) {
            /*
             * The data was not the size expected; assume that it is missing the application data
             * block, which is where the DatabaseInfo block (this) is stored. When deserializing a
             * MobileDB database, this really shoudn't happen. One example of when it could happen
             * is if setApplicationInfo(new AppicationInfo()) was called on MobileDB.
             */
            applicationData = new DatabaseInfo().serialize();
        }

        version = new UInt16(Utilities.subbyte(applicationData, 0, 2));
        passwordHash = new UInt32(Utilities.subbyte(applicationData, 2, 4));

        if (applicationData[6] == 1)
            searchOnGlobalFind = false;
        else
            searchOnGlobalFind = true;

        if (applicationData[7] == 1)
            displayLongDates = true;
        else
            displayLongDates = false;

        if (applicationData[8] == 1)
            editOnSelect = true;
        else
            editOnSelect = false;

        lockedColumn = new UInt8(applicationData[9]);
        passwordProtection = new UInt8(applicationData[10]);

        int last = 11;

        reserved = new byte[BYTES_RESERVED];
        for (int i = 0; i != BYTES_RESERVED; i++) {
            reserved[i] = applicationData[last + i];
        }
        last += BYTES_RESERVED;

        filter = new FilterCriterion[3];
        for (int i = 0; i != 3; i++) {
            filter[i] = new FilterCriterion(Utilities.subbyte(applicationData, last,
                    FilterCriterion.DATA_LENGTH));
            last += FilterCriterion.DATA_LENGTH;
        }

        sort = new SortCriterion[3];
        for (int i = 0; i != 3; i++) {
            sort[i] = new SortCriterion(Utilities.subbyte(applicationData, last,
                    SortCriterion.DATA_LENGH));
            last += SortCriterion.DATA_LENGH;
        }
    }

    @Override
    public byte[] serialize() {
        applicationData = new byte[DATA_LENGTH];
        int position = 0;

        System.arraycopy(version.toBigEndian(), 0, applicationData, position, 2);
        position += 2;

        System.arraycopy(passwordHash.toBigEndian(), 0, applicationData, position, 4);
        position += 4;

        if (searchOnGlobalFind)
            applicationData[position] = 0;
        else
            applicationData[position] = 1;
        position++;

        if (displayLongDates)
            applicationData[position] = 1;
        else
            applicationData[position] = 0;
        position++;

        if (editOnSelect)
            applicationData[position] = 1;
        else
            applicationData[position] = 0;
        position++;

        applicationData[position] = lockedColumn.byteValue();
        position++;
        applicationData[position] = passwordProtection.byteValue();
        position++;

        System.arraycopy(reserved, 0, applicationData, position, reserved.length);
        position += reserved.length;

        for (int i = 0; i != 3; i++) {
            System.arraycopy(filter[i].serialize(), 0, applicationData, position,
                    FilterCriterion.DATA_LENGTH);
            position += FilterCriterion.DATA_LENGTH;
        }

        for (int i = 0; i != 3; i++) {
            System.arraycopy(sort[i].serialize(), 0, applicationData, position,
                    SortCriterion.DATA_LENGH);
            position += SortCriterion.DATA_LENGH;
        }

        return super.serialize();
    }

    /**
     * Creates a password hash for locking the database.
     * 
     * @param password
     *            the password
     * @return the password hash
     * @see DatabaseInfo#setPasswordHash(UInt32)
     */
    public UInt32 hashPassword(String password) {
        if ((password == null) || (password.length() == 0))
            return new UInt32(0);

        byte[] passwordAsByteArray = password.getBytes();

        // From the palm-db-tools project.
        long tmp = 4711;
        for (int i = 0; i != password.length(); i++) {
            tmp = tmp * 42731 + passwordAsByteArray[i] - 12899
                    * passwordAsByteArray[passwordAsByteArray.length - i - 1];
        }

        return new UInt32(tmp);
    }

    /**
     * Gets the search on global find flag.
     * 
     * @return <code>true</code> if the database should be visible to find; <code>false</code>
     *         otherwise
     */
    public boolean isSearchOnGlobalFind() {
        return searchOnGlobalFind;
    }

    /**
     * Sets the search on global find flag. Default is <code>true</code>.
     * 
     * @param searchOnGlobalFind
     *            <code>true</code> if the database should be visible to find; <code>false</code>
     *            otherwise
     */
    public void setSearchOnGlobalFind(boolean searchOnGlobalFind) {
        this.searchOnGlobalFind = searchOnGlobalFind;
    }

    /**
     * Gets the edit on select flag.
     * 
     * @return <code>true</code> if a record should be edited when selected; <code>false</code>
     *         otherwise
     */
    public boolean isEditOnSelect() {
        return editOnSelect;
    }

    /**
     * Sets the edit on select flag. Default is <code>true</code>.
     * 
     * @param editOnSelect
     *            <code>true</code> if a record should be edited when selected; <code>false</code>
     *            otherwise
     */
    public void setEditOnSelect(boolean editOnSelect) {
        this.editOnSelect = editOnSelect;
    }

    /**
     * Gets the first filter criterion.
     * 
     * @return the first filter criterion.
     */
    public FilterCriterion getFilter1() {
        return filter[0];
    }

    /**
     * Sets the first filter criterion.
     * 
     * @param filter
     *            the first filter criterion
     */
    public void setFilter1(FilterCriterion filter) {
        this.filter[0] = filter;
    }

    /**
     * Gets the second filter criterion.
     * 
     * @return the second filter criterion.
     */
    public FilterCriterion getFilter2() {
        return filter[1];
    }

    /**
     * Sets the second filter criterion.
     * 
     * @param filter
     *            the second filter criterion
     */
    public void setFilter2(FilterCriterion filter) {
        this.filter[1] = filter;
    }

    /**
     * Gets the third filter criterion.
     * 
     * @return the third filter criterion.
     */
    public FilterCriterion getFilter3() {
        return filter[2];
    }

    /**
     * Sets the third filter criterion.
     * 
     * @param filter
     *            the third filter criterion
     */
    public void setFilter3(FilterCriterion filter) {
        this.filter[2] = filter;
    }

    /**
     * Gets the password hash. If the hash is <code>0</code> then the database is not password
     * protected.
     * 
     * @return the password hash or <code>0</code>
     */
    public UInt32 getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the password hash. Set this to <code>0</code> to disable password protection.
     * 
     * @param passwordHash
     *            the password hash or <code>0</code>
     * @see #hashPassword(String)
     */
    public void setPasswordHash(UInt32 passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the display long dates flag.
     * <p>
     * Example long date: Sep 3, 2006.<br>
     * Example short date: 9/3/06.
     * 
     * @return <code>true</code> if long dates should be shown; <code>false</code> otherwise
     */
    public boolean isDisplayLongDates() {
        return displayLongDates;
    }

    /**
     * Sets the display long dates flag. Default is <code>false</code>.
     * <p>
     * Example long date: Sep 3, 2006.<br>
     * Example short date: 9/3/06.
     * 
     * @param displayLongDates
     *            <code>true</code> if long dates should be shown; <code>false</code> otherwise
     */
    public void setDisplayLongDates(boolean displayLongDates) {
        this.displayLongDates = displayLongDates;
    }

    /**
     * Gets the reserved bytes. These bytes are reserved for future versions of MobileDB.
     * 
     * @return the reserved bytes
     */
    public byte[] getReserved() {
        return reserved;
    }

    /**
     * Sets the reserved bytes. These bytes are reserved for future versions of MobileDB.
     * 
     * @param reserved
     *            the reserved bytes
     */
    public void setReserved(byte[] reserved) {
        this.reserved = reserved;
    }

    /**
     * Gets the first sort criterion.
     * 
     * @return the first sort critrion
     */
    public SortCriterion getSort1() {
        return sort[0];
    }

    /**
     * Sets the first sort criterion.
     * 
     * @param sort
     *            the first sort criterion
     */
    public void setSort1(SortCriterion sort) {
        this.sort[0] = sort;
    }

    /**
     * Gets the second sort criterion.
     * 
     * @return the second sort critrion
     */
    public SortCriterion getSort2() {
        return sort[1];
    }

    /**
     * Sets the second sort criterion.
     * 
     * @param sort
     *            the second sort criterion
     */
    public void setSort2(SortCriterion sort) {
        this.sort[1] = sort;
    }

    /**
     * Gets the third sort criterion.
     * 
     * @return the third sort critrion
     */
    public SortCriterion getSort3() {
        return sort[2];
    }

    /**
     * Sets the third sort criterion.
     * 
     * @param sort
     *            the third sort criterion
     */
    public void setSort3(SortCriterion sort) {
        this.sort[2] = sort;
    }

    /**
     * Gets the header version. Should always be <code>1</code>.
     * 
     * @return the header version
     */
    public UInt16 getVersion() {
        return version;
    }

    /**
     * Sets the header version. Should always be <code>1</code>.
     * 
     * @param version
     *            the header version
     */
    public void setVersion(UInt16 version) {
        this.version = version;
    }

    /**
     * Gets the locked column.
     * 
     * @return the locked column number or {@link #LOCKED_COLUMN_NONE}
     */
    public UInt8 getLockedColumn() {
        return lockedColumn;
    }

    /**
     * Sets the locked column. A column that is locked doesn't scroll in the record list.
     * 
     * @param lockedColumn
     *            the locked column number or {@link #LOCKED_COLUMN_NONE}
     */
    public void setLockedColumn(UInt8 lockedColumn) {
        this.lockedColumn = lockedColumn;
    }

    /**
     * Gets the type of password protection.
     * 
     * @return {@link #PASSWORD_PROTECTION_LOCK}, {@link #PASSWORD_PROTECTION_NONE}, or
     *         {@link #PASSWORD_PROTECTION_READ_ONLY}
     */
    public UInt8 getPasswordProtection() {
        return passwordProtection;
    }

    /**
     * Sets the type of password protection.
     * 
     * @param passwordProtection
     *            {@link #PASSWORD_PROTECTION_LOCK}, {@link #PASSWORD_PROTECTION_NONE}, or
     *            {@link #PASSWORD_PROTECTION_READ_ONLY}
     * @see #setPasswordHash(UInt32)
     */
    public void setPasswordProtection(UInt8 passwordProtection) {
        this.passwordProtection = passwordProtection;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DatabaseInfo))
            return false;
        return super.equals(object);
    }
}
