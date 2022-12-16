/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.data;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import lombok.Getter;

@Getter
public class GetDocByIdResDTO extends ResponseDTO {

    @Schema(implementation = TransformDTO.class)
    private final TransformDTO document;

    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo The {@link LogTraceInfoDTO} instance
     * @param document      The data object
     */
    public GetDocByIdResDTO(LogTraceInfoDTO traceInfo, TransformDTO document) {
        super(traceInfo);
        this.document = document;
    }
}