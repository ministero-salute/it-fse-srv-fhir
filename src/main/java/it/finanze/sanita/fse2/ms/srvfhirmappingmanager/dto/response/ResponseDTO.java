package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import javax.validation.constraints.Size;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.AbstractDTO;
import lombok.Getter;

/**
 * Base response
 * @author CPIERASC
 */
@Getter
public class ResponseDTO implements AbstractDTO {

	/**
	 * Trace id log.
	 */
	@Size(max = 100)
	private String traceID;

	/**
	 * Span id log.
	 */
	@Size(max = 100)
	private String spanID;

	/**
	 * Instantiates a new response DTO.
	 */
	public ResponseDTO() {
	}

	/**
	 * Instantiates a new response DTO.
	 *
	 * @param traceInfo the trace info
	 */
	public ResponseDTO(final LogTraceInfoDTO traceInfo) {
		traceID = traceInfo.getTraceID();
		spanID = traceInfo.getSpanID();
	}

}