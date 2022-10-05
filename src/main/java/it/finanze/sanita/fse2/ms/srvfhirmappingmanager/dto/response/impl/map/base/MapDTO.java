package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.base;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Map;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class MapDTO {
    private String filenameMap;
    private String nameMap;
    private String contentMap;
    private String rootMap;
    private String extensionMap;
    private OffsetDateTime lastUpdateDate;

    public static MapDTO fromEntity(Map e) {
        return new MapDTO(
            e.getFilenameMap(),
            e.getNameMap(),
            UtilsMisc.encodeBase64(e.getContentMap().getData()),
            e.getTemplateIdRoot(),
            e.getTemplateIdExtension(),
            UtilsMisc.convertToOffsetDateTime(e.getLastUpdateDate())
        );
    }
}
