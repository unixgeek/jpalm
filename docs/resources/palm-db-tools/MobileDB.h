/*
 * palm-db-tools: Adaptor class for MobileDB databases
 * Copyright (C) 1999-2000 by Tom Dyas (tdyas@users.sourceforge.net)
 */

#ifndef __PALMLIB_FLATFILE_MOBILEDB_H__
#define __PALMLIB_FLATFILE_MOBILEDB_H__

#include <string>

#include "libpalm/Database.h"
#include "Database.h"

namespace PalmLib {
    namespace FlatFile {

	class MobileDB : public Database {
	public:
	    /**
             * Return true if this class can handle the given PalmOS
             * database.
             *
             * @param pdb PalmOS database to check for support.
             */
            static bool classify(PalmLib::Database& pdb);

            /**
             * Return true if this class is the database identified by
             * name.
             *
             * @param name A database type name to check.
             */
            static bool match_name(const std::string& name);

            /**
             * Default constructor for an initially empty database.
             */
            MobileDB()
                : Database("mdb"), m_password_hash(0), m_find_disabled(false),
                  m_edit_on_select(false)
                { }

            /**
             * Constructor which fills the flat-file structure from a
             * PalmOS database.
             */
            MobileDB(PalmLib::Database&);

            // destructor
            virtual ~MobileDB() { }

	    /**
             * After all processing to add fields and records is done,
             * outputPDB is called to create the actual file format
             * used by the flat-file database product.
             *
             * @param pdb An instance of PalmLib::Database.
             */
	    virtual void outputPDB(PalmLib::Database& pdb) const;

            /**
             * Return the maximum number of fields allowed in the
             * database. This class returns 0 since there is no limit.
	     */
            virtual unsigned getMaxNumOfFields() const;

            /**
             * Return true for the field types that this class
             * currently supports. Returns false otherwise.
             *
             * @param type The field type to check for support.
             */
            virtual bool supportsFieldType(const Field::FieldType& type) const;

            /**
             * Return the maximum number of views supported by this
             * type of flat-file database.
             */
            virtual unsigned getMaxNumOfListViews() const;

	    /**
	     * Hook the end of schema processing.
	     */
	    virtual void doneWithSchema();

            /**
             * Set a extra option.
             *
             * @param opt_name  The name of the option to set.
             * @param opt_value The value to assign to this option.
             */
	    virtual void setOption(const std::string& opt_name,
				   const std::string& opt_value);

	    /**
             * Get a list of extra options.
             */
	    virtual options_list_t getOptions(void) const;

	private:
	    // Convenience type for the record parsing functions.
	    typedef std::vector<std::string> mdb_record_t;

	    // Unpacked form of the application information block.
	    struct MobileAppInfoType {
		pi_uint16_t renamedCategories;
		std::string categoryLabels[16];
		pi_char_t categoryUniqueIDs[16];
		pi_char_t lastUniqueID;

		pi_uint16_t version;  // current header version
		pi_uint32_t lock;     // hash of password
		bool dontSearch;      // true, DB is invisible to global find
		bool editOnSelect;    // true, edit record on select
        
		struct FilterCriterion {
		    std::string text;  // the filter text the user entered
		    int field;         // field index, -1 means any field
		    pi_char_t flags;   // reserved
		} filter[3];

		struct SortCriterion {
		    int field;       // field index, -1 means unused criterion
		    bool descending; // direction to sort
		    pi_char_t type;  // low bit: 1 - numeric, 0 - lexical
		} sort[3];

		// Pack the above data into binary form.
		PalmLib::Block pack();

		// Unpack the binary form into the above data.
		void unpack(const PalmLib::Block& block);
	    };

	    pi_uint32_t m_password_hash;
	    bool m_find_disabled;
	    bool m_edit_on_select;

	    /**
	     * Search the provided PDB database for the metadata
	     * record identified by category "cat". Throws an
	     * exception if the record is not found or if multiple
	     * copies of the category exist.
	     *
	     * @param pdb Database to search for a metadata record.
	     * @param cat Category of the metadata record.
	     */
	    unsigned find_metadata_index(const PalmLib::Database& pdb,
					 pi_char_t cat) const;

	    /**
	     * Take the provided fields vector and produce a
	     * MobileDB-style record.
	     *
	     * @param fields A vector containing all of the field data.
	     */
	    PalmLib::Record build_record(const mdb_record_t& fields) const;

	    /**
	     * Parses the provided record and returns the fields in a
	     * vector. Throws an exception if the record is corrupt or
	     * invalid.
	     *
	     * @param rec The record to pull fields from.
	     */
	    mdb_record_t parse_record(const PalmLib::Record& rec) const;

	    pi_uint32_t hash_password(const std::string& password);

	};

    }
}

#endif
