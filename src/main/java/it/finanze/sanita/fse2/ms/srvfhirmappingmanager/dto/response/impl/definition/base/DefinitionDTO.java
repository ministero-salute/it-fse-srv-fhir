package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.base;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.DefinitionETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class DefinitionDTO {

    private String id;
    private String filenameDefinition;
    private String nameDefinition;
    private String contentDefinition;
    private String versionDefinition;
    private OffsetDateTime lastUpdateDate;

    public static DefinitionDTO fromEntity(DefinitionETY e) {
        return new DefinitionDTO(
            e.getId(),
            e.getFilenameDefinition(),
            e.getNameDefinition(),
            UtilsMisc.encodeBase64(e.getContentDefinition().getData()),
            e.getVersionDefinition(),
            UtilsMisc.convertToOffsetDateTime(e.getLastUpdateDate())
        );
    }

}
