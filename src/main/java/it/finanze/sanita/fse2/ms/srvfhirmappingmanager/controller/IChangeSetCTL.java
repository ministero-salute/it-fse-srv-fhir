/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.specs.TransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators.NoFutureDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.*;

/**
 * ChangeSet retriever controller
 */
@Tag(name = API_CHANGESET_TAG)
@Validated
public interface IChangeSetCTL {

	@Operation(summary = "Transform Status check",description = "a check about Transform")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",description = "Documents uploaded",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400",description = "Invalid parameters",content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500",description = "Internal Server Error",content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,schema = @Schema(implementation = ErrorResponseDTO.class)))})
	@GetMapping(API_CHANGESET_STATUS)
	ChangeSetResDTO<TransformCS> getTransformChangeSet(@RequestParam(value=API_QP_LAST_UPDATE, required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)@NoFutureDate(message = "The last update date cannot be in the future")Date lastUpdate)throws OperationException;


}
