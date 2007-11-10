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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.jpalm.mobiledb.field.definition.Definition;
import net.sourceforge.jpalm.mobiledb.record.DataRecord;
import net.sourceforge.jpalm.mobiledb.record.FieldDefinitionsRecord;
import net.sourceforge.jpalm.mobiledb.record.FieldDisplaySizesRecord;
import net.sourceforge.jpalm.mobiledb.record.FieldLabelsRecord;
import net.sourceforge.jpalm.mobiledb.record.FilteredDataRecord;
import net.sourceforge.jpalm.mobiledb.record.PreferencesRecord;
import net.sourceforge.jpalm.palmdb.ApplicationInfo;
import net.sourceforge.jpalm.palmdb.Header;
import net.sourceforge.jpalm.palmdb.PalmDB;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.SortInfo;
import net.sourceforge.juint.UInt16;
import net.sourceforge.juint.UInt32;
import net.sourceforge.juint.UInt8;

/**
 * A MobileDB database.
 */
public class MobileDB implements PalmDB {
    protected Header header;
    protected SortInfo sortInfo;
    protected DatabaseInfo databaseInfo;
    protected List<DataRecord> dataRecords;
    protected List<FilteredDataRecord> filteredDataRecords;
    protected FieldDefinitionsRecord fieldDefinitionsRecord;
    protected FieldLabelsRecord fieldLabelsRecord;
    protected FieldDisplaySizesRecord fieldDisplaySizesRecord;
    protected PreferencesRecord preferencesRecord;

    /**
     * Creates a new MobileDB database with sane defaults and no records.
     */
    public MobileDB(Map<String, Definition> fields) {
        header = new Header();
        header.setApplicationInfoOffset(new UInt32(0));
        header.addAttribute(Header.ATTRIBUTE_BACKUP_DATABASE);
        net.sourceforge.jpalm.palmdb.Date date = new net.sourceforge.jpalm.palmdb.Date();
        header.setCreationDate(date);
        header.setCreator("Mdb1");
        header.setLastBackupDate(new net.sourceforge.jpalm.palmdb.Date(new UInt32(0)));
        header.setModificationDate(date);
        header.setModificationNumber(new UInt32(0));
        header.setName("default");
        header.setNextRecordListOffset(new UInt32(0));
        header.setNumberOfRecords(new UInt16(0));
        header.setSortInfoOffset(new UInt32(0));
        header.setType("Mdb1");
        header.setUniqueIdSeed(new UInt32(0));
        header.setVersion(new UInt16(0));

        databaseInfo = new DatabaseInfo();

        sortInfo = null;

        setPreferencesRecord(new PreferencesRecord());
        setFieldDisplaySizesRecord(new FieldDisplaySizesRecord());

        ArrayList<String> fieldLabels = new ArrayList<String>(fields.size());
        ArrayList<Definition> fieldDefinitions = new ArrayList<Definition>(fields.size());
        for (Entry<String, Definition> entry : fields.entrySet()) {
            fieldLabels.add(entry.getKey());
            fieldDefinitions.add(entry.getValue());
        }
        setFieldLabelsRecord(new FieldLabelsRecord(fieldLabels));
        setFieldDefinitionsRecord(new FieldDefinitionsRecord(fieldDefinitions));
    }

    /**
     * Creates a new MobileDB database with sane defaults, no records, and no fields.
     */
    public MobileDB() {
        this(new HashMap<String, Definition>());
    }

