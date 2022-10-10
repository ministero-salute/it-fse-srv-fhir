package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureMap;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MapDTO {
    private String filenameMap;
    private String nameMap;
    private String contentMap;

    public static MapDTO fromEntity(StructureMap e) {
        return new MapDTO(
            e.getFilenameMap(),
            e.getNameMap(),
            UtilsMisc.encodeBase64(e.getContentMap().getData())
        );
    }
}
