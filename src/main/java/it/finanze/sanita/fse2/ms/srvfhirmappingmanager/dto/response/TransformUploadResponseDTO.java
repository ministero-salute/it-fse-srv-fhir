package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The Class XslTransformResponseDTO.
 *
 * @author Riccardo Bonesi
 * 
 *         Xsl Transform Creation Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TransformUploadResponseDTO extends ResponseDTO {

	@Schema(implementation = Map.class, description = "Map of the uploaded XSLT files", minLength = 1, maxLength = 1000)
	private Map<String, Integer> items;

	public TransformUploadResponseDTO() {
		super();
	}

	public TransformUploadResponseDTO(final LogTraceInfoDTO traceInfo, final Map<String, Integer> inItems) {
		super(traceInfo);
		items = inItems;
	}
}
