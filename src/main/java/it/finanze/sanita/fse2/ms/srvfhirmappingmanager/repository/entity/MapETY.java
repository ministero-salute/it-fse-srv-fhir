package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IChangeSetRepo.FIELD_INSERTION_DATE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IChangeSetRepo.FIELD_LAST_UPDATE;

import java.io.IOException;
import java.nio.file.Path;
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

@Document(collection = "#{@structureMapBean}")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapETY {

    @Id
    private String id;

    @Field(name = "name_map")
    private String nameMap;

    @Field(name = "filename_map")
    private String filenameMap;

    @Field(name = "content_map")
    private Binary contentMap;

    @Field(name = "template_id_root")
    private String templateIdRoot;

    @Field(name = "template_id_extension")
    private String templateIdExtension;

    @Field(name = FIELD_INSERTION_DATE)
    private Date insertionDate;

    @Field(name = FIELD_LAST_UPDATE)
    private Date lastUpdateDate;

    @Field(name = "deleted")
    private boolean deleted;

    public void setContentMap(MultipartFile file) throws DataProcessingException {
        try {
            this.contentMap = new Binary(file.getBytes());
        } catch (IOException e) {
            throw new DataProcessingException("Unable to encode multipart raw bytes into entity data", e);
        }
    }

    public static MapETY fromMultipart(String name, String root, String extension, MultipartFile file) throws DataProcessingException {
        MapETY entity = new MapETY();
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
    
    public static MapETY fromPath(Path path, String extension, String root,MultipartFile file) throws DataProcessingException {
        MapETY entity = new MapETY();
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
