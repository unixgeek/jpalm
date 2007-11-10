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

import net.sourceforge.jpalm.DataBlock;
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.juint.UInt16;
import net.sourceforge.juint.UInt32;

/**
 * The header in a Palm database.
 */
public class Header extends DataBlock {
    /**
     * The maximum length of the database name, including 1 for the null terminator.<br>
     * {@value}
     */
    public static final int NAME_LENGTH = 32;
    /**
     * The maximum length of the creator name.<br>
     * {@value}
     */
    public static final int CREATOR_LENGTH = 4;
    /**
     * The maximum length of the type name.<br>
     * {@value}
     */
    public static final int TYPE_LENGTH = 4;
    /**
     * The maximum length of the header in <code>byte</code>s.<br>
     * {@value}
     */
    public static final int DATA_LENGTH = NAME_LENGTH + CREATOR_LENGTH + TYPE_LENGTH + 38;
    /**
     * Database is a resource database.
     */
    public static final UInt16 ATTRIBUTE_RESOURCE_DATABASE = new UInt16(0x0001);
    /**
     * Database is read-only.
     */
    public static final UInt16 ATTRIBUTE_READ_ONLY = new UInt16(0x0002);
    /**
     * Database's application info block is dirty.
     */
    public static final UInt16 ATTRIBUTE_DIRTY_APPLICATION_INFO_AREA = new UInt16(0x0004);
    /**
     * Backup database on next HotSync.
     */
    public static final UInt16 ATTRIBUTE_BACKUP_DATABASE = new UInt16(0x0008);
    /**
     * Replace existing database.
     */
    public static final UInt16 ATTRIBUTE_OK_TO_INSTALL_NEWER = new UInt16(0x0010);
    /**
     * Reset device after this database is installed.
     */
    public static final UInt16 ATTRIBUTE_RESET_DEVICE_AFTER_INSTALL = new UInt16(0x0020);
    /**
     * Disallow copies of this database.
     */
    public static final UInt16 ATTRIBUTE_COPY_PREVENTION = new UInt16(0x0040);
    /**
     * Database is a file stream.
     */
    public static final UInt16 ATTRIBUTE_STREAM = new UInt16(0x0080);
    /**
     * Database should be hidden from view. For data databases this hides the record count. For
     * applications this hides the database from the main launcher view.
     */
    public static final UInt16 ATTRIBUTE_HIDDEN = new UInt16(0x0100);
    /**
     * This data database can be launched by having it's name passed to it's owning application.
     */
    public static final UInt16 ATTRIBUTE_LAUNCHABLE_DATA = new UInt16(0x0200);
    /**
     * Database will be deleted when closed or upon a system reset.
     */
    public static final UInt16 ATTRIBUTE_RECYCLABLE = new UInt16(0x0400);
    /**
     * Database is associated with the application with the same creator. It will be beamed along
     * with the application.
     */
    public static final UInt16 ATTRIBUTE_BUNDLE = new UInt16(0x0800);
    /**
     * Database is open.
     */
    public static final UInt16 ATTRIBUTE_OPEN = new UInt16(0x8000);
    protected String name;
    protected UInt16 attributes;
    protected UInt16 version;
    protected Date creationDate;
    protected Date modificationDate;
    protected Date lastBackupDate;
    protected UInt32 modificationNumber;
    protected UInt32 applicationInfoOffset;
    protected UInt32 sortInfoOffset;
    protected String type;
    protected String creator;
    protected UInt32 uniqueIdSeed;
    protected UInt32 nextRecordListOffset;
    protected UInt16 numberOfRecords;

    /**
     * Creates a new header from the <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     * @see #deserialize(byte[])
     */
    public Header(byte[] data) {
        deserialize(data);
    }

    /**
     * Creates a new header.
     */
    public Header() {
        applicationInfoOffset = new UInt32(0);
        attributes = new UInt16(0);
        Date date = new Date();
        creationDate = date;
        creator = "";
        lastBackupDate = new Date(new UInt32(0));
        modificationDate = date;
        modificationNumber = new UInt32(0);
        name = "";
        nextRecordListOffset = new UInt32(0);
        numberOfRecords = new UInt16(0);
        sortInfoOffset = new UInt32(0);
        type = "";
        uniqueIdSeed = new UInt32(0);
        version = new UInt16(0);
    }

