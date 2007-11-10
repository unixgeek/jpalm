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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jpalm.DataBlock;
import net.sourceforge.jpalm.Utilities;
import net.sourceforge.juint.UInt16;
import net.sourceforge.juint.UInt8;

/**
 * The application info block in a Palm database.
 */
public class ApplicationInfo extends DataBlock {
    /**
     * The length of this <code>DataBlock</code> in <code>byte</code>s, not including the
     * length of <code>applicationData</code>. <br>
     * {@value}
     */
    public static final int DATA_LENGTH = 276;
    protected UInt16 renamedCategories;
    protected List<String> categoryLabels;
    protected List<UInt8> categoryUniqueIds;
    protected UInt8 lastUniqueId;
    protected UInt8 padding;
    protected byte[] applicationData;

    /**
     * Creates a new application info block from the <code>byte</code> array.
     * 
     * @param data
     *            the <code>byte</code> array
     * @see #deserialize(byte[])
     */
    public ApplicationInfo(byte[] data) {
        deserialize(data);
    }

    /**
     * Creates a application info block.
     */
    public ApplicationInfo() {
        applicationData = null;
        lastUniqueId = new UInt8(0);
        padding = new UInt8(0);
        renamedCategories = new UInt16(0);
    }

    // TODO Make labels and ids a map?
    /**
     * Gets the category labels.
     * 
     * @return the category labels
     */
    public List<String> getCategoryLabels() {
        if (categoryLabels == null) {
            categoryLabels = new ArrayList<String>(Category.NUMBER_OF_CATEGORIES);
        }
        return categoryLabels;
    }

    /**
     * Sets the category labels. Labels cannot be longer than {@link Category#CATEGORY_LENGTH} and
     * there cannot be more than {@link Category#NUMBER_OF_CATEGORIES} categories.
     * 
     * @param labels
     *            the category labels
     */
    public void setCategoryLabels(List<String> labels) {
        if (labels.size() > Category.NUMBER_OF_CATEGORIES)
            categoryLabels = labels.subList(0, Category.NUMBER_OF_CATEGORIES);
        else
            categoryLabels = labels;

        for (int i = 0; i != categoryLabels.size(); i++) {
            categoryLabels.set(i, trimLabel(categoryLabels.get(i)));
        }
    }

    /**
     * Gets the unique identifier for each category.
     * 
     * @return the unique identifiers
     */
    public List<UInt8> getCategoryUniqueIds() {
        if (categoryUniqueIds == null) {
            categoryUniqueIds = new ArrayList<UInt8>(Category.NUMBER_OF_CATEGORIES);
        }
        return categoryUniqueIds;
    }

    /**
     * Sets the unique identifier for each category. There cannot be more than
     * {@link Category#NUMBER_OF_CATEGORIES} identifiers.
     * 
     * @param ids
     *            the unique identifiers
     * @see #setLastUniqueId(UInt8)
     */
    public void setCategoryUniqueIds(List<UInt8> ids) {
        if (ids.size() > Category.NUMBER_OF_CATEGORIES)
            categoryUniqueIds = ids.subList(0, Category.NUMBER_OF_CATEGORIES);
        else
            categoryUniqueIds = ids;
    }

    /**
     * Gets the last used unique identifier for a category.
     * 
     * @return the last used unique identifier
     */
    public UInt8 getLastUniqueId() {
        return lastUniqueId;
    }

    /**
     * Sets the last used unique identifier for a category.
     * 
     * @param lastUniqueId
     *            the last used unique identifier
     * @see #setCategoryUniqueIds(List)
     */
    public void setLastUniqueId(UInt8 lastUniqueId) {
        this.lastUniqueId = lastUniqueId;
    }

    /**
     * Gets the padding.
     * 
     * @return the padding
     */
    public UInt8 getPadding() {
        return padding;
    }

    /**
     * Sets the padding.
     * 
     * @param padding
     *            the padding
     */
    public void setPadding(UInt8 padding) {
        this.padding = padding;
    }

    /**
     * Gets the renamed categories.
     * 
     * @return the renamed categories
     */
    public UInt16 getRenamedCategories() {
        return renamedCategories;
    }

    /**
     * Sets the renamed categories.
     * 
     * @param renamedCategories
     *            the renamed categories
     */
    public void setRenamedCategories(UInt16 renamedCategories) {
        this.renamedCategories = renamedCategories;
    }

