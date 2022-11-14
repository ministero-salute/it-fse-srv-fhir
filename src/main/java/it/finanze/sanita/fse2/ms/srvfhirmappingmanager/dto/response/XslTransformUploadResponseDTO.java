/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class XslTransformResponseDTO.
 *
 * 
 * 	Xsl Transform Creation Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class XslTransformUploadResponseDTO extends ResponseDTO {

	@Schema(description = "Number of inserted XSLT")
	@Min(value = 1)
	@Max(value = 1000)
	private Integer insertedXslt;

	@Schema(description = "Number of updated XSLT")
	@Min(value = 1)
	@Max(value = 1000)
	private Integer updatedXslt;

	@Schema(description = "Number of deleted XSLT")
	@Min(value = 1)
	@Max(value = 1000)
	private Integer deletedXslt;
	 
	public XslTransformUploadResponseDTO() {
		super();
	}

	public XslTransformUploadResponseDTO(LogTraceInfoDTO traceInfo, Integer insertedXslt, Integer updatedXslt, Integer deletedXslt) {
		super(traceInfo);
		this.insertedXslt = insertedXslt;
		this.updatedXslt = updatedXslt;
		this.deletedXslt = deletedXslt;
	}
}
