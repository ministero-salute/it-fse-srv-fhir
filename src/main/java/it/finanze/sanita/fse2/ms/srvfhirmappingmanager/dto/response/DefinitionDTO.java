package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import javax.validation.constraints.Size;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureDefinition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefinitionDTO {
    
    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    @Size(min = 0, max = 1000)
    private String filenameDefinition;
    
    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    @Size(min = 0, max = 1000)
    private String nameDefinition;
    
    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    @Size(min = 0, max = 1000)
    private String contentDefinition;
    
    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    @Size(min = 0, max = 1000)
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
