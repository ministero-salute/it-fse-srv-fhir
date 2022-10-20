/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.DefinitionDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.MapDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ValuesetDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransformDTO {
    
    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    private String id;

    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    private String templateIdRoot;

    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    private String version;

    private Date insertionDate;

    private Date lastUpdateDate;

    @ArraySchema(minItems = 0, maxItems = 1000)
    private List<MapDTO> maps;

    @ArraySchema(minItems = 0, maxItems = 1000)
    private List<ValuesetDTO> valuesets;

    @ArraySchema(minItems = 0, maxItems = 1000)
    private List<DefinitionDTO> definitions;
    
    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
    private String rootMap;

    private boolean deleted;
}
