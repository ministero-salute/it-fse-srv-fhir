/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.data;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.info.LogTraceInfoDTO;
import lombok.Getter;

@Getter
public class GetDocByIdResDTO extends ResponseDTO {

    @Schema(implementation = TransformDTO.class)
    private final TransformDTO document;

    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo The {@link LogTraceInfoDTO} instance
     * @param fhirDTO      The data object
     */
    public GetDocByIdResDTO(LogTraceInfoDTO traceInfo, TransformDTO fhirDTO) {
        super(traceInfo);
        this.document = fhirDTO;
    }
}
