package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureValueset;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValuesetDTO {
    private String filenameValueset;
    private String nameValueset;
    private String contentValueset;

    public static ValuesetDTO fromEntity(StructureValueset e) {
        return new ValuesetDTO(
            e.getFilenameValueset(),
            e.getNameValueset(),
            UtilsMisc.encodeBase64(e.getContentValueset().getData())
        );
    }

}
