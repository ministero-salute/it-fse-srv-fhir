/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright 2023 Ministero della Salute
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

	public static final class Collections {
		public static final String TRANSFORM = "transform_eds";

		private Collections() {
		}
	}

	public static final class Regex {
		public static final String REG_VERSION = "^(\\d+\\.)(\\d+)$";

		private Regex() {
			//This method is intentionally left blank.
		}
	}

	public static final class Logs {

		// Descriptions
		public static final String VAL_DESC_ROOT = "Questo campo ha effetto solo per le mappe";

		// Validation
		public static final String ERR_VAL_ID_BLANK = "L'identificatore documento non può essere vuoto";
		public static final String ERR_VAL_ID_NOT_VALID = "L'identificatore documento non è valido";
		public static final String ERR_VAL_URI_BLANK = "Il campo 'uri' non può essere vuoto";
		public static final String ERR_VAL_VERSION_BLANK = "Il campo 'version' non può essere vuoto";
		public static final String ERR_VAL_VERSION_INVALID = "Il campo 'version' non rispetta il formato previsto";
		public static final String ERR_VAL_FILES_INVALID = "Il 'file' fornito sembra invalido";
		public static final String ERR_VAL_FUTURE_DATE = "La data del ultimo aggiornamento non può essere nel futuro";
		public static final String ERR_VAL_UNABLE_CONVERT = "Impossibile convertire %s nel tipo %s";

		// Services
		public static final String ERR_SRV_ROOT_ITEMS_INVALID = "Una o più root map indicate sono invalide oppure la lista è vuota";
		public static final String ERR_SRV_ROOT_ALREADY_EXIST = "Uno o più root id sono già in uso ad un altra risorsa";
		public static final String ERR_SRV_DOC_ALREADY_EXIST = "Una versione dello stesso documento è già presenti";
		public static final String ERR_SRV_DOC_NOT_EXIST = "Il documento richiesto non esiste";
		public static final String ERR_SRV_VERSION_MISMATCH = "Versione non valida: %s. La versione deve essere maggiore di %s";

		// Repositories
		public static final String ERR_REP_FIND_BY_ID = "Impossibile cercare documento per OID";
		public static final String ERR_REP_FIND_BY_URI = "Impossibile cercare documento per URI";
		public static final String ERR_REP_FIND_BY_ANY = "Impossibile cercare tra tutti i documenti";
		public static final String ERR_REP_FIND_BY_ANY_ACTIVE = "Impossibile cercare tra tutti i documenti attivi";
		public static final String ERR_REP_FIND_BY_ROOT = "Impossibile cercare documento per root id";
		public static final String ERR_REP_COUNT_ACTIVE_DOC = "Impossibile conteggiare ogni estensione attiva";
		public static final String ERR_REP_INS_DOCS_BY_URI = "Impossibile inserire il documento richiesto";
		public static final String ERR_REP_DEL_DOCS_BY_URI = "Impossibile cancellare i documenti richiesti";
		public static final String ERR_REP_CHANGESET_INSERT = "Impossibile recuperare il change-set degli inserimenti";
		public static final String ERR_REP_CHANGESET_DELETE = "Impossibile recuperare il change-set delle cancellazioni";

		// Entities
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
