/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.base.types;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureMap;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

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

    @AllArgsConstructor
    public static class Options {
        private final boolean binary;
    }

    public static MapDTO fromEntity(StructureMap e) {
        return new MapDTO(
            e.getFilenameMap(),
            e.getNameMap(),
            UtilsMisc.encodeBase64(e.getContentMap().getData())
        );
    }

    public MapDTO applyOptions(Options o) {
        if(!o.binary) contentMap = null;
        return this;
    }
}