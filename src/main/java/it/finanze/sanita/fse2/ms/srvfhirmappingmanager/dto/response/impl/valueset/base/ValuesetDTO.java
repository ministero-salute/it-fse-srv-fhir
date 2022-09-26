package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.base;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.ValuesetETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ValuesetDTO {

    private String id;
    private String filenameValueset;
    private String nameValueset;
    private String contentValueset;
    private OffsetDateTime lastUpdateDate;

    public static ValuesetDTO fromEntity(ValuesetETY e) {
        return new ValuesetDTO(
            e.getId(),
            e.getFilenameValueset(),
            e.getNameValueset(),
            UtilsMisc.encodeBase64(e.getContentValueset().getData()),
            UtilsMisc.convertToOffsetDateTime(e.getLastUpdateDate())
        );
    }

}
