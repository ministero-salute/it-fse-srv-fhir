/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
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
public class DelDocsResDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857199886068379718L;

	private CrudInfoDTO deletedItems;

	public DelDocsResDTO(final LogTraceInfoDTO traceInfo, final CrudInfoDTO deletedItems) {
		super(traceInfo);
		this.deletedItems = deletedItems;
	}
	
}
