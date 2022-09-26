package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_ARRAY_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_ARRAY_MIN_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for Change Set status endpoint response.
 *
 * @author Riccardo Bonesi
 * 
 */
@Getter
@Setter
public class ChangeSetResDTO<T> extends ResponseDTO {

	/**
     * Trace id log.
     */
    @Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
    private String traceID;

    /**
     * Span id log.
     */
    @Schema(maxLength = DEFAULT_STRING_MAX_SIZE)
    private String spanID;
	
	
	private Date lastUpdate;
	private Date timestamp;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO<T>> insertions;

	@ArraySchema(minItems = DEFAULT_ARRAY_MIN_SIZE, maxItems = DEFAULT_ARRAY_MAX_SIZE, uniqueItems = true)
	private List<ChangeSetDTO<T>> deletions;

	private int totalNumberOfElements;

	public ChangeSetResDTO() {
		super();
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
