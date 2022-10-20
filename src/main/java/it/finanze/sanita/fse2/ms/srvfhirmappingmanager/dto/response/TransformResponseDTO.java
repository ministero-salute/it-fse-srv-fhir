/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class XslTransformResponseDTO.
 *
 * @author Riccardo Bonesi
 * 
 * 	Xsl Transform Creation Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TransformResponseDTO extends ResponseDTO {
	 
	public TransformResponseDTO() {
		super();
	}
	
	public TransformResponseDTO(final LogTraceInfoDTO traceInfo) {
		super(traceInfo);
	}
}
