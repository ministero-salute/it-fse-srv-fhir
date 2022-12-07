/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
public class ChangeSetResDTO<T> {

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
	private List<ChangeSetDTO<T>> insertions;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO<T>> deletions;

    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = 10000)
	@Size(min = 0, max = 10000)
	private long totalNumberOfElements;

	private long collectionSize;

}
