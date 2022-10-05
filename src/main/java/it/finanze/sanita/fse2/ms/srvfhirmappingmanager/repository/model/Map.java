package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model;

import java.io.IOException;
import java.nio.file.Path;
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
public class Map {
    private String nameMap;
    private String filenameMap;
    private Binary contentMap;
    private String templateIdRoot;
    private String templateIdExtension;
    private Date insertionDate;
    private Date lastUpdateDate;
    private boolean deleted;

    public void setContentMap(MultipartFile file) throws DataProcessingException {
        try {
            this.contentMap = new Binary(file.getBytes());
        } catch (IOException e) {
            throw new DataProcessingException("Unable to encode multipart raw bytes into entity data", e);
        }
    }

    public static Map fromMultipart(String name, String root, String extension, MultipartFile file) throws DataProcessingException {
        Map entity = new Map();
        Date now = new Date();
        entity.setFilenameMap(file.getOriginalFilename());
        entity.setNameMap(name);
        entity.setContentMap(file);
        entity.setTemplateIdRoot(root);
        entity.setTemplateIdExtension(extension);
        entity.setInsertionDate(now);
        entity.setLastUpdateDate(now);
        return entity;
    }
    
    public static Map fromPath(Path path, String extension, String root, MultipartFile file) throws DataProcessingException {
        Map entity = new Map();
        Date now = new Date();
        entity.setNameMap(path.getFileName().toString());
        entity.setContentMap(file);
        entity.setTemplateIdExtension(extension);
        entity.setTemplateIdRoot(root);
        entity.setInsertionDate(now);
        entity.setLastUpdateDate(now);
        return entity;
    }
}
