package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_EXTS_STRING_MAX;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_EXTS_STRING_MIN;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformUploadResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.InvalidVersionException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators.ValidObjectId;

/**
 * Transform Controller
 */
@RequestMapping(path = "/v1/transform")
@Tag(name = "Transform Controller")
@Validated
public interface ITransformCTL extends Serializable {

        @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
                        MediaType.MULTIPART_FORM_DATA_VALUE })
        @Operation(summary = "Add transform to MongoDB", description = "Servizio che consente di aggiungere una trasformata alla base dati.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TransformUploadResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Creazione trasformata avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransformResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Conflitto riscontrato sulla risorsa", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<TransformUploadResponseDTO> uploadTransform(HttpServletRequest request,
                        @RequestPart("rootMapIdentifier") @Parameter(description = "Root map identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @Size(min = 0, max = OA_EXTS_STRING_MAX, message = "rootMapIdentifier does not match the expected size") @NotBlank(message = "Root map identifier cannot be blank") String rootMapIdentifier,
                        @RequestPart("templateIdRoot") @Parameter(description = "Template id root", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "templateIdRoot does not match the expected size") @NotBlank(message = "Template id root cannot be blank") String templateIdRoot,
                        @RequestPart("version") @Parameter(description = "Version", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Version does not match the expected size") @NotBlank(message = "Version cannot be blank") @Pattern(message = "Version does not match the regex ^(\\d+\\.)(\\d+)$", regexp = "^(\\d+\\.)(\\d+)$") String version,
                        @Parameter(description = "Structure definitions files", array = @ArraySchema(minItems = 1, maxItems = 1000, schema = @Schema(type = "string", format = "binary", maxLength = 1000))) @Size(min = 1, max = 1000, message = "File array does not match the expected size") @RequestPart("structureDefinitions") MultipartFile[] structureDefinitions,
                        @Parameter(description = "Map files", array = @ArraySchema(minItems = 1, maxItems = 1000, schema = @Schema(type = "string", format = "binary", maxLength = 1000))) @Size(min = 1, max = 1000, message = "File array does not match the expected size") @RequestPart("maps") MultipartFile[] maps,
                        @Parameter(description = "Valueset files", array = @ArraySchema(minItems = 1, maxItems = 1000, schema = @Schema(type = "string", format = "binary", maxLength = 1000))) @Size(min = 1, max = 1000, message = "File array does not match the expected size") @RequestPart("valueSets") MultipartFile[] valueSets) throws IOException, OperationException,
                        DocumentAlreadyPresentException, DocumentNotFoundException, InvalidContentException;

        @PutMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
        @Operation(summary = "Update transform to MongoDB", description = "Servizio che consente di aggiornare una trasformata nella base dati.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Aggiornamento trasformata avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransformResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Risorsa non trovata", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Conflitto riscontrato sulla risorsa", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<TransformUploadResponseDTO> updateTransform(HttpServletRequest request,
                        @RequestPart("rootMapIdentifier") @Parameter(description = "Root map identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @Size(min = 0, max = OA_EXTS_STRING_MAX, message = "rootMapIdentifier does not match the expected size") String rootMapIdentifier,
                        @RequestPart("templateIdRoot") @Parameter(description = "Template id root", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "templateIdRoot does not match the expected size") @NotBlank(message = "Template id root cannot be blank") String templateIdRoot,
                        @RequestPart("version") @Parameter(description = "Version", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "version does not match the expected size") @NotBlank(message = "Version cannot be blank") @Pattern(message = "Version does not match the regex ^(\\d+\\.)(\\d+)$", regexp = "^(\\d+\\.)(\\d+)$") String version,
                        @Parameter(description = "Structure definitions files", array = @ArraySchema(minItems = 1, maxItems = 1000, schema = @Schema(type = "string", format = "binary", maxLength = 1000))) @Size(min = 1, max = 1000, message = "File array does not match the expected size") @RequestPart("structureDefinitions") MultipartFile[] structureDefinitions,
                        @Parameter(description = "Maps files", array = @ArraySchema(minItems = 1, maxItems = 1000, schema = @Schema(type = "string", format = "binary", maxLength = 1000))) @Size(min = 1, max = 1000, message = "File array does not match the expected size") @RequestPart("maps") MultipartFile[] maps,
                        @Parameter(description = "Valueset files", array = @ArraySchema(minItems = 1, maxItems = 1000, schema = @Schema(type = "string", format = "binary", maxLength = 1000))) @Size(min = 1, max = 1000, message = "File array does not match the expected size") @RequestPart("valueSets") MultipartFile[] valueSets) throws IOException, OperationException,
                        DocumentAlreadyPresentException, DocumentNotFoundException, InvalidVersionException,
                        InvalidContentException;

        @DeleteMapping(value = "/root/{templateIdRoot}/version/{version}", produces = {
                        MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Delete transform from MongoDB given its Template ID Root and Version", description = "Servizio che consente di cancellare una trasformata dalla base dati tramite il Template ID Root e la Version.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cancellazione avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransformResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Trasformata non trovata sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<TransformUploadResponseDTO> deleteTransform(HttpServletRequest request,
                        @PathVariable @NotBlank(message = "templateIdRoot cannot be blank") @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "templateIdRoot does not match the expected size") String templateIdRoot,
                        @PathVariable @NotBlank(message = "version cannot be blank") @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "version does not match the expected size") String version)
                        throws DocumentNotFoundException, OperationException;

        @GetMapping(value = "/root/{templateIdRoot}/version/{version}", produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns a transform from MongoDB, given its Template ID Root and its Version", description = "Servizio che consente di ritornare una trasformata dalla base dati tramite il suo Template ID Root e Version.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransformDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Trasformata non trovata sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<TransformDTO> getTransformByTemplateIdRootAndVersion(HttpServletRequest request,
                        @PathVariable @NotBlank(message = "templateIdRoot cannot be blank") @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "templateIdRoot does not match the expected size") String templateIdRoot,
                        @PathVariable @NotBlank(message = "version cannot be blank") @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "version does not match the expected size") String version)
                        throws DocumentNotFoundException, OperationException;

        @GetMapping(value = "/id/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns a transform from MongoDB, given its ID", description = "Servizio che consente di ritornare una trasformata dalla base dati tramite il suo ID.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransformDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Trasformata non trovata sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<TransformDTO> getTransformById(HttpServletRequest request,
                        @PathVariable @NotBlank(message = "Id cannot be null") @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "id does not match the expected size") @ValidObjectId(message = "Document id not valid") String id)
                        throws OperationException, DocumentNotFoundException;

        @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns the list of all transforms from MongoDB", description = "Servizio che consente di ritornare la lista delle trasformate dalla base dati.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransformDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<List<TransformDTO>> getTransform(HttpServletRequest request) throws OperationException;

        @GetMapping(value = "/active", produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns the list of all active transforms from MongoDB", description = "Servizio che consente di ritornare la lista delle trasformate attive dalla base dati.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = TransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TransformDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<List<TransformDTO>> getActiveTransform(HttpServletRequest request) throws OperationException;
}
