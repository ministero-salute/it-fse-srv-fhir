package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Base response
 * @author CPIERASC
 */
@Getter
@Setter
public class ResponseDTO implements AbstractDTO {

	/**
	 * Trace id log.
	 */
	@Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
	private String traceID;

	/**
	 * Span id log.
	 */
	@Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
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