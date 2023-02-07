/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config;

/**
 * 
 *
 * Constants application.
 */
public final class Constants {

	 

	public static final class Profile {
		public static final String TEST = "test";
		public static final String TEST_PREFIX = "test_";


		/** 
		 * Constructor.
		 */
		private Profile() {
			//This method is intentionally left blank.
		}

	}


	public static final class App {

		public static final String JWT_TOKEN_AUDIENCE = "eds-semantic";

		public static final String MONGO_ID = "_id"; 

		public static final String FILENAME = "filename";

		public static final String TEMPLATE_ID_ROOT = "template_id_root"; 

		public static final String VERSION = "version"; 

		public static final String DELETED = "deleted"; 

		public static final String CONTENT_MULTIPART_FILE = "file";

		public static final String LAST_UPDATE_DATE = "last_update_date";

		public static final String INSERTION_DATE = "insertion_date";

		public static final String NAME_DEFINITION = "name_definition";
		
		public static final String NAME_MAP = "name_map";
		
		public static final String NAME_VALUESET = "name_valueset";


		private App() {
			//This method is intentionally left blank.
		}


	}

	public static final class Collections {
		public static final String TRANSFORM = "transform_eds";

		private Collections() {
		}
	}

	public static final class Logs {

		public static final String ERR_VAL_UNABLE_CONVERT = "Impossibile convertire %s nel tipo %s";
		
		public static final String ERROR_UNABLE_FIND_INSERTIONS = "Unable to retrieve change-set insertions"; 

		public static final String ERROR_UNABLE_FIND_DELETIONS = "Unable to retrieve change-set deletions"; 

		public static final String ERROR_EXECUTE_EXIST_VERSION_QUERY = "Error while execute exists by version query "; 

		public static final String ERROR_INSERTING_ETY = "Error inserting entity "; 
		
		public static final String ERR_VAL_FUTURE_DATE = "The last update date cannot be in the future";

		public static final String ERROR_DELETING_ETY = "Error deleting entity ";
		public static final String ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST = "The requested document does not exists";
		public static final String ERROR_INSERT_TRANSFORM = "Error while inserting transform";
		public static final String ERROR_UPDATE_TRANSFORM = "Error while updating transform";
		public static final String ERROR_FIND_TRANSFORM = "Error find by id ety transform :";
		public static final String ERROR_UPDATING_TRANSFORM = "Error while updating transform ";
		public static final String ERROR_FIND_ALL_TRANSFORM = "Error find all active ety transform :";
		public static final String ERROR_DOCUMENT_ALREADY_EXIST = "Cannot insert the given document, it already exists";

		public static final String ERR_REP_COUNT_ACTIVE_DOC = "Impossibile conteggiare ogni estensione attiva";
		
		public static final String ERR_ETY_BINARY_CONVERSION = "Impossibile convertire i dati binari nel formato richiesto (UTF-8)";

		private Logs() {
			//This method is intentionally left blank. 
		}
	}

	/**
	 *	Constants.
	 */
	private Constants() {

	}

}
