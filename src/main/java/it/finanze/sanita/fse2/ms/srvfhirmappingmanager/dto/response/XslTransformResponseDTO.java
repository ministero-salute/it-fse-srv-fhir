package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import javax.validation.constraints.Size;

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

	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String transactionId;

	public XslTransformResponseDTO() {
		super();
	}

	public XslTransformResponseDTO(final LogTraceInfoDTO traceInfo, final String inTransactionId) {
		super(traceInfo);
		transactionId = inTransactionId;
	}
	
}
