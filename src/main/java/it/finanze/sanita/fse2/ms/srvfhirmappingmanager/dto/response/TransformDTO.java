/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.base.types.DefinitionDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.base.types.MapDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.base.types.ValuesetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

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

    @AllArgsConstructor
    public static class Options {
        private final boolean binary;
    }

    public static TransformDTO fromEntity(TransformETY e) {
        return new TransformDTO(
            e.getId(),
            e.getTemplateIdRoot(),
            e.getVersion(),
            e.getInsertionDate(),
            e.getLastUpdateDate(),
            e.getStructureMaps().stream().map(MapDTO::fromEntity).collect(Collectors.toList()),
            e.getStructureValuesets().stream().map(ValuesetDTO::fromEntity).collect(Collectors.toList()),
            e.getStructureDefinitions().stream().map(DefinitionDTO::fromEntity).collect(Collectors.toList()),
            e.getRootStructureMap(),
            e.isDeleted()
        );
    }

    public TransformDTO applyOptions(Options o) {
        maps.forEach(m -> m.applyOptions(new MapDTO.Options(o.binary)));
        valuesets.forEach(v -> v.applyOptions(new ValuesetDTO.Options(o.binary)));
        definitions.forEach(d -> d.applyOptions(new DefinitionDTO.Options(o.binary)));
        return this;
    }
}
