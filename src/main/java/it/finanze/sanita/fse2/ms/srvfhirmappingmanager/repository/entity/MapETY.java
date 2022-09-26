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

@Document(collection = "#{@structureMapBean}")
@Data
@NoArgsConstructor
public class MapETY {

    public static final String FIELD_ID = "_id";
    public static final String FIELD_NAME = "name_map";
    public static final String FIELD_FILENAME = "filename_map";
    public static final String FIELD_CONTENT = "content_map";
    public static final String FIELD_ID_ROOT = "template_id_root";
    public static final String FIEDL_ID_EXTS = "template_id_extension";

    @Id
    private String id;

    @Field(name = FIELD_NAME)
    private String nameMap;

    @Field(name = FIELD_FILENAME)
    private String filenameMap;

    @Field(name = FIELD_CONTENT)
    private Binary contentMap;

    @Field(name = FIELD_ID_ROOT)
    private String templateIdRoot;

    @Field(name = FIEDL_ID_EXTS)
    private String templateIdExtension;

    @Field(name = FIELD_INSERTION_DATE)
    private Date insertionDate;

    @Field(name = FIELD_LAST_UPDATE)
    private Date lastUpdateDate;

    @Field(name = FIELD_DELETED)
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
}
