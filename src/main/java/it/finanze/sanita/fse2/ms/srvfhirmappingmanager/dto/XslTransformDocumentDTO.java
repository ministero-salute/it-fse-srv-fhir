package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto;


import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

import java.io.Serializable;
import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class XslTransformDocumentDTO implements Serializable {

	/**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 4887099482914213186L; 
	
	
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String id;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String templateIdRoot;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String nameXslTransform;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String contentXslTransform;
	@Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
	private String version;
	private OffsetDateTime lastUpdateDate;

	public static XslTransformDocumentDTO fromEntity(XslTransformETY e) {
		return new XslTransformDocumentDTO(
				e.getId(),
				e.getTemplateIdRoot(),
				e.getNameXslTransform(),
				UtilsMisc.encodeBase64(e.getContentXslTransform().getData()),
				e.getVersion(),
				UtilsMisc.convertToOffsetDateTime(e.getLastUpdateDate()));
	}

}
