/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.info.LogTraceInfoDTO;
import lombok.Getter;

@Getter
public class PutDocsResDTO extends ResponseDTO {

	private final int updatedItems;

	public PutDocsResDTO(final LogTraceInfoDTO traceInfo, int updatedItems) {
		super(traceInfo);
		this.updatedItems = updatedItems;
	}
	
}
