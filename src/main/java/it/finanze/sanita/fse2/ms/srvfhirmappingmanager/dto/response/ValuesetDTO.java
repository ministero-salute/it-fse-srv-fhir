/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureValueset;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValuesetDTO {

    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    private String filenameValueset;
    
    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    private String nameValueset;
    
    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    private String contentValueset;

    public static ValuesetDTO fromEntity(StructureValueset e) {
        return new ValuesetDTO(
            e.getFilenameValueset(),
            e.getNameValueset(),
            UtilsMisc.encodeBase64(e.getContentValueset().getData())
        );
    }

}
