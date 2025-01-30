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
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.*;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_ARRAY_FILES_MAX;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSetDTO {

	@Size(min = 0, max = DEFAULT_STRING_MAX_SIZE)
	@Pattern(regexp = "^[a-zA-Z0-9-_]+$")
	private String id;

	Payload description;

	@Value
	public static class Payload {
		/**
		 * The resource extension identifier
		 */
		@ArraySchema(
				minItems = OA_ARRAY_FILES_MIN,
				maxItems = OA_ARRAY_FILES_MAX,
				schema = @Schema(implementation = String.class)
		)
		List<@Size(min = OA_ANY_STRING_MIN, max = OA_ANY_STRING_MAX) String> templateIdRoot;
		/**
		 * The resource filename
		 */
		@Size(min = OA_ANY_STRING_MIN, max = OA_ANY_STRING_MAX)
		String version;
	}

}
