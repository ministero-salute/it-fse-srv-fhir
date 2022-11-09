/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_ARRAY_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_ARRAY_MIN_SIZE;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;


import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for Change Set status endpoint response.
 *
 * 
 */
@Getter
@Setter
public class ChangeSetResDTO<T> extends ResponseDTO {

	private Date lastUpdate;
	private Date timestamp;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO<T>> insertions;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO<T>> deletions;

    @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = 10000)
	@Size(min = 0, max = 10000)
	private int totalNumberOfElements;

	public ChangeSetResDTO() {
		super();
	}

	public void setTraceId(String traceId) {
		super.setTraceID(traceId);
	}

	public ChangeSetResDTO(final LogTraceInfoDTO traceInfo, final Date inLastUpdate, final Date inTimestamp, final List<ChangeSetDTO<T>> inInsertions, final List<ChangeSetDTO<T>> inDeletions, final int inTotalNumberOfElements) {
		super(traceInfo);
		this.lastUpdate = inLastUpdate;
		this.timestamp = inTimestamp;
		this.insertions = inInsertions;
		this.deletions = inDeletions;
		this.totalNumberOfElements = inTotalNumberOfElements;
	}
	
}
