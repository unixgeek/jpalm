/*
 * palm-db-tools: Read/write MobileDB databases
 * Copyright (C) 1999 by Tom Dyas (tdyas@users.sourceforge.net)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * File format documented at:
 * http://www.mobilegeneration.com/products/mobiledb/dbstructure.htm
 */

#include <iostream>
#include <cstdlib>
#include <cstring>
#include <stdexcept>
#include <algorithm>

#include <memory.h>
#include <string.h>

#include "libsupport/strop.h"

#include "MobileDB.h"

using namespace PalmLib::FlatFile;
using namespace PalmLib;

static const pi_char_t CATEGORY_FIELD_LABELS          = 1;
static const pi_char_t CATEGORY_DATA_RECORDS          = 2;
static const pi_char_t CATEGORY_DATA_RECORDS_FILTERED = 3;
static const pi_char_t CATEGORY_PREFERENCES           = 4;
static const pi_char_t CATEGORY_DATA_TYPES            = 5;
static const pi_char_t CATEGORY_FIELD_LENGTHS         = 6;

PalmLib::Block
PalmLib::FlatFile::MobileDB::MobileAppInfoType::pack()
{
    int i;

    pi_char_t* buf = new pi_char_t[512];
    pi_char_t* p = buf;

    // Clear out the entire buffer.
    memset(buf, 0, 512);

    PalmLib::set_short(p, renamedCategories);
    p += 2;

    for (i = 0; i < 16; ++i) {
        strncpy((char *) p, categoryLabels[i].c_str(), 15);
        p += 16;
    }
    for (i = 0; i < 16; ++i) {
        *p++ = categoryUniqueIDs[i];
    }
    *p++ = lastUniqueID;
    *p++ = 0; // reserved byte after lastUniqueID

    PalmLib::set_short(p, version);
    p += 2;

    PalmLib::set_long(p, lock);
    p += 4;

    *p++ = (dontSearch) ? 1 : 0;
    *p++ = (editOnSelect) ? 1 : 0;
    *p++ = 0; // reserved[0]
    *p++ = 0; // reserved[1]
    *p++ = 0; // reserved[2]

    for (i = 0; i < 3; ++i) {
        strncpy((char *) p, filter[i].text.c_str(), 39);
        p += 40;

        *p++ = static_cast<pi_char_t> (filter[i].field & 0xFF);
        *p++ = filter[i].flags;
    }

    for (i = 0; i < 3; ++i) {
        *p++ = static_cast<pi_char_t> (sort[i].field & 0xFF);
        *p++ = (sort[i].descending) ? 1 : 0;
        *p++ = sort[i].type;
    }
        
    PalmLib::Block data(buf, p - buf);
    delete [] buf;
    return data;
}

void
PalmLib::FlatFile::MobileDB::MobileAppInfoType::unpack(const PalmLib::Block& block)
{
    int i;

    if (block.size() < (2 + (16 * 16) + (16 * 1) + 1))
        throw PalmLib::error("header is corrupt");

    PalmLib::Block::const_pointer p = block.data();

    renamedCategories = PalmLib::get_short(p);
    p += 2;

    for (i = 0; i < 16; ++i) {
        categoryLabels[i] = std::string((char *) p);
        p += 16;
    }
    for (i = 0; i < 16; ++i) {
        categoryUniqueIDs[i] = *p++;
    }
    lastUniqueID = *p++;
    p += 1; // reserved byte after lastUniqueID

    if (block.end() - p < (2 + 4 + 1 + 1 + 3 + 3 * (40 + 1 + 1)
                           + 3 * (1 + 1 + 1)))
        throw PalmLib::error("header is corrupt");

    version = PalmLib::get_short(p);
    p += 2;

    lock = PalmLib::get_long(p);
    p += 4;

    dontSearch = (*p++ != 0) ? true : false;
    editOnSelect = (*p++ != 0) ? true : false;
    p += 3; // reserved[3]

    for (i = 0; i < 3; ++i) {
        filter[i].text = std::string((char *) p);
        p += 40;
        filter[i].field = static_cast<int> (*p++);
        filter[i].flags = *p++;
    }

    for (i = 0; i < 3; ++i) {
        sort[i].field = static_cast<int> (*p++);
        sort[i].descending = (*p++ != 0) ? true : false;
        sort[i].type = *p++;
    }

    if (version != 1)
        throw PalmLib::error("unknown header version");
}

bool
PalmLib::FlatFile::MobileDB::classify(PalmLib::Database& pdb)
{
    return (! pdb.isResourceDB())
        && (pdb.creator() == PalmLib::mktag('M','d','b','1'))
        && (pdb.type()    == PalmLib::mktag('M','d','b','1'));
}

