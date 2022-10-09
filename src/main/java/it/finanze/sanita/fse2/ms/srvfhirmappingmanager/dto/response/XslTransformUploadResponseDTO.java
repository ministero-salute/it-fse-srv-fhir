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
	 
	public XslTransformUploadResponseDTO() {
		super();
	}

	public XslTransformUploadResponseDTO(final LogTraceInfoDTO traceInfo, final Integer inInsertedXslt) {
		super(traceInfo);
		insertedXslt = inInsertedXslt;
	}
	
}
