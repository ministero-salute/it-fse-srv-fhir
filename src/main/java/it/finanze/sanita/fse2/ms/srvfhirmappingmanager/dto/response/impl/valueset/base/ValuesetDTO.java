package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.base;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Valueset;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ValuesetDTO {
    private String filenameValueset;
    private String nameValueset;
    private String contentValueset;
    private OffsetDateTime lastUpdateDate;

    public static ValuesetDTO fromEntity(Valueset e) {
        return new ValuesetDTO(
            e.getFilenameValueset(),
            e.getNameValueset(),
            UtilsMisc.encodeBase64(e.getContentValueset().getData()),
            UtilsMisc.convertToOffsetDateTime(e.getLastUpdateDate())
        );
    }

}
