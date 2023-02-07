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
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.PutDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.GetDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators.ValidObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.*;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_EXTS_STRING_MAX;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_EXTS_STRING_MIN;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

/**
 * Transform Controller
 */
@RequestMapping(path = API_TRANSFORM_MAPPER)
@Tag(name = API_TRANSFORM_TAG)
@Validated
public interface ITransformCTL {

        @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
                        MediaType.MULTIPART_FORM_DATA_VALUE })
        @Operation(summary = "Add transform to MongoDB", description = "Servizio che consente di aggiungere una trasformata alla base dati.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Creazione trasformata avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PostDocsResDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Conflitto riscontrato sulla risorsa", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        @ResponseStatus(HttpStatus.CREATED)
        PostDocsResDTO uploadTransform(
                        @RequestPart(API_PATH_TEMPLATE_ID_ROOT_VAR) @Parameter(description = "Template id root", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "templateIdRoot does not match the expected size") @NotBlank(message = "Template id root cannot be blank") String templateIdRoot,
                        @RequestPart(API_PATH_VERSION_VAR) @Parameter(description = "Version", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Version does not match the expected size") @NotBlank(message = "Version cannot be blank") @Pattern(message = "Version does not match the regex ^(\\d+\\.)(\\d+)$", regexp = "^(\\d+\\.)(\\d+)$") String version,
                        @RequestPart(API_PATH_FILE_VAR)MultipartFile file, @RequestPart(API_PATH_URI_VAR) String uri, @RequestPart(API_PATH_TYPE_VAR) FhirTypeEnum type) throws IOException, OperationException, 
        DocumentAlreadyPresentException, DocumentNotFoundException, InvalidContentException;

        @PutMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
        @Operation(summary = "Update transform to MongoDB", description = "Servizio che consente di aggiornare una trasformata nella base dati.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Aggiornamento trasformata avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PutDocsResDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Risorsa non trovata", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Conflitto riscontrato sulla risorsa", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        PutDocsResDTO updateTransform(
                        @RequestPart(API_PATH_TEMPLATE_ID_ROOT_VAR) @Parameter(description = "Template id root", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "templateIdRoot does not match the expected size") @NotBlank(message = "Template id root cannot be blank") String templateIdRoot,
                        @RequestPart(API_PATH_VERSION_VAR) @Parameter(description = "Version", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "version does not match the expected size") @NotBlank(message = "Version cannot be blank") @Pattern(message = "Version does not match the regex ^(\\d+\\.)(\\d+)$", regexp = "^(\\d+\\.)(\\d+)$") String version,
                        @RequestPart(API_PATH_FILE_VAR)MultipartFile file, @RequestPart(API_PATH_URI_VAR) String uri, @RequestPart(API_PATH_TYPE_VAR) FhirTypeEnum type) throws IOException, OperationException,
                        DocumentAlreadyPresentException, DocumentNotFoundException, InvalidVersionException,
                        InvalidContentException;

        @DeleteMapping(value = API_DELETE_BY_TEMPLATE_ID_ROOT, produces = {
                        MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Delete transform from MongoDB given its Template ID Root", description = "Servizio che consente di cancellare una trasformata dalla base dati tramite il Template ID Root.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cancellazione avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DelDocsResDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Trasformata non trovata sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        DelDocsResDTO deleteTransform(@PathVariable(API_PATH_TEMPLATE_ID_ROOT_VAR) @NotBlank(message = "templateIdRoot cannot be blank") @Size(max = DEFAULT_STRING_MAX_SIZE, message = "templateIdRoot does not match the expected size") String templateIdRoot) throws DocumentNotFoundException, OperationException;

        @GetMapping(value = API_GET_BY_TEMPLATE_ID_ROOT, produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns a transform from MongoDB, given its Template ID Root and its Version", description = "Servizio che consente di ritornare una trasformata dalla base dati tramite il suo Template ID Root e Version.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetDocsResDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Trasformata non trovata sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        GetDocsResDTO getTransformByTemplateIdRootAndVersion(
           @PathVariable(API_PATH_TEMPLATE_ID_ROOT_VAR) @NotBlank(message = "templateIdRoot cannot be blank") @Size(max = DEFAULT_STRING_MAX_SIZE, message = "templateIdRoot does not match the expected size") String templateIdRoot,
           @RequestParam(value = API_QP_BINARY, defaultValue = "false") @Parameter(description = "Include binary content") boolean binary,
           @RequestParam(value = API_QP_INCLUDE_DELETED, defaultValue = "false") @Parameter(description = "Include deleted content") boolean deleted
)
            throws DocumentNotFoundException, OperationException;

        @GetMapping(value = API_GET_ONE_BY_ID, produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns a transform from MongoDB, given its ID", description = "Servizio che consente di ritornare una trasformata dalla base dati tramite il suo ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetDocByIdResDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Trasformata non trovata sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        GetDocByIdResDTO getTransformById(
                        @PathVariable(API_PATH_ID_VAR) @NotBlank(message = "Id cannot be null") @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "id does not match the expected size") @ValidObjectId(message = "Document id not valid") String id)
                        throws OperationException, DocumentNotFoundException;

        @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns the list of all transforms from MongoDB", description = "Servizio che consente di ritornare la lista delle trasformate dalla base dati.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetDocsResDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        GetDocsResDTO getTransform(
            @RequestParam(value = API_QP_BINARY, defaultValue = "false") @Parameter(description = "Include binary content") boolean binary,
            @RequestParam(value = API_QP_INCLUDE_DELETED, defaultValue = "false") @Parameter(description = "Include deleted schema") boolean deleted
        ) throws OperationException;

}
