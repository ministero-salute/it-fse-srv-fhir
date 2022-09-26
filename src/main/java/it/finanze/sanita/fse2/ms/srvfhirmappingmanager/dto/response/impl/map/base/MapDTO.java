package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.base;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class MapDTO {
    private String id;
    private String filenameMap;
    private String nameMap;
    private String contentMap;
    private String rootMap;
    private String extensionMap;
    private OffsetDateTime lastUpdateDate;

    public static MapDTO fromEntity(MapETY e) {
        return new MapDTO(
            e.getId(),
            e.getFilenameMap(),
            e.getNameMap(),
            UtilsMisc.encodeBase64(e.getContentMap().getData()),
            e.getTemplateIdRoot(),
            e.getTemplateIdExtension(),
            UtilsMisc.convertToOffsetDateTime(e.getLastUpdateDate())
        );
    }
}
