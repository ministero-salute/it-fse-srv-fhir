package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import java.util.Date;

import javax.validation.constraints.Size;

import org.bson.types.Binary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class XslTransformDTO {

	@Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String id;

	@Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String templateIdRoot;

	@Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String nameXslTransform;

	@Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE, type = "string", format = "binary", description = "XSLT content")
	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private Binary contentXslTransform;

	@Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
	@Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
	private String version;

	private Date insertionDate;

	private Date lastUpdate;

	@Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
	private boolean deleted;

}
