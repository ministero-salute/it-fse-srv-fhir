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
public class XslTransformUploadResponseDTO extends ResponseDTO {
	private Integer insertedXslt;
	private Integer updatedXslt;
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
