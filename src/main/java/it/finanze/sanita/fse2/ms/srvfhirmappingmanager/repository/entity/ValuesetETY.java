package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IChangeSetRepo.FIELD_INSERTION_DATE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IChangeSetRepo.FIELD_LAST_UPDATE;

import java.io.IOException;
import java.util.Date;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "#{@valueSetBean}")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValuesetETY {

    public static final String FIELD_ID = "_id";
//    public static final String FIELD_NAME = "name_valueset";
    public static final String FIELD_FILENAME = "filename_valueset";
    public static final String FIELD_CONTENT = "content_valueset";

    @Id
    private String id;

    @Field(name = FIELD_FILENAME)
    private String filenameValueset;

    @Field(name = "name_valueset")
    private String nameValueset;

    @Field(name = FIELD_CONTENT)
    private Binary contentValueset;

    @Field(name = FIELD_INSERTION_DATE)
    private Date insertionDate;

    @Field(name = FIELD_LAST_UPDATE)
    private Date lastUpdateDate;

    @Field(name = "deleted")
    private boolean deleted;

    public void setMultipartContentValueset(MultipartFile file) throws DataProcessingException {
        try {
            this.contentValueset = new Binary(file.getBytes());
        } catch (IOException e) {
            throw new DataProcessingException("Unable to encode multipart raw bytes into entity data", e);
        }
    }

    public static ValuesetETY fromMultipart(String name,MultipartFile file) throws DataProcessingException {
        ValuesetETY entity = new ValuesetETY();
        Date now = new Date();
        entity.setFilenameValueset(file.getOriginalFilename());
        entity.setNameValueset(name);
        entity.setMultipartContentValueset(file);
        entity.setInsertionDate(now);
        entity.setLastUpdateDate(now);
        return entity;
    }
    
    
    public static ValuesetETY fromPath(String name,MultipartFile file) throws DataProcessingException {
    	ValuesetETY entity = new ValuesetETY();
        Date now = new Date();
        entity.setNameValueset(name);
        entity.setFilenameValueset(file.getOriginalFilename());
        entity.setMultipartContentValueset(file);
        entity.setInsertionDate(now);
        entity.setLastUpdateDate(now);
        return entity;
    }

}