bool
PalmLib::FlatFile::MobileDB::match_name(const std::string& name)
{
    return name == "MobileDB" || name == "mobiledb" || name == "mdb";
}

unsigned
PalmLib::FlatFile::MobileDB::find_metadata_index(const PalmLib::Database& pdb,
                                                 pi_char_t cat) const
{
    unsigned count = 0;
    unsigned index = 0;

    for (unsigned i = 0; i < pdb.getNumRecords(); ++i) {
        const PalmLib::Record record = pdb.getRecord(i);
        if (record.category() == cat) {
            count++;
            index = i;
        }
    }

    if (count == 0)
        throw PalmLib::error("metadata record not found");

    if (count > 1)
        throw PalmLib::error("multiple metadata records");

    return index;
}

PalmLib::FlatFile::MobileDB::mdb_record_t
PalmLib::FlatFile::MobileDB::parse_record(const PalmLib::Record& rec) const
{
    pi_char_t header[] = { 0xFF, 0xFF, 0xFF, 0x01, 0xFF, 0x00, 0x00 };
    mdb_record_t fields;

    /* Verify that record header exists and is valid. */
    if (rec.size() < sizeof(header)
        || memcmp(rec.data(), &header[0], sizeof(header)) != 0)
        throw PalmLib::error("record header is corrupt");

    /* Point at the start of the record data. */
    PalmLib::Record::const_pointer p = rec.data() + sizeof(header);
    while (p != rec.end()) {
        // Extract the field number.
        unsigned field = *p++;

        // If the field number is 0xFF, then we have reached the end.
        if (field == 0xFF) break;

        // Make sure that we don't go beyond the maximum number of fields.
        if (field >= getMaxNumOfFields())
            throw PalmLib::error("maximum number of fields exceeded");

        // Expand the fields vector if this field goes beyond the end
        // of the existing space.
        if (field + 1 > fields.size()) fields.resize(field + 1);

        // Now search for the end of the current field.
        pi_char_t* q = reinterpret_cast<pi_char_t*>
            (memchr(p, 0, rec.end() - p));
        if (!q)
            throw PalmLib::error("field terminiator is missing");

        // Copy the field data into the fields vector.
        std::string::size_type size = (q - p);
        if (size > 0)
            fields[field] = std::string((char *) p, size);
        else
            fields[field] = "";

        // Advance the field pointer.
        p = q + 1;
    }

    // Make sure that we processed the entire record.
    if (p != rec.end())
        throw PalmLib::error("record is corrupt");

    return fields;
}

PalmLib::Record
PalmLib::FlatFile::MobileDB::build_record(const mdb_record_t& fields) const
{
    pi_char_t header[] = { 0xFF, 0xFF, 0xFF, 0x01, 0xFF, 0x00, 0x00 };
    unsigned i;

    // Calculate the total size of the final record.
    PalmLib::Record::size_type size = sizeof(header) + 1;
    for (i = 0; i < fields.size(); ++i) {
        size += 1 + fields[i].length() + 1;
    }

    // Allocate the PalmOS record object.
    PalmLib::Record record(0, 0, size);
    PalmLib::Record::pointer p = record.data();

    // Copy the header into the record.
    memcpy(p, &header[0], sizeof(header));
    p += sizeof(header);

    // Now copy the fields into the buffer.
    for (i = 0; i < fields.size(); ++i) {
        // Place the field number into the record.
        *p++ = static_cast<pi_char_t> (i & 0xFF);

        // Copy the field data into the record.
        strcpy((char *) p, fields[i].c_str());

        // Advance the pointer to the next field position.
        p += fields[i].length() + 1;
    }

    // Copy the trailer into place.
    *p++ = 0xFF;

    return record;
}

