package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config;

/**
 * 
 * @author vincenzoingenito
 *
 * Constants application.
 */
public final class Constants {

	/**
	 *	Path scan.
	 */
	public static final class ComponentScan {

		/**
		 * Base path.
		 */
		public static final String BASE = "it.finanze.sanita.fse2.ms.srvfhirmappingmanager";

		/**
		 * Controller path.
		 */
		public static final String CONTROLLER = "it.sanita.srvfhirmappingmanager.controller";

		/**
		 * Service path.
		 */
		public static final String SERVICE = "it.sanita.srvfhirmappingmanager.service";

		/**
		 * Configuration path.
		 */
		public static final String CONFIG = "it.sanita.srvfhirmappingmanager.config";

		/**
		 * Configuration mongo path.
		 */
		public static final String CONFIG_MONGO = "it.sanita.srvfhirmappingmanager.config.mongo";

		/**
		 * Configuration mongo repository path.
		 */
		public static final String REPOSITORY_MONGO = "it.sanita.srvfhirmappingmanager.repository";	 

		private ComponentScan() {
			//This method is intentionally left blank.
		}

	}


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
		public static final String STRUCTURE_DEFINITION = "structure-definition";
		public static final String MAP = "structure-map";
		public static final String VALUESET = "structure-valueset";
		public static final String XSL_TRANSFORM = "xsl_transform";
		public static final String TRANSFORM = "transform";

		private Collections() {
		}
	}

	public static final class Logs {

		public static final String CALLED_API_POST_XSL_TRANSFORM = "Called POST /xslt"; 

		public static final String CALLED_API_PUT_XSL_TRANSFORM = "Called PUT /xslt"; 

		public static final String CALLED_API_GET_XSL_TRANSFORM = "Called GET /xslt"; 

		public static final String CALLED_API_GET_BY_ID_XSL_TRANSFORM = "Called GET /xslt/{id}"; 

		public static final String CALLED_API_DELETE_XSL_TRANSFORM = "Called DELETE /xslt"; 

		public static final String CALLED_API_QUERY_ROOT_EXTENSION = "Called GET /xslt by ID Root and Version";

		public static final String CALLED_API_QUERY_ID = "Called GET /xslt by ID";

		public static final String ERROR_INSERT_XSL_TRANSFORM = "Error inserting all ety xslTransform :"; 

		public static final String ERROR_FIND_XSL_TRANSFORM = "Error find by id ety xslTransform :"; 

		public static final String ERROR_UPDATING_XSL_TRANSFORM = "Error while updating XSLT "; 

		public static final String ERROR_XSL_TRANSFORM_ALREADY_PRESENT = "Error: xslTransform already present in the database"; 

		public static final String ERROR_FINDALL_XSL_TRANSFORM = "Error while getting xslTransforms :"; 

		public static final String ERROR_DELETE_XSL_TRANSFORM = "Error while deleting ety xslTransform :"; 

		public static final String ERROR_RETRIEVING_HOST_INFO = "Error while retrieving host informations"; 

		public static final String ERROR_RETRIEVING_INSERTIONS = "Error retrieving insertions"; 

		public static final String ERROR_RETRIEVING_MODIFICATIONS = "Error retrieving modifications"; 

		public static final String ERROR_UNABLE_FIND_INSERTIONS = "Unable to retrieve change-set insertions"; 

		public static final String ERROR_UNABLE_FIND_MODIFICATIONS = "Unable to retrieve change-set modifications"; 

		public static final String ERROR_UNABLE_FIND_DELETIONS = "Unable to retrieve change-set deletions"; 

		public static final String ERROR_EXECUTE_EXIST_VERSION_QUERY = "Error while execute exists by version query "; 

		public static final String ERROR_INSERTING_ETY = "Error inserting entity "; 

		public static final String ERROR_DELETING_ETY = "Error deleting entity "; 

		public static final String ERROR_COMPUTING_SHA = "Errore in fase di calcolo SHA-256"; 

		public static final String ERROR_JSON_HANDLING = "Errore gestione json :"; 

		public static final String ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST = "The requested document does not exists"; 

		public static final String ERROR_INVALID_OBJECT_ID = "The following string is not a valid object id: "; 

		public static final String ERROR_DOCUMENT_NOT_FOUND = "Error: document not found in collection";

		public static final String CALLED_API_QUERY_TRANSFORM_ROOT_EXTENSION = "Called GET /transform by ID Root and Version";
		public static final String CALLED_API_UPLOAD_TRANSFORM = "Called POST /transform";
		public static final String CALLED_API_UPDATE_TRANSFORM = "Called PUT /transform";
		public static final String CALLED_API_DELETE_TRANSFORM = "Called DELETE /transform";
		public static final String ERROR_INSERT_TRANSFORM = "Error while inserting transform";
		public static final String ERROR_UPDATE_TRANSFORM = "Error while updating transform";

		public static final String ERROR_FIND_TRANSFORM = "Error find by id ety transform :";
		public static final String ERROR_UPDATING_TRANSFORM = "Error while updating transform ";
		public static final String CALLED_API_GET_TRANSFORM = "Called GET /transform/id";
		public static final String CALLED_API_GET_ALL_TRANSFORM = "Called GET /transform";

		public static final String ERROR_FIND_ALL_TRANSFORM = "Error find all active ety transform :";
		public static final String CALLED_API_GET_ACTIVE_TRANSFORM = "Called GET /transform/active";
		public static final String ERROR_INSERT_VALUESET = "Unable to insert valueset by name";
		public static final String ERROR_DOCUMENT_ALREADY_EXIST = "Cannot insert the given document, it already exists";


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
