package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Data
@NoArgsConstructor
public class Definition {

    private String nameDefinition;
    private String filenameDefinition;
    private Binary contentDefinition;
    private String versionDefinition;
    private Date insertionDate;
    private Date lastUpdateDate;
    private boolean deleted;

    public void setMultipartContentDefinition(MultipartFile file) throws DataProcessingException {
        try {
            this.contentDefinition = new Binary(file.getBytes());
        } catch (IOException e) {
            throw new DataProcessingException("Unable to encode multipart raw bytes into entity data", e);
        }
    }

    public static Definition fromMultipart(String name, String version, MultipartFile file) throws DataProcessingException {
        Definition entity = new Definition();
        Date now = new Date();
        entity.setFilenameDefinition(file.getOriginalFilename());
        entity.setNameDefinition(name);
        entity.setMultipartContentDefinition(file);
        entity.setVersionDefinition(version);
        entity.setInsertionDate(now);
        entity.setLastUpdateDate(now);
        return entity;
    }
    
    
    
    public static Definition fromPath(String name, String version, MultipartFile file) throws DataProcessingException {
    	Definition entity = new Definition();
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