PalmLib::FlatFile::MobileDB::MobileDB(PalmLib::Database& pdb)
    : Database("mdb", pdb), m_password_hash(0), m_find_disabled(false),
      m_edit_on_select(false)
{
    unsigned i, numFields, recNum;

    // Unpack the application information block. It is not an error if
    // we fail to unpack it since all the metadata is contained within
    // the records.
    try {
        MobileAppInfoType hdr;
        hdr.unpack(pdb.getAppInfoBlock());
        m_find_disabled = hdr.dontSearch;
        m_edit_on_select = hdr.editOnSelect;
    } catch (...) {
    }

    // Extract the field labels record.
    recNum = find_metadata_index(pdb, CATEGORY_FIELD_LABELS);
    mdb_record_t names = parse_record(pdb.getRecord(recNum));
    numFields = names.size();

    // Extract the data types record.
    recNum = find_metadata_index(pdb, CATEGORY_DATA_TYPES);
    mdb_record_t types = parse_record(pdb.getRecord(recNum));
    if (types.size() < numFields)
        throw PalmLib::error("data types record is corrupt");

    // Extract the field lengths.
    recNum = find_metadata_index(pdb, CATEGORY_FIELD_LENGTHS);
    mdb_record_t widths = parse_record(pdb.getRecord(recNum));
    if (widths.size() < numFields)
        throw PalmLib::error("width record is corrupt");

    // Fill in the schema information and single list view.
    PalmLib::FlatFile::ListView lv;
    for (i = 0; i < numFields; ++i) {
        unsigned width;

        appendField(names[i], Field::STRING);
        StrOps::convert_string(widths[i], width);
        lv.push_back(ListViewColumn(i, width));
    }
    appendListView(lv);

    // Extract the records.
    for (i = 0; i < pdb.getNumRecords(); ++i) {
        PalmLib::Record pdb_record = pdb.getRecord(i);
        Record record;

        // Skip this record if it isn't a data record.
        if (pdb_record.category() != CATEGORY_DATA_RECORDS
            && pdb_record.category() != CATEGORY_DATA_RECORDS_FILTERED)
            continue;

        // Extract the fields from the record.
        mdb_record_t fields = parse_record(pdb_record);
        if (fields.size() != getNumOfFields())
            throw PalmLib::error("data record has the wrong number of fields");

        // Insert the fields into the flat-file record.
        for (mdb_record_t::const_iterator p = fields.begin();
             p != fields.end(); ++p) {
            Field field;

            field.type = Field::STRING;
            field.v_string = (*p);
            record.appendField(field);
        }

        // Add this record to the database.
        appendRecord(record);
    }
}

void
PalmLib::FlatFile::MobileDB::outputPDB(PalmLib::Database& pdb) const
{
    unsigned i;

    // Let the superclass have a chance.
    SUPERCLASS(PalmLib::FlatFile,Database,outputPDB,(pdb));

    // Set the type and creator for this database.
    pdb.creator(PalmLib::mktag('M','d','b','1'));
    pdb.type(PalmLib::mktag('M','d','b','1'));

    // Setup the application information block.
    MobileAppInfoType hdr;
    hdr.renamedCategories = 0;
    hdr.categoryLabels[0] = "Unfiled";
    hdr.categoryLabels[1] = "FieldLabels";
    hdr.categoryLabels[2] = "DataRecords";
    hdr.categoryLabels[3] = "DataRecordsFout";
    hdr.categoryLabels[4] = "Preferences";
    hdr.categoryLabels[5] = "DataType";
    hdr.categoryLabels[6] = "FieldLengths";
    for (i = 7; i < 16; ++i) hdr.categoryLabels[i] = "";
    for (i = 0; i < 16; ++i) hdr.categoryUniqueIDs[i] = i;
    hdr.lastUniqueID = 15;
    hdr.version = 1;
    hdr.lock = m_password_hash;
    hdr.dontSearch = m_find_disabled;
    hdr.editOnSelect = m_edit_on_select;
    for (i = 0; i < 3; ++i) {
        hdr.filter[i].text = "Hello World";
        hdr.filter[i].field = i;
        hdr.filter[i].flags = 0;
        hdr.sort[i].field = i;
        hdr.sort[i].descending = false;
        hdr.sort[i].type = 0;
    }
    pdb.setAppInfoBlock(hdr.pack());

    // Create the record which contains the field names.
    {
        mdb_record_t v;

        for (i = 0; i < getNumOfFields(); ++i) {
            v.push_back(field_name(i));
        }
        PalmLib::Record record(build_record(v));
        record.category(CATEGORY_FIELD_LABELS);
        pdb.appendRecord(record);
    }

    // Create the record which contains the field types.
    {
        mdb_record_t v;

        for (i = 0; i < getMaxNumOfFields(); ++i)
            v.push_back(std::string("str"));
        PalmLib::Record record(build_record(v));
        record.category(CATEGORY_DATA_TYPES);
        pdb.appendRecord(record);
    }

    // Create the record which contains the widths.
    {
        PalmLib::FlatFile::ListView lv = getListView(0);
        PalmLib::FlatFile::ListView::const_iterator iter = lv.begin();
        mdb_record_t v;

        for (i = 0; i < getNumOfFields(); ++i, ++iter) {
            std::ostrstream stream;
            stream << (*iter).width << std::ends;
            v.push_back(stream.str());
        }
        PalmLib::Record record(build_record(v));
        record.category(CATEGORY_FIELD_LENGTHS);
        pdb.appendRecord(record);
    }

    // Create the preferences record.
    {
        mdb_record_t v;

        for (i = 0; i < getNumOfFields(); ++i)
            v.push_back(std::string(1, char(0x01)));
        PalmLib::Record record(build_record(v));
        record.category(CATEGORY_PREFERENCES);
        pdb.appendRecord(record);
    }

    // Insert the actual data records.
    for (i = 0; i < getNumRecords(); ++i) {
        PalmLib::FlatFile::Record record = getRecord(i);
        mdb_record_t v;

        for (unsigned int j = 0; j < getNumOfFields(); j++) {
#ifdef __LIBSTDCPP_PARTIAL_SUPPORT__
            v.push_back(record.fields()[j].v_string);
#else
            v.push_back(record.fields().at(j).v_string);
#endif
        }

        PalmLib::Record pdb_record(build_record(v));
        pdb_record.category(CATEGORY_DATA_RECORDS);
        pdb.appendRecord(pdb_record);
    }
}

