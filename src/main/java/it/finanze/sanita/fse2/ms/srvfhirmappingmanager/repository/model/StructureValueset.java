package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model;

import java.io.IOException;

import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StructureValueset {
    private String filenameValueset;
    private String nameValueset;
    private Binary contentValueset;

    public void setMultipartContentValueset(MultipartFile file) throws DataProcessingException {
        try {
            this.contentValueset = new Binary(file.getBytes());
        } catch (IOException e) {
            throw new DataProcessingException("Unable to encode multipart raw bytes into entity data", e);
        }
    }

    public static StructureValueset fromMultipart(String name, MultipartFile file) throws DataProcessingException {
        StructureValueset entity = new StructureValueset();
        entity.setFilenameValueset(file.getOriginalFilename());
        entity.setNameValueset(name);
        entity.setMultipartContentValueset(file);
        return entity;
    }
    
    
    public static StructureValueset fromPath(String name, MultipartFile file) throws DataProcessingException {
    	StructureValueset entity = new StructureValueset();
        entity.setNameValueset(name);
        entity.setFilenameValueset(file.getOriginalFilename());
        entity.setMultipartContentValueset(file);
        return entity;
    }

}
