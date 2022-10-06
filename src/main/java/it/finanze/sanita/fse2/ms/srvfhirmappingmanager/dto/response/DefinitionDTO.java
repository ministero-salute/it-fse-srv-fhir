package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureDefinition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefinitionDTO {
    private String filenameDefinition;
    private String nameDefinition;
    private String contentDefinition;
    private String versionDefinition;

    public static DefinitionDTO fromEntity(StructureDefinition e) {
        return new DefinitionDTO(
            e.getFilenameDefinition(),
            e.getNameDefinition(),
            UtilsMisc.encodeBase64(e.getContentDefinition().getData()),
            e.getVersionDefinition()
        );
    }

}
