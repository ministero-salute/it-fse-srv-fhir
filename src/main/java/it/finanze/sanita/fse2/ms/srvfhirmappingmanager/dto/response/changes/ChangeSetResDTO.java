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
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.*;

/**
 * DTO for Change Set status endpoint response.
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSetResDTO {

	/**
	 * Trace id log.
	 */
	@Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
	private String traceID;

	/**
	 * Span id log.
	 */
	@Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
	private String spanID;

	private Date lastUpdate;
	private Date timestamp;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO> insertions;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO> deletions;

    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = 10000)
	@Size(min = 0, max = 10000)
	private long totalNumberOfElements;

	private long collectionSize;

}
