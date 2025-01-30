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
import lombok.*;

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

		@ArraySchema(
				minItems = OA_ARRAY_FILES_MIN,
				maxItems = OA_ARRAY_FILES_MAX,
				schema = @Schema(implementation = TemplateIdRootItem.class)  // ✅ Uses wrapper class
		)
		List<TemplateIdRootItem> templateIdRoot;

		/**
		 * The resource filename
		 */
		@Size(min = OA_ANY_STRING_MIN, max = OA_ANY_STRING_MAX)
		@Schema(minLength = OA_ANY_STRING_MIN, maxLength = OA_ANY_STRING_MAX)  // ✅ Explicit constraints
				String version;
	}

	@Value
	@AllArgsConstructor
	public static class TemplateIdRootItem {

		@Getter
		@Schema(maxLength = 1000)
		String value;
	}
}