    /**
     * Gets the application specific data.
     * 
     * @return the application data
     */
    public byte[] getApplicationData() {
        return applicationData.clone();
    }

    /**
     * Sets the application specific data.
     * 
     * @param applicationData
     *            the application data
     */
    public void setApplicationData(byte[] applicationData) {
        this.applicationData = applicationData.clone();
    }

    @Override
    public void deserialize(byte[] data) {
        renamedCategories = new UInt16(Utilities.subbyte(data, 0, 2));
        int index = 2;

        categoryLabels = new ArrayList<String>(Category.NUMBER_OF_CATEGORIES);
        for (int i = 0; i != Category.NUMBER_OF_CATEGORIES; i++) {
            // Find the null terminator.
            int nullIndex = Utilities.indexOf(data, index, (byte) 0x00);
            // The length of the label.
            int length = index - nullIndex;

            // If the null terminator wasn't found, ignore this label.
            if ((nullIndex == -1) || (length > Category.CATEGORY_LENGTH))
                categoryLabels.add("");
            // The null terminator was found.
            else {
                categoryLabels.add(new String(Utilities.subbyte(data, index, nullIndex - index)));
            }
            index += Category.CATEGORY_LENGTH;
        }

        categoryUniqueIds = new ArrayList<UInt8>(Category.NUMBER_OF_CATEGORIES);
        for (int i = 0; i != Category.NUMBER_OF_CATEGORIES; i++) {
            categoryUniqueIds.add(new UInt8(data[index]));
            index++;
        }

        lastUniqueId = new UInt8(data[index]);
        index++;

        padding = new UInt8(data[index]);
        index++;

        // Remaining data is application specific data.
        applicationData = new byte[data.length - DATA_LENGTH];
        for (int i = 0; i != applicationData.length; i++) {
            applicationData[i] = data[index + i];
        }
    }

    @Override
    public byte[] serialize() {
        byte[] bytes;
        if (applicationData == null)
            bytes = new byte[DATA_LENGTH];
        else
            bytes = new byte[DATA_LENGTH + applicationData.length];

        System.arraycopy(renamedCategories.toBigEndian(), 0, bytes, 0, 2);
        int index = 2;

        if (categoryLabels == null)
            categoryLabels = new ArrayList<String>();

        // Pad the category labels, if necessary.
        if (categoryLabels.size() < Category.NUMBER_OF_CATEGORIES) {
            ArrayList<String> labels = new ArrayList<String>();
            labels.addAll(categoryLabels);

            int size = Category.NUMBER_OF_CATEGORIES - categoryLabels.size();
            for (int i = 0; i != size; i++) {
                labels.add("");
            }
            categoryLabels = labels;
        }

        for (String label : categoryLabels) {
            System.arraycopy(label.getBytes(), 0, bytes, index, label.length());
            index += Category.CATEGORY_LENGTH;
        }

        if (categoryUniqueIds == null)
            categoryUniqueIds = new ArrayList<UInt8>();

        // Pad the category ids, if necessary.
        if (categoryUniqueIds.size() < Category.NUMBER_OF_CATEGORIES) {
            ArrayList<UInt8> categoryIds = new ArrayList<UInt8>();
            categoryIds.addAll(categoryUniqueIds);

            int size = Category.NUMBER_OF_CATEGORIES - categoryUniqueIds.size();
            for (int i = 0; i != size; i++) {
                categoryIds.add(new UInt8(0));
            }
            categoryUniqueIds = categoryIds;
        }

        for (UInt8 id : categoryUniqueIds) {
            bytes[index] = id.byteValue();
            index++;
        }

        bytes[index] = lastUniqueId.byteValue();
        index++;

        bytes[index] = padding.byteValue();
        index++;

        if (applicationData != null)
            System.arraycopy(applicationData, 0, bytes, index, applicationData.length);

        return bytes;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ApplicationInfo))
            return false;
        return super.equals(object);
    }

    private String trimLabel(String label) {
        if (label == null)
            return "";
        if (label.length() < Category.CATEGORY_LENGTH)
            return label;

        // Only get CATEGORY_LABEL_LENGTH bytes.
        return label.substring(0, Category.CATEGORY_LENGTH - 1);
    }
}
