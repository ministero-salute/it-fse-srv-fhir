package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IChangeSetRepo.*;

/**
 * Model to save generic transform.
 */
@Document(collection = "#{@transformBean}")
@Data
@NoArgsConstructor
public class TransformETY {
	
	public static final String FIELD_TEMPLATE_ID_ROOT = "template_id_root";
	public static final String FIELD_VERSION = "version";
	public static final String FIELD_URI = "uri";
	public static final String FIELD_FILENAME = "filename";
	public static final String FIELD_CONTENT = "content";
	public static final String FIELD_TYPE = "type";

	@Id
	private String id;

	@Field(name = FIELD_FILENAME)
	private String filename;

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

	@Field(name = FIELD_INSERTION_DATE)
	private Date insertionDate;

	@Field(name = FIELD_LAST_UPDATE)
	private Date lastUpdateDate;

	@Field(name = FIELD_DELETED)
	private boolean deleted;

	public void setContent(MultipartFile file) throws DataProcessingException {
	    try {
	        this.content = new Binary(file.getBytes());
	    } catch (IOException e) {
	        throw new DataProcessingException(Logs.ERR_ETY_BINARY_CONVERSION, e);
	    }
	}
	
	public static TransformETY fromComponents(String uri, String version, String root, FhirTypeEnum type, MultipartFile file) throws DataProcessingException {
		Date date = new Date();
		TransformETY e = new TransformETY();
		e.setUri(uri);
		e.setVersion(version);
		if (type == FhirTypeEnum.Map) e.setTemplateIdRoot(root);
		e.setType(type);
		e.setFilename(file.getOriginalFilename());
		e.setContent(file);
		e.setInsertionDate(date);
		e.setLastUpdateDate(date);
		e.setDeleted(false);
		return e;
	}
	

}
