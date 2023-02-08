/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.data.GetDocByIdResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.GetDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.PutDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators.ValidObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.IOException;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs.*;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Regex.REG_VERSION;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.*;

/**
 * Transform Controller
 */
@RequestMapping(path = API_TRANSFORM_MAPPER)
@Tag(name = API_TRANSFORM_TAG)
@Validated
public interface ITransformCTL {

        @PostMapping(
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
        )
        @Operation(summary = "Aggiunta entità FHIR su MongoDB")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Creazione entità avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDocsResDTO.class))),
                @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(responseCode = "409", description = "Conflitto riscontrato sulla risorsa", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
        })
        @ResponseStatus(HttpStatus.CREATED)
        PostDocsResDTO uploadTransform(
                @RequestPart(API_PATH_URI_VAR)
                @NotBlank(message = ERR_VAL_URI_BLANK)
                String uri,
                @RequestPart(API_PATH_VERSION_VAR)
                @NotBlank(message = ERR_VAL_VERSION_BLANK)
                @Pattern(message = ERR_VAL_VERSION_INVALID, regexp = REG_VERSION)
                String version,
                @RequestParam(API_PATH_TYPE_VAR)
                FhirTypeEnum type,
                @RequestPart(value = API_PATH_TEMPLATE_ID_ROOT_VAR, required = false)
                @Parameter(description = VAL_DESC_ROOT)
                String templateIdRoot,
                @RequestPart(API_PATH_FILE_VAR)
                MultipartFile file
        ) throws IOException, OperationException, DocumentAlreadyPresentException, InvalidContentException;

        @PutMapping(
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
        )
        @Operation(summary = "Aggiornamento entità FHIR su MongoDB")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Aggiornamento trasformata avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PutDocsResDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Risorsa non trovata", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Conflitto riscontrato sulla risorsa", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
        })
        PutDocsResDTO updateTransform(
            @RequestPart(API_PATH_URI_VAR)
            @NotBlank(message = ERR_VAL_URI_BLANK)
            String uri,
            @RequestPart(API_PATH_VERSION_VAR)
            @NotBlank(message = ERR_VAL_VERSION_BLANK)
            @Pattern(message = ERR_VAL_VERSION_INVALID, regexp = REG_VERSION)
            String version,
            @RequestPart(API_PATH_FILE_VAR)
            MultipartFile file
        ) throws OperationException, DocumentNotFoundException, InvalidVersionException, DataProcessingException, InvalidContentException;

        @DeleteMapping(
            produces = { MediaType.APPLICATION_JSON_VALUE }
        )
        @Operation(summary = "Rimozione entità FHIR su MongoDB")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cancellazione avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DelDocsResDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Risorsa non trovata", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
        })
        DelDocsResDTO deleteTransform(
            @RequestParam(API_PATH_URI_VAR)
            @NotBlank(message = ERR_VAL_URI_BLANK)
            String uri
        ) throws DocumentNotFoundException, OperationException;

        @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Restituzione entità FHIR per URI")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Richiesta avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetDocsResDTO.class))),
                @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(responseCode = "404", description = "Risorsa non trovata", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
        })
        GetDocsResDTO getTransformByUri(
           @RequestParam(API_PATH_URI_VAR)
           @NotBlank(message = ERR_VAL_URI_BLANK)
           String uri,
           @RequestParam(value = API_QP_BINARY, defaultValue = "false")
           @Parameter(description = "Include binary content")
           boolean binary,
           @RequestParam(value = API_QP_INCLUDE_DELETED, defaultValue = "false")
           @Parameter(description = "Include deleted content")
           boolean deleted
        ) throws DocumentNotFoundException, OperationException;

        @GetMapping(value = API_GET_ONE_BY_ID, produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Restituzione entità FHIR per ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Richiesta avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetDocByIdResDTO.class))),
                @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(responseCode = "404", description = "Risorsa non trovata", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
        })
        GetDocByIdResDTO getTransformById(
                @PathVariable(API_PATH_ID_VAR)
                @NotBlank(message = ERR_VAL_ID_BLANK)
                @ValidObjectId(message = ERR_VAL_ID_NOT_VALID)
                String id
        ) throws OperationException, DocumentNotFoundException;

        @GetMapping(value = API_PATH_ALL_VAR, produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Restituzione di tutte le entità FHIR disponibili")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetDocsResDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
        })
        GetDocsResDTO getTransform(
            @RequestParam(value = API_QP_BINARY, defaultValue = "false") @Parameter(description = "Include binary content") boolean binary,
            @RequestParam(value = API_QP_INCLUDE_DELETED, defaultValue = "false") @Parameter(description = "Include deleted schema") boolean deleted
        ) throws OperationException;

}
