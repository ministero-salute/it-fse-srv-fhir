/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO.Options;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.info.LogTraceInfoDTO;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_ARRAY_FILES_MAX;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_ARRAY_FILES_MIN;

@Getter
public class GetDocsResDTO extends ResponseDTO {

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
     * @param list     The available documents object
     */
    public GetDocsResDTO(LogTraceInfoDTO traceInfo, List<TransformDTO> list, Options options) {
        super(traceInfo);
        this.items = applyOptions(list, options);
        this.numberOfItems = list.size();
    }
    
	private List<TransformDTO> applyOptions(List<TransformDTO> list, Options options) {
		return list.stream().map(d -> d.applyOptions(options)).collect(Collectors.toList());
	}


}
