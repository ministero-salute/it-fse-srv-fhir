/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums;

public enum ErrorLogEnum implements ILogEnum {

	KO_XSLT_CREATE("KO_XSLT_CREATE", "Error while creating XSLT"),
	KO_XSLT_UPDATE("KO_XSLT_UPDATE", "Error while updating XSLT"),
	KO_XSLT_QUERY("KO_XSLT_QUERY", "Error while querying XSLT"),
	KO_XSLT_DELETE("KO_XSLT_CDELETE", "Error while deleting XSLT"),
	KO_XSLT_GET("KO_XSLT_GET", "Error while getting XSLT");

	private String code;
	
	public String getCode() {
		return code;
	}

	private String description;

	private ErrorLogEnum(String inCode, String inDescription) {
		code = inCode;
		description = inDescription;
	}

	public String getDescription() {
		return description;
	}

}

