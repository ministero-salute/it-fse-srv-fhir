/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.info.LogTraceInfoDTO;
import lombok.Getter;

@Getter
public class DelDocsResDTO extends ResponseDTO {

	private final int deletedItems;

	public DelDocsResDTO(final LogTraceInfoDTO traceInfo, int deletedItems) {
		super(traceInfo);
		this.deletedItems = deletedItems;
	}
	
}