    /**
     * Gets the offset of the application info block.
     * 
     * @return the offset
     */
    public UInt32 getApplicationInfoOffset() {
        return applicationInfoOffset;
    }

    /**
     * Sets the offset of the application info block.
     * 
     * @param applicationInfoOffset
     *            the offset
     */
    public void setApplicationInfoOffset(UInt32 applicationInfoOffset) {
        this.applicationInfoOffset = applicationInfoOffset;
    }

    // TODO Use booleans for attributes.
    /**
     * Gets the attributes.
     * 
     * @return the attributes
     * @see #ATTRIBUTE_BACKUP_DATABASE
     * @see #ATTRIBUTE_BUNDLE
     * @see #ATTRIBUTE_COPY_PREVENTION
     * @see #ATTRIBUTE_DIRTY_APPLICATION_INFO_AREA
     * @see #ATTRIBUTE_HIDDEN
     * @see #ATTRIBUTE_LAUNCHABLE_DATA
     * @see #ATTRIBUTE_OK_TO_INSTALL_NEWER
     * @see #ATTRIBUTE_OPEN
     * @see #ATTRIBUTE_READ_ONLY
     * @see #ATTRIBUTE_RECYCLABLE
     * @see #ATTRIBUTE_RESET_DEVICE_AFTER_INSTALL
     * @see #ATTRIBUTE_RESOURCE_DATABASE
     * @see #ATTRIBUTE_STREAM
     */
    public UInt16 getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes.
     * 
     * @param attributes
     *            the attributes
     * @see #ATTRIBUTE_BACKUP_DATABASE
     * @see #ATTRIBUTE_BUNDLE
     * @see #ATTRIBUTE_COPY_PREVENTION
     * @see #ATTRIBUTE_DIRTY_APPLICATION_INFO_AREA
     * @see #ATTRIBUTE_HIDDEN
     * @see #ATTRIBUTE_LAUNCHABLE_DATA
     * @see #ATTRIBUTE_OK_TO_INSTALL_NEWER
     * @see #ATTRIBUTE_OPEN
     * @see #ATTRIBUTE_READ_ONLY
     * @see #ATTRIBUTE_RECYCLABLE
     * @see #ATTRIBUTE_RESET_DEVICE_AFTER_INSTALL
     * @see #ATTRIBUTE_RESOURCE_DATABASE
     * @see #ATTRIBUTE_STREAM
     */
    public void setAttributes(UInt16 attributes) {
        this.attributes = attributes;
    }

    /**
     * Adds an attribute.
     * 
     * @param attribute
     *            the attribute
     * @see #ATTRIBUTE_BACKUP_DATABASE
     * @see #ATTRIBUTE_BUNDLE
     * @see #ATTRIBUTE_COPY_PREVENTION
     * @see #ATTRIBUTE_DIRTY_APPLICATION_INFO_AREA
     * @see #ATTRIBUTE_HIDDEN
     * @see #ATTRIBUTE_LAUNCHABLE_DATA
     * @see #ATTRIBUTE_OK_TO_INSTALL_NEWER
     * @see #ATTRIBUTE_OPEN
     * @see #ATTRIBUTE_READ_ONLY
     * @see #ATTRIBUTE_RECYCLABLE
     * @see #ATTRIBUTE_RESET_DEVICE_AFTER_INSTALL
     * @see #ATTRIBUTE_RESOURCE_DATABASE
     * @see #ATTRIBUTE_STREAM
     */
    public void addAttribute(UInt16 attribute) {
        this.attributes = attributes.addBitmask(attribute);
    }

