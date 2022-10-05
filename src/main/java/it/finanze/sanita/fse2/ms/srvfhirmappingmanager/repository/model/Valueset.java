package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model;

import java.io.IOException;
import java.util.Date;

import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Valueset {
    private String filenameValueset;
    private String nameValueset;
    private Binary contentValueset;
    private Date insertionDate;
    private Date lastUpdateDate;
    private boolean deleted;

    public void setMultipartContentValueset(MultipartFile file) throws DataProcessingException {
        try {
            this.contentValueset = new Binary(file.getBytes());
        } catch (IOException e) {
            throw new DataProcessingException("Unable to encode multipart raw bytes into entity data", e);
        }
    }

    public static Valueset fromMultipart(String name, MultipartFile file) throws DataProcessingException {
        Valueset entity = new Valueset();
        Date now = new Date();
        entity.setFilenameValueset(file.getOriginalFilename());
        entity.setNameValueset(name);
        entity.setMultipartContentValueset(file);
        entity.setInsertionDate(now);
        entity.setLastUpdateDate(now);
        return entity;
    }
    
    
    public static Valueset fromPath(String name, MultipartFile file) throws DataProcessingException {
    	Valueset entity = new Valueset();
        Date now = new Date();
        entity.setNameValueset(name);
        entity.setFilenameValueset(file.getOriginalFilename());
        entity.setMultipartContentValueset(file);
        entity.setInsertionDate(now);
        entity.setLastUpdateDate(now);
        return entity;
    }

}
