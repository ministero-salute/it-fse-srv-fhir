package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity;

import java.util.Date;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Model to save xsl transform.
 */
@Document(collection = "#{@xslTransformBean}")
@Data
@NoArgsConstructor
public class XslTransformETY {

	@Id
	private String id;
	
	@Field(name = "template_id_root")
	private String templateIdRoot;
	
	@Field(name = "name_xsl_transform")
	private String nameXslTransform;
	
	@Field(name = "content_xsl_transform")
	private Binary contentXslTransform;
	
	@Field(name = "version")
	private String version;

	@Field(name = "insertion_date")
	private Date insertionDate; 
	
	@Field(name = "last_update_date")
	private Date lastUpdateDate;

	@Field(name = "deleted")
	private boolean deleted;
	
	
	public static XslTransformETY fromPath(String name,String id, String version, MultipartFile file) throws DataProcessingException {
		XslTransformETY entity = new XslTransformETY();
        Date now = new Date();
        entity.setNameXslTransform(name);
        entity.setId(id);
        entity.setInsertionDate(now);
        entity.setLastUpdateDate(now);
        entity.setDeleted(false);
        entity.setVersion(version);
        return entity;
    }
	 
}