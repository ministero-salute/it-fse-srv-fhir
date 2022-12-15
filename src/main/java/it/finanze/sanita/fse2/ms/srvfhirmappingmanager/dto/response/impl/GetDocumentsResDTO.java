/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import lombok.Getter;

import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_ARRAY_FILES_MAX;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_ARRAY_FILES_MIN;

@Getter
public class GetDocumentsResDTO extends ResponseDTO {

    @ArraySchema(
            minItems = OA_ARRAY_FILES_MIN,
            maxItems = OA_ARRAY_FILES_MAX,
            schema = @Schema(implementation = TransformDTO.class)
    )
    private final List<TransformDTO> items;

    private final long numberOfItems;

    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo The {@link LogTraceInfoDTO} instance
     * @param items     The available documents object
     */
    public GetDocumentsResDTO(LogTraceInfoDTO traceInfo, List<TransformDTO> items) {
        super(traceInfo);
        this.items = items;
        this.numberOfItems = items.size();
    }

}