    /**
     * Removes an attribute.
     * 
     * @param attribute
     *            the attribute
     * @see #ATTRIBUTE_BACKUP_DATABASE
     * @see #ATTRIBUTE_BUNDLE
     * @see #ATTRIBUTE_COPY_PREVENTION
     * @see #ATTRIBUTE_DIRTY_APPLICATION_INFO_AREA
     * @see #ATTRIBUTE_HIDDEN
     * @see #ATTRIBUTE_LAUNCHABLE_DATA
     * @see #ATTRIBUTE_OK_TO_INSTALL_NEWER
     * @see #ATTRIBUTE_OPEN
     * @see #ATTRIBUTE_READ_ONLY
     * @see #ATTRIBUTE_RECYCLABLE
     * @see #ATTRIBUTE_RESET_DEVICE_AFTER_INSTALL
     * @see #ATTRIBUTE_RESOURCE_DATABASE
     * @see #ATTRIBUTE_STREAM
     */
    public void removeAttribute(UInt16 attribute) {
        this.attributes = attributes.removeBitmask(attribute);
    }

    /**
     * Gets the creation date.
     * 
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date.
     * 
     * @param creationDate
     *            the creation date
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the creator.
     * 
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Sets the creator. The creator cannot be longer than {@link #CREATOR_LENGTH} characters.
     * 
     * @param creator
     *            the creator
     */
    public void setCreator(String creator) {
        int length = (creator.length() < CREATOR_LENGTH) ? creator.length() : CREATOR_LENGTH;
        this.creator = creator.substring(0, length);
    }

    /**
     * Gets the last backup date.
     * 
     * @return the last backup date
     */
    public Date getLastBackupDate() {
        return lastBackupDate;
    }

    /**
     * Sets the last backup date.
     * 
     * @param lastBackupDate
     *            the last backup date
     */
    public void setLastBackupDate(Date lastBackupDate) {
        this.lastBackupDate = lastBackupDate;
    }

    /**
     * Gets the modification date.
     * 
     * @return the modification date
     */
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * Sets the modification date.
     * 
     * @param modificationDate
     *            the modification date
     */
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    /**
     * Gets the modification number.
     * 
     * @return the modification number
     */
    public UInt32 getModificationNumber() {
        return modificationNumber;
    }

    /**
     * Sets the modificaiton number.
     * 
     * @param modificationNumber
     *            the modification number
     */
    public void setModificationNumber(UInt32 modificationNumber) {
        this.modificationNumber = modificationNumber;
    }

    /**
     * Gets the name of the Palm database.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the Palm database. The name cannot be longer than {@value #NAME_LENGTH}
     * characters.
     * 
     * @param name
     *            the name
     */
    public void setName(String name) {
        int length = (name.length() < NAME_LENGTH - 1) ? name.length() : NAME_LENGTH - 1;
        this.name = name.substring(0, length);
    }

    /**
     * Gets the offset of the next record list. (Not used).
     * 
     * @return the offset
     */
    public UInt32 getNextRecordListOffset() {
        return nextRecordListOffset;
    }

    /**
     * Sets the offset of the next record list. (Not used).
     * 
     * @param nextRecordListOffset
     *            the offset
     */
    public void setNextRecordListOffset(UInt32 nextRecordListOffset) {
        this.nextRecordListOffset = nextRecordListOffset;
    }

    /**
     * Gets the number of records in the Palm database.
     * 
     * @return the number of records
     */
    public UInt16 getNumberOfRecords() {
        return numberOfRecords;
    }

