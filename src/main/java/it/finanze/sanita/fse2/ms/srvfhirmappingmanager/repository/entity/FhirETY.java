package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity;

import java.io.IOException;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to save generic transform.
 */
@Document(collection = "#{@transformBean}")
@Data
@NoArgsConstructor
public class FhirETY {
	
	public static final String FIELD_TEMPLATE_ID_ROOT = "template_id_root";
	public static final String FIELD_VERSION = "version";
	public static final String FIELD_URI = "uri";
	public static final String FIELD_FILENAME = "filename";
	public static final String FIELD_CONTENT = "content";
	public static final String FIELD_TYPE = "type";

	@Id
	private String id;
	
	@Field(name = FIELD_TEMPLATE_ID_ROOT)
	private String templateIdRoot;
	
	@Field(name = FIELD_VERSION)
	private String version;

	@Field(name = FIELD_CONTENT)
	private Binary content;
	
	@Field(name = FIELD_URI )
	private String uri;
	
	@Field(name = FIELD_TYPE)
	private FhirTypeEnum type;
	
	@Field(name = FIELD_FILENAME)
	private String filename;
		
	public void setContent(MultipartFile file) throws DataProcessingException {
	    try {
	        this.content = new Binary(file.getBytes());
	    } catch (IOException e) {
	        throw new DataProcessingException(Logs.ERR_ETY_BINARY_CONVERSION, e);
	    }
	}
	
	public static FhirETY fromComponents(String root, String uri, String version,  MultipartFile file, FhirTypeEnum type) throws DataProcessingException {
		FhirETY entity = new FhirETY();
		entity.setTemplateIdRoot(root);
		entity.setVersion(version);
		entity.setFilename(file.getOriginalFilename());
		entity.setContent(file);
		entity.setUri(uri);
		entity.setType(type);
		return entity;
	}
	

}
