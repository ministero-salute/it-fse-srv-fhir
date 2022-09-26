package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LogTraceInfoDTO implements AbstractDTO {

	/**
	 * Span.
	 */
	private final String spanID;
	
	/**
	 * Trace.
	 */
	private final String traceID;

}