    /**
     * Sets the number of records in the Palm database.
     * 
     * @param numberOfRecords
     *            the number of records
     */
    public void setNumberOfRecords(UInt16 numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    /**
     * Gets the offset of the sort info block.
     * 
     * @return the offset
     */
    public UInt32 getSortInfoOffset() {
        return sortInfoOffset;
    }

    /**
     * Sets the offset of the sort info block.
     * 
     * @param sortInfoOffset
     *            the offset
     */
    public void setSortInfoOffset(UInt32 sortInfoOffset) {
        this.sortInfoOffset = sortInfoOffset;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type. The type cannot be longer than {@value #TYPE_LENGTH} characters.
     * 
     * @param type
     *            the type
     */
    public void setType(String type) {
        int length = (type.length() < TYPE_LENGTH) ? type.length() : TYPE_LENGTH;
        this.type = type.substring(0, length);
    }

    /**
     * Gets the unique id seed.
     * 
     * @return the unique id seed
     */
    public UInt32 getUniqueIdSeed() {
        return uniqueIdSeed;
    }

    /**
     * Sets the unique id seed.
     * 
     * @param uniqueIdSeed
     *            the unique id seed
     */
    public void setUniqueIdSeed(UInt32 uniqueIdSeed) {
        this.uniqueIdSeed = uniqueIdSeed;
    }

    /**
     * Gets the version. The version is for use by the user and is not used by the Palm.
     * 
     * @return the version
     */
    public UInt16 getVersion() {
        return version;
    }

    /**
     * Sets the version. The version is for use by the user and is not used by the Palm.
     * 
     * @param version
     *            the version
     */
    public void setVersion(UInt16 version) {
        this.version = version;
    }

    @Override
    public void deserialize(byte[] data) {
        // Find the null terminator.
        int nullIndex = Utilities.indexOf(data, 0, (byte) 0x00);
        int length = nullIndex + 1;

        // If the null terminator wasn't found, ignore the name.
        if ((nullIndex == -1) || (length > NAME_LENGTH))
            name = "";
        else
            name = new String(Utilities.subbyte(data, 0, nullIndex));

        int index = NAME_LENGTH;

        attributes = new UInt16(Utilities.subbyte(data, index, 2));
        index += 2;

        version = new UInt16(Utilities.subbyte(data, index, 2));
        index += 2;

        creationDate = new Date(new UInt32(Utilities.subbyte(data, index, 4)));
        index += 4;

        modificationDate = new Date(new UInt32(Utilities.subbyte(data, index, 4)));
        index += 4;

        lastBackupDate = new Date(new UInt32(Utilities.subbyte(data, index, 4)));
        index += 4;

        modificationNumber = new UInt32(Utilities.subbyte(data, index, 4));
        index += 4;

        applicationInfoOffset = new UInt32(Utilities.subbyte(data, index, 4));
        index += 4;

        sortInfoOffset = new UInt32(Utilities.subbyte(data, index, 4));
        index += 4;

        type = new String(Utilities.subbyte(data, index, TYPE_LENGTH));
        index += TYPE_LENGTH;

        creator = new String(Utilities.subbyte(data, index, CREATOR_LENGTH));
        index += CREATOR_LENGTH;

        uniqueIdSeed = new UInt32(Utilities.subbyte(data, index, 4));
        index += 4;

        nextRecordListOffset = new UInt32(Utilities.subbyte(data, index, 4));
        index += 4;

        numberOfRecords = new UInt16(Utilities.subbyte(data, index, 2));
    }

    @Override
    public byte[] serialize() {
        byte[] bytes = new byte[DATA_LENGTH];
        System.arraycopy(name.getBytes(), 0, bytes, 0, name.length());
        int index = NAME_LENGTH;

        System.arraycopy(attributes.toBigEndian(), 0, bytes, index, 2);
        index += 2;

        System.arraycopy(version.toBigEndian(), 0, bytes, index, 2);
        index += 2;

        System.arraycopy(creationDate.getSeconds().toBigEndian(), 0, bytes, index, 4);
        index += 4;

        System.arraycopy(modificationDate.getSeconds().toBigEndian(), 0, bytes, index, 4);
        index += 4;

        System.arraycopy(lastBackupDate.getSeconds().toBigEndian(), 0, bytes, index, 4);
        index += 4;

        System.arraycopy(modificationNumber.toBigEndian(), 0, bytes, index, 4);
        index += 4;

        System.arraycopy(applicationInfoOffset.toBigEndian(), 0, bytes, index, 4);
        index += 4;

        System.arraycopy(sortInfoOffset.toBigEndian(), 0, bytes, index, 4);
        index += 4;

        System.arraycopy(type.getBytes(), 0, bytes, index, type.length());
        index += TYPE_LENGTH;

        // Only get CREATOR_LENGTH bytes.
        System.arraycopy(creator.getBytes(), 0, bytes, index, creator.length());
        index += CREATOR_LENGTH;

        System.arraycopy(uniqueIdSeed.toBigEndian(), 0, bytes, index, 4);
        index += 4;

        System.arraycopy(nextRecordListOffset.toBigEndian(), 0, bytes, index, 4);
        index += 4;

        System.arraycopy(numberOfRecords.toBigEndian(), 0, bytes, index, 2);

        return bytes;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Header))
            return false;
        return super.equals(object);
    }
}
