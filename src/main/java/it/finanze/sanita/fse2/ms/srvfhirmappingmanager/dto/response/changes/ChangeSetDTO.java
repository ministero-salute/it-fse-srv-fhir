/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Size;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_ANY_STRING_MAX;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_ANY_STRING_MIN;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSetDTO {

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String id;

	Payload description;

	@Value
	public static class Payload {
		/**
		 * The resource extension identifier
		 */
		@Size(min = OA_ANY_STRING_MIN, max = OA_ANY_STRING_MAX)
		List<String> templateIdRoot;
		/**
		 * The resource filename
		 */
		@Size(min = OA_ANY_STRING_MIN, max = OA_ANY_STRING_MAX)
		String version;
	}

}
