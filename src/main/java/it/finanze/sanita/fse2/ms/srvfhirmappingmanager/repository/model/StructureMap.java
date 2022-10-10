package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StructureMap {
    private String nameMap;
    private String filenameMap;
    private Binary contentMap;

    public void setContentMap(MultipartFile file) throws DataProcessingException {
        try {
            this.contentMap = new Binary(file.getBytes());
        } catch (IOException e) {
            throw new DataProcessingException("Unable to encode multipart raw bytes into entity data", e);
        }
    }

    public static StructureMap fromMultipart(String name, MultipartFile file) throws DataProcessingException {
        StructureMap entity = new StructureMap();
        entity.setFilenameMap(file.getOriginalFilename());
        entity.setNameMap(name);
        entity.setContentMap(file);
        return entity;
    }
    
    public static StructureMap fromPath(Path path, MultipartFile file) throws DataProcessingException {
        StructureMap entity = new StructureMap();
        entity.setNameMap(path.getFileName().toString());
        entity.setContentMap(file);
        return entity;
    }
}