    /**
     * Gets the database info block.
     * 
     * @return the database info block
     */
    public DatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    /**
     * Sets the database info block.
     * 
     * @param databaseInfo
     *            the database info block
     */
    public void setDatabaseInfo(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    /**
     * Gets the creation date.
     * 
     * @return the creation date
     * @see Header#getCreationDate()
     */
    public Date getCreationDate() {
        return header.getCreationDate().getDate();
    }

    /**
     * Sets the creation date.
     * 
     * @param date
     *            the creation date
     * @see Header#setCreationDate(net.sourceforge.jpalm.palmdb.Date)
     */
    public void setCreationDate(Date date) {
        header.setCreationDate(new net.sourceforge.jpalm.palmdb.Date(date));
    }

    /**
     * Gets the modification date.
     * 
     * @return the modification date
     * @see Header#getModificationDate()
     */
    public Date getModificationDate() {
        return header.getModificationDate().getDate();
    }

    /**
     * Sets the modification date.
     * 
     * @param date
     *            the modification date
     * @see Header#setModificationDate(net.sourceforge.jpalm.palmdb.Date)
     */
    public void setModificationDate(Date date) {
        header.setModificationDate(new net.sourceforge.jpalm.palmdb.Date(date));
    }

    /**
     * Gets the name of this database.
     * 
     * @return the name
     * @see Header#getName()
     */
    public String getName() {
        return header.getName();
    }

    /**
     * Sets the name of this database.
     * 
     * @param name
     *            the name
     * @see Header#setName(String)
     */
    public void setName(String name) {
        header.setName(name);
    }

    /**
     * Gets the data records.
     * 
     * @return the data records
     */
    public List<DataRecord> getDataRecords() {
        return dataRecords;
    }

    /**
     * Sets the data records.
     * 
     * @param dataRecords
     *            the data records
     */
    public void setDataRecords(List<DataRecord> dataRecords) {
        this.dataRecords = dataRecords;
    }

    /**
     * Gets the field definitions record.
     * 
     * @return the field definitions record
     */
    public FieldDefinitionsRecord getFieldDefinitionsRecord() {
        return fieldDefinitionsRecord;
    }

    /**
     * Sets the field definitions record.
     * 
     * @param fieldDefinitionsRecord
     *            the field definitions record
     */
    public void setFieldDefinitionsRecord(FieldDefinitionsRecord fieldDefinitionsRecord) {
        this.fieldDefinitionsRecord = fieldDefinitionsRecord;
    }

    /**
     * Gets the field definitions.
     * 
     * @return the field definitions
     */
    public List<Definition> getFieldDefinitions() {
        return getFieldDefinitionsRecord().getFieldDefinitions();
    }

    /**
     * Sets the field definitions
     * 
     * @param definitions
     *            the field definitions
     */
    public void setFieldDefinitions(List<Definition> definitions) {
        setFieldDefinitionsRecord(new FieldDefinitionsRecord(definitions));
    }

    /**
     * Gets the field display sizes record.
     * 
     * @return the field display sizes record
     */
    public FieldDisplaySizesRecord getFieldDisplaySizesRecord() {
        return fieldDisplaySizesRecord;
    }

    /**
     * Sets the field display sizes record.
     * 
     * @param fieldDisplaySizesRecord
     *            the field display sizes record
     */
    public void setFieldDisplaySizesRecord(FieldDisplaySizesRecord fieldDisplaySizesRecord) {
        this.fieldDisplaySizesRecord = fieldDisplaySizesRecord;
    }

    /**
     * Gets the field display sizes.
     * 
     * @return the field display sizes
     */
    public List<Integer> getFieldDisplaySizes() {
        return getFieldDisplaySizesRecord().getFieldDisplaySizes();
    }

    /**
     * Sets the field display sizes.
     * 
     * @param sizes
     *            the field display sizes
     */
    public void setFieldDisplaySizes(List<Integer> sizes) {
        setFieldDisplaySizesRecord(new FieldDisplaySizesRecord(sizes));
    }

    /**
     * Gets the field labels record.
     * 
     * @return the field labels record
     */
    public FieldLabelsRecord getFieldLabelsRecord() {
        return fieldLabelsRecord;
    }

    /**
     * Sets the field labels record.
     * 
     * @param fieldLabelsRecord
     *            the field labels record
     */
    public void setFieldLabelsRecord(FieldLabelsRecord fieldLabelsRecord) {
        this.fieldLabelsRecord = fieldLabelsRecord;
    }

    /**
     * Gets the field labels.
     * 
     * @return the field labels
     */
    public List<String> getFieldLabels() {
        return getFieldLabelsRecord().getFieldLabels();
    }

    /**
     * Sets the field labels.
     * 
     * @param fieldLabels
     *            the field labels
     */
    public void setFieldLabels(List<String> fieldLabels) {
        setFieldLabelsRecord(new FieldLabelsRecord(fieldLabels));
    }

    /**
     * Gets the filtered data records.
     * 
     * @return the filtered data records
     */
    public List<FilteredDataRecord> getFilteredDataRecords() {
        return filteredDataRecords;
    }

    /**
     * Sets the filtered data records.
     * 
     * @param filteredDataRecords
     *            the filtered data records
     */
    public void setFilteredDataRecords(List<FilteredDataRecord> filteredDataRecords) {
        this.filteredDataRecords = filteredDataRecords;
    }

    /**
     * Gets the preferences record.
     * 
     * @return the preferences record
     */
    public PreferencesRecord getPreferencesRecord() {
        return preferencesRecord;
    }

    /**
     * Sets the preferences record.
     * 
     * @param preferencesRecord
     *            the preferences record
     */
    public void setPreferencesRecord(PreferencesRecord preferencesRecord) {
        this.preferencesRecord = preferencesRecord;
    }

    /**
     * Sets the database info note.
     * 
     * @return the note
     */
    public String getNote() {
        return getPreferencesRecord().getNote();
    }

    /**
     * Gets the database info note.
     * 
     * @param note
     *            the note
     */
    public void setNote(String note) {
        setPreferencesRecord(new PreferencesRecord(note));
    }

    /**
     * Sets the user data. The object will be serialized and stored in the sort info block which is
     * unused by MobileDB.
     * 
     * @param object
     *            the object to embed in the database
     * @throws IOException
     *             if an error occurs during serialization
     * @see SortInfo
     * @see PalmDB#setSortInfo(SortInfo)
     */
    public void setUserData(Serializable object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ObjectOutputStream(byteArrayOutputStream).writeObject(object);
        SortInfo info = new SortInfo(byteArrayOutputStream.toByteArray());
        setSortInfo(info);
    }

    /**
     * Gets the user data. The object will be deserialized from data stored in the sort info block.
     * 
     * @return the object embedded in the database
     * @throws IOException
     *             if an error occurs during deserialization
     * @throws ClassNotFoundException
     *             if the class of the serialized object cannot be found
     */
    public Object getUserData() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getSortInfo()
                .serialize());
        return new ObjectInputStream(byteArrayInputStream).readObject();
    }

    /**
     * Sets the user data. The data will be stored in the sort info block which is unused by
     * MobileDB.
     * 
     * @param data
     *            the data to embed
     * @see SortInfo
     * @see PalmDB#setSortInfo(SortInfo)
     */
    public void setUserDataAsByteArray(byte[] data) {
        setSortInfo(new SortInfo(data));
    }

    /**
     * Gets the user data. The data will be loaded from the sort info block.
     * 
     * @return the embedded data
     */
    public byte[] getUserDataAsByteArray() {
        return getSortInfo().serialize();
    }

    /***********************************************************************************************
     * Methods for PalmDB interface.
     **********************************************************************************************/

    /**
     * Needed for {@link PalmDB} interface; don't use this unless you know what you are doing.
     * <p>
     * {@inheritDoc}
     */
    public List<Record> getRecords() {
        // Put all records into one list.
        ArrayList<Record> records = new ArrayList<Record>();
        if (getPreferencesRecord() != null)
            records.add(getPreferencesRecord());
        if (getFieldDisplaySizesRecord() != null)
            records.add(getFieldDisplaySizesRecord());
        if (getFieldLabelsRecord() != null)
            records.add(getFieldLabelsRecord());
        if (getFieldDefinitionsRecord() != null)
            records.add(getFieldDefinitionsRecord());
        if (getDataRecords() != null)
            records.addAll(getDataRecords());
        if (getFilteredDataRecords() != null)
            records.addAll(getFilteredDataRecords());
        return records;
    }

    /**
     * Needed for {@link PalmDB} interface; don't use this unless you know what you are doing.
     * <p>
     * {@inheritDoc}
     */
    public void setRecords(List<Record> records) {
        for (Record record : records) {
            /*
             * Parse out accepted types. In cases where only one record is allowed (i.e.,
             * FieldLabelsRecord) the last liar wins.
             */
            UInt8 category = record.getHeader().getCategory();
            if (category.equals(DataRecord.CATEGORY_ID)) {
                if (getDataRecords() == null)
                    dataRecords = new ArrayList<DataRecord>();
                getDataRecords().add(new DataRecord(record));
            }
            else if (category.equals(FilteredDataRecord.CATEGORY_ID)) {
                if (getFilteredDataRecords() == null)
                    filteredDataRecords = new ArrayList<FilteredDataRecord>();
                getFilteredDataRecords().add(new FilteredDataRecord(record));
            }
            else if (category.equals(FieldDefinitionsRecord.CATEGORY_ID)) {
                setFieldDefinitionsRecord(new FieldDefinitionsRecord(record));
            }
            else if (category.equals(FieldLabelsRecord.CATEGORY_ID)) {
                setFieldLabelsRecord(new FieldLabelsRecord(record));
            }
            else if (category.equals(FieldDisplaySizesRecord.CATEGORY_ID)) {
                setFieldDisplaySizesRecord(new FieldDisplaySizesRecord(record));
            }
            else if (category.equals(PreferencesRecord.CATEGORY_ID)) {
                setPreferencesRecord(new PreferencesRecord(record));
            }
            else {
                // Ignore it.
            }
        }
    }

    /**
     * Needed for {@link PalmDB} interface; don't use this unless you know what you are doing.
     * <p>
     * {@inheritDoc}
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Needed for {@link PalmDB} interface; don't use this unless you know what you are doing.
     * <p>
     * {@inheritDoc}
     */
    public void setHeader(Header header) {
        this.header = header;
    }

    /**
     * Needed for {@link PalmDB} interface; don't use this unless you know what you are doing.
     * <p>
     * {@inheritDoc}
     */
    public ApplicationInfo getApplicationInfo() {
        return getDatabaseInfo();
    }

    /**
     * Needed for {@link PalmDB} interface; don't use this unless you know what you are doing.
     * <p>
     * {@inheritDoc}
     */
    public void setApplicationInfo(ApplicationInfo applicationInfo) {
        setDatabaseInfo(new DatabaseInfo(applicationInfo.serialize()));
    }

    /**
     * Needed for {@link PalmDB} interface; don't use this unless you know what you are doing.
     * <p>
     * {@inheritDoc}
     */
    public SortInfo getSortInfo() {
        return sortInfo;
    }

    /**
     * Needed for {@link PalmDB} interface; don't use this unless you know what you are doing.
     * <p>
     * {@inheritDoc}
     */
    public void setSortInfo(SortInfo sortInfo) {
        this.sortInfo = sortInfo;
    }
}
