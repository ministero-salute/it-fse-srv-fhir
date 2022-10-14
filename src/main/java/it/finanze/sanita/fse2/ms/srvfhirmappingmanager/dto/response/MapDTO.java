package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import javax.validation.constraints.Size;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureMap;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MapDTO {

    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    @Size(min = 0, max = 1000)
    private String filenameMap;
    
    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    @Size(min = 0, max = 1000)
    private String nameMap;

    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    @Size(min = 0, max = 1000)
    private String contentMap;

    public static MapDTO fromEntity(StructureMap e) {
        return new MapDTO(
            e.getFilenameMap(),
            e.getNameMap(),
            UtilsMisc.encodeBase64(e.getContentMap().getData())
        );
    }
}
