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
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc.encodeBase64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransformDTO {

    @Schema(maxLength = 255)
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "ID must contain only alphanumeric, dashes, or underscores")
    private String id;

    @Schema(maxLength = 255)
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "URI must contain only alphanumeric, dashes, or underscores")
    private String uri;

    @Schema(maxLength = 255)
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "Version must contain only alphanumeric, dashes, or underscores")
    private String version;

    @Schema(minLength = 1, maxLength = 100)
    @Size(min = 1, max = 100)
    private List<
                @Size(max = 255)
                @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "Template ID must contain only alphanumeric, dashes, or underscores")
                        String
                > templateIdRoot;

    @Schema(maxLength = 255)
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "Content must contain only alphanumeric, dashes, or underscores")
    private String content;

    @Schema(maxLength = 255)
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "Filename must contain only alphanumeric, dashes, or underscores")
    private String filename;

    @Schema(maxLength = 255)
    private FhirTypeEnum type;

    @Schema(format = "date-time", maxLength = 255)
    private Date insertionDate;

    @Schema(format = "date-time", maxLength = 255)
    private Date lastUpdateDate;

    private boolean deleted;

    @AllArgsConstructor
    public static class Options {
        private final boolean binary;
    }

	
    public static TransformDTO fromEntity(TransformETY e) {
        return new TransformDTO(
            e.getId(),
            e.getUri(),
            e.getVersion(),
            e.getTemplateIdRoot(),
            encodeBase64(e.getContent().getData()),
            e.getFilename(),
            e.getType(),
            e.getInsertionDate(),
            e.getLastUpdateDate(),
            e.isDeleted()
        );
    }


	public TransformDTO applyOptions(Options o) {
        if(!o.binary) content = null;
        return this;
	}
}
