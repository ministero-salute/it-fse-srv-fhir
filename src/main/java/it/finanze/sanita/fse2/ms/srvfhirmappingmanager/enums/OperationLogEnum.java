/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums;

public enum OperationLogEnum implements ILogEnum {

	POST_XSL_TRANSFORM("POST_XSL_TRANSFORM", "Aggiunta XSLT"), 
	PUT_XSL_TRANSFORM("PUT_XSL_TRANSFORM", "Aggiornamento XSLT"), 
	DELETE_XSL_TRANSFORM("DELETE_XSL_TRANSFORM", "Cancellazione XSLT"), 
	QUERY_XSL_TRANSFORM("QUERY_XSL_TRANSFORM", "Query XSLT"),
	QUERY_BY_ID_XSL_TRANSFORM("QUERY_BY_ID_XSL_TRANSFORM", "Query XSLT by ID");


	
	private String code;
	
	public String getCode() {
		return code;
	}

	private String description;

	private OperationLogEnum(String inCode, String inDescription) {
		code = inCode;
		description = inDescription;
	}

	public String getDescription() {
		return description;
	}

}

