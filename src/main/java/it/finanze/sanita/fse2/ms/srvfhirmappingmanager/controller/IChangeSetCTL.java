package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;


import java.util.Date;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.DefinitionCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.MapCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.ValuesetCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.XSLTransformCS;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators.NoFutureDate;

/**
 * ChangeSet retriever controller
 *
 * @author Riccardo Bonesi
 */
@RequestMapping(path = "/v1/changeset")
@Tag(name = "Change Set controller")
@Validated
public interface IChangeSetCTL {

	@Operation(summary = "XSLT ChangeSet status", description = "Creazione lista ChangeSet dei file XSLT")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Operazione eseguita correttamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@GetMapping("/xslt/status")
	ChangeSetResDTO<XSLTransformCS> getXslTransformChangeSet(
		@RequestParam(value="lastUpdate", required = false)
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@NoFutureDate(message = "The last update date cannot be in the future")
		Date lastUpdate
	)
	throws OperationException;

	@GetMapping("/valueset/status")
	ChangeSetResDTO<ValuesetCS> getValuesetChangeset(
		@RequestParam(value="lastUpdate", required = false)
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@NoFutureDate(message = "The last update date cannot be in the future")
		Date lastUpdate
	) throws OperationException;

	@GetMapping("/definition/status")
	ChangeSetResDTO<DefinitionCS> getDefinitionChangeset(
		@RequestParam(value="lastUpdate", required = false)
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@NoFutureDate(message = "The last update date cannot be in the future")
		Date lastUpdate
	) throws OperationException;

	@GetMapping("/map/status")
	ChangeSetResDTO<MapCS> getMapChangeset(
		@RequestParam(value="lastUpdate", required = false)
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@NoFutureDate(message = "The last update date cannot be in the future")
		Date lastUpdate
	) throws OperationException;

}
