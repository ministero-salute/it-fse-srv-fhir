/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.info.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.base.CrudInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class TransformResponseDTO.
 *
 * 
 * 	Transform Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PutDocsResDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857199886068379718L;

	private CrudInfoDTO updatedItems;

	public PutDocsResDTO(final LogTraceInfoDTO traceInfo, final CrudInfoDTO updatedItems) {
		super(traceInfo);
		this.updatedItems = updatedItems;
	}
	
}
