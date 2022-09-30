package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity;

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

@Document(collection = "#{@structureDefinitionBean}")
@Data
@NoArgsConstructor
public class DefinitionETY {

    public static final String FIELD_ID = "_id";
    public static final String FIELD_NAME = "name_definition";
    public static final String FIELD_FILENAME = "filename_definition";
    public static final String FIELD_CONTENT = "content_definition";
    public static final String FIELD_VERSION = "version_definition";

    @Id
    private String id;

    @Field(name = FIELD_NAME)
    private String nameDefinition;

    @Field(name = FIELD_FILENAME)
    private String filenameDefinition;

    @Field(name = FIELD_CONTENT)
    private Binary contentDefinition;

    @Field(name = FIELD_VERSION)
    private String versionDefinition;

    @Field(name = FIELD_INSERTION_DATE)
    private Date insertionDate;

    @Field(name = FIELD_LAST_UPDATE)
    private Date lastUpdateDate;

    @Field(name = FIELD_DELETED)
    private boolean deleted;

    public void setMultipartContentDefinition(MultipartFile file) throws DataProcessingException {
        try {
            this.contentDefinition = new Binary(file.getBytes());
        } catch (IOException e) {
            throw new DataProcessingException("Unable to encode multipart raw bytes into entity data", e);
        }
    }

    public static DefinitionETY fromMultipart(String name, String version, MultipartFile file) throws DataProcessingException {
        DefinitionETY entity = new DefinitionETY();
        Date now = new Date();
        entity.setFilenameDefinition(file.getOriginalFilename());
        entity.setNameDefinition(name);
        entity.setMultipartContentDefinition(file);
        entity.setVersionDefinition(version);
        entity.setInsertionDate(now);
        entity.setLastUpdateDate(now);
        return entity;
    }
    
    
    
    public static DefinitionETY fromPath(String name,String version, MultipartFile file) throws DataProcessingException {
    	DefinitionETY entity = new DefinitionETY();
        Date now = new Date();
        entity.setNameDefinition(name);
        entity.setFilenameDefinition(file.getOriginalFilename());
        entity.setMultipartContentDefinition(file);
        entity.setInsertionDate(now);
        entity.setLastUpdateDate(now);
        entity.setDeleted(false);
        entity.setVersionDefinition(version);
        return entity;
    }

}