unsigned
PalmLib::FlatFile::MobileDB::getMaxNumOfFields() const
{
    return 20;
}

bool
PalmLib::FlatFile::MobileDB::supportsFieldType(const Field::FieldType& type) const
{
    switch (type) {
    case Field::STRING:
        return true;
    default:
        return false;
    }
}

unsigned
PalmLib::FlatFile::MobileDB::getMaxNumOfListViews() const
{
    return 1;
}

void PalmLib::FlatFile::MobileDB::doneWithSchema()
{
    // Let the superclass have a chance.
    SUPERCLASS(PalmLib::FlatFile, Database, doneWithSchema, ());

    if (getNumOfListViews() < 1)
        throw PalmLib::error("a list view must be specified");

    PalmLib::FlatFile::ListView lv = getListView(0);

    if (lv.size() != getNumOfFields())
        throw PalmLib::error("the list view must have the same number of columns as fields");

    PalmLib::FlatFile::ListView::const_iterator p = lv.begin();
    unsigned field = 0;

    for (; p != lv.end(); ++p, ++field) {
        const PalmLib::FlatFile::ListViewColumn& col = (*p);
        if (field != col.field) {
            throw PalmLib::error("the list view columns must be in the same order as the fields");
        }
    }
}

void
MobileDB::setOption(const std::string& name, const std::string& value)
{
    if (name == "password") {
        if (! value.empty()) {
            m_password_hash = hash_password(value);
            SUPERCLASS(PalmLib::FlatFile,Database,setOption,
                       ("copy-prevention", "true"));
        }
    } else if (name == "find") {
        m_find_disabled = ! StrOps::string2boolean(value);
    } else if (name == "edit-on-select") {
        m_edit_on_select = StrOps::string2boolean(value);
    } else {
        SUPERCLASS(PalmLib::FlatFile,Database,setOption,(name, value));
    }
}

PalmLib::FlatFile::Database::options_list_t
PalmLib::FlatFile::MobileDB::getOptions(void) const
{
    typedef PalmLib::FlatFile::Database::options_list_t::value_type value;
    PalmLib::FlatFile::Database::options_list_t result =
        SUPERCLASS(PalmLib::FlatFile,Database,getOptions,());

    if (m_find_disabled)
        result.push_back(value("find", "false"));
    else
        result.push_back(value("find", "true"));

    if (m_edit_on_select)
        result.push_back(value("edit-on-select", "true"));
    else
        result.push_back(value("edit-on-select", "false"));

    return result;
}

// The following hash function was provided by the Mobile Generation support
// people as the function used to calcualte the hash of the password. This
// is done so that the clear text of the password is not stored in the
// database itself.
//
//
// // Computes a hash number from the text.
// // This function does not do enything sophisticated, as the whole security
// // scheme protects only against occasional glancing.
// // Many attacks are possible:
// //   looking at the plain pdb file
// //   patch this program to ignore passwords
// //   ...
// static ULong hashText( CharPtr text ) {
//         ULong tmp = 4711;
//         UInt i,len;
//         if( !text || (len=StrLen(text))==0 ) { return 0; }
//         for( i=0; i<len; i++ ) {
//                 tmp = tmp * 42731 + text[i] - 12899 * text[len-i-1];
//         }
//         return tmp;
// }

pi_uint32_t
PalmLib::FlatFile::MobileDB::hash_password(const std::string& text)
{
    pi_uint32_t tmp = 4711;

    if (text.length() == 0) return 0;

    for (std::string::size_type i = 0; i < text.length(); ++i) {
        tmp = tmp * 42731 + pi_int16_t(text[i])
            - pi_int16_t(12899 * text[text.length() - i - 1]);
    }

    return tmp;
}
