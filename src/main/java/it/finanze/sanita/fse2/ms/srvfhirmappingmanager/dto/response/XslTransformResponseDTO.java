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
public class XslTransformResponseDTO extends ResponseDTO {

	 
	public XslTransformResponseDTO() {
		super();
	}

	public XslTransformResponseDTO(final LogTraceInfoDTO traceInfo) {
		super(traceInfo);
	}
	
}
