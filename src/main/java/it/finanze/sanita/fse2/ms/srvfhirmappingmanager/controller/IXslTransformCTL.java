package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import java.io.IOException;
import java.io.Serializable;

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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.GetDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.GetXsltResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.XslTransformResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.XslTransformUploadResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.InvalidVersionException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators.ValidObjectId;

/**
 * XSLT Controller.
 * 
 * @author Riccardo Bonesi
 */
@RequestMapping(path = "/v1")
@Tag(name = "XSLT Controller")
@Validated
public interface IXslTransformCTL extends Serializable {

        @PostMapping(value = "/xslt", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
                        MediaType.MULTIPART_FORM_DATA_VALUE })
        @Operation(summary = "Add xsl transform to MongoDB", description = "Servizio che consente di aggiungere un xsl alla base dati.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = XslTransformUploadResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Creazione XSL avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = XslTransformUploadResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Conflitto riscontrato sulla risorsa", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<XslTransformUploadResponseDTO> addXslTransform(
                        @RequestPart("templateIdRoot") @Parameter(description = "Template Id Root of the xslt", schema = @Schema(minLength = 1, maxLength = 100)) @Size(min = 1, max = 100) @NotBlank(message = "Template Id cannot be blank") String templateIdRoot,
                        @RequestPart("version") @Parameter(description = "Xslt version", schema = @Schema(minLength = 1, maxLength = 100)) @Size(min = 1, max = 100) @NotBlank(message = "Template Id cannot be blank") @Pattern(message = "Version does not match the regex ^(\\d+\\.)(\\d+)$", regexp = "^(\\d+\\.)(\\d+)$") String version,
                        @RequestPart("file") MultipartFile file, HttpServletRequest request) throws IOException,
                        OperationException, DocumentAlreadyPresentException, InvalidContentException;

        @PutMapping(value = "/xslt", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
                        MediaType.MULTIPART_FORM_DATA_VALUE })
        @Operation(summary = "Update XSLT on MongoDB", description = "Servizio che consente di aggiornare uno XSLT sulla base dati.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = XslTransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Aggiornamento XSLT avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = XslTransformResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "XSLT non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "409", description = "Conflitto riscontrato sulla risorsa", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<XslTransformUploadResponseDTO> updateXslTransform(
                        @RequestPart("templateIdRoot") @Parameter(description = "Template Id Root of the xslt", schema = @Schema(minLength = 1, maxLength = 100)) @Size(min = 1, max = 100) @NotBlank(message = "Template Id cannot be blank") String templateIdRoot,
                        @RequestPart("version") @Parameter(description = "Xslt version", schema = @Schema(minLength = 1, maxLength = 100)) @Size(min = 1, max = 100) @NotBlank(message = "Version cannot be blank") @Pattern(message = "Version does not match the regex ^(\\d+\\.)(\\d+)$", regexp = "^(\\d+\\.)(\\d+)$") String version,
                        @RequestPart("file") MultipartFile file, HttpServletRequest request)
                        throws IOException, OperationException, InvalidContentException, DocumentNotFoundException,
                        InvalidVersionException;

        @DeleteMapping(value = "/xslt/root/{templateIdRoot}/version/{version}", produces = {
                        MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Delete XSLT from MongoDB given its Template ID Root and Version", description = "Servizio che consente di cancellare uno XSLT dalla base dati dato il Template ID Root e la Version.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = XslTransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Cancellazione XSLT avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = XslTransformResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "XSLT non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<XslTransformUploadResponseDTO> deleteXslTransform(HttpServletRequest request,
                        @PathVariable @NotBlank(message = "templateIdRoot cannot be blank") @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "templateIdRoot does not match the expected size") String templateIdRoot,
                        @PathVariable @NotBlank(message = "version cannot be blank") @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "version does not match the expected size") @Pattern(message = "Version does not match the regex ^(\\d+\\.)(\\d+)$", regexp = "^(\\d+\\.)(\\d+)$") String version)
        throws DocumentNotFoundException, OperationException;

        @GetMapping(value = "/xslt/root/{templateIdRoot}/version/{version}", produces = {
                        MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns a XSLT from MongoDB, given its Template ID Root and its Version", description = "Servizio che consente di ritornare uno XSLT dalla base dati dati il suo Template ID Root e Version.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = XslTransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta XSLT avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = XslTransformDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "XSLT non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<XslTransformDTO> getXslTransformByTemplateIdRootAndVersion(HttpServletRequest request,
                        @PathVariable @NotBlank(message = "templateIdRoot cannot be blank") @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "templateIdRoot does not match the expected size") String templateIdRoot,
                        @PathVariable @NotBlank(message = "version cannot be blank") @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE, message = "version does not match the expected size") @Pattern(message = "Version does not match the regex ^(\\d+\\.)(\\d+)$", regexp = "^(\\d+\\.)(\\d+)$") String version)
                        throws DocumentNotFoundException, OperationException;

        @GetMapping(value = "/xslt/id/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns a XSLT from MongoDB, given its ID", description = "Servizio che consente di ritornare uno XSLT dalla base dati dati il suo ID.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = XslTransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta XSLT avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = XslTransformDTO.class))),
                        @ApiResponse(responseCode = "400", description = "I parametri forniti non sono validi", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "404", description = "XSLT non trovato sul database", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<GetDocumentResDTO> getXslTransformById(HttpServletRequest request,
                        @NotBlank(message = "Id cannot be blank") @PathVariable @Size(min = 1, max = DEFAULT_STRING_MAX_SIZE, message = "id does not match the expected size") @ValidObjectId(message = "Document id not valid") String id)
                        throws OperationException, DocumentNotFoundException;

        @GetMapping(value = "/xslt", produces = { MediaType.APPLICATION_JSON_VALUE })
        @Operation(summary = "Returns the list of all XSLTs from MongoDB", description = "Servizio che consente di ritornare la lista degli XSLT dalla base dati.")
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = XslTransformResponseDTO.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Richiesta XSLT avvenuta con successo", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetXsltResponseDTO.class))),
                        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))) })
        ResponseEntity<GetXsltResponseDTO> getXslTransform(HttpServletRequest request) throws OperationException;

}
