/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright 2023 Ministero della Salute
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
