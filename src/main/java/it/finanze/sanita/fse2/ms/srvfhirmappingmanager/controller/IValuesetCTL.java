package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_EXTS_STRING_MAX;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_EXTS_STRING_MIN;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_GET_ONE_BY_ID;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_PATH_EXTS;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_PATH_ID_VAR;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_PATH_NAME_VAR;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_VALUESET_MAPPER;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_VALUESET_TAG;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.http.MediaType;
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
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.DeleteValuesetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.GetValuesetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.UpdateValuesetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.UploadValuesetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators.ValidObjectId;

/**
 * Valueset controller
 *
 * @author G. Baittiner
 */
@RequestMapping(path = API_VALUESET_MAPPER)
@Tag(name = API_VALUESET_TAG)
@Validated
public interface IValuesetCTL {

	@GetMapping(API_PATH_EXTS)
	@Operation(summary = "Get Value Set by Name", description = "A description about Value Set Controller")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	GetValuesetResDTO getValuesetByName(
			@PathVariable(name = API_PATH_NAME_VAR) @Parameter(description = "Extension identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @NotBlank(message = "Extension cannot be blank") @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Name does not match the expected size") String name)
			throws DocumentNotFoundException, OperationException;


	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@Operation(summary = "Upload Value Set ", description = "A description about Value Set Uploading")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "409", description = "Duplicated extension identifier", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	UploadValuesetResDTO uploadValueset(@RequestPart("file") MultipartFile file)throws DocumentAlreadyPresentException, OperationException, DataProcessingException;

			
	@PutMapping(value = API_PATH_EXTS, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	@Operation(summary = "Update Value Set ", description = "A description about Value Set Updating")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "409", description = "Duplicated extension identifier", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})




	UpdateValuesetResDTO updateValueset(
			@PathVariable(name = API_PATH_NAME_VAR) @Parameter(description = "Extension identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @NotBlank(message = "Extension cannot be blank") @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Name does not match the expected size") String name,
			@RequestPart("file") MultipartFile file)
			throws DocumentNotFoundException, OperationException, DataProcessingException;

			@DeleteMapping(API_PATH_EXTS)
	@Operation(summary = "Delete Value Set ", description = "A description about Value Set Deleteing")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "409", description = "Duplicated extension identifier", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	
	DeleteValuesetResDTO deleteValueset(
			@PathVariable(name = API_PATH_NAME_VAR) @Parameter(description = "Extension identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @NotBlank(message = "Extension cannot be blank") @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Extension does not match the expected size") String name)
			throws DocumentNotFoundException, OperationException;

			@GetMapping(API_GET_ONE_BY_ID)
	@Operation(summary = "Get Value Set by Id", description = "A description about Value Getting by Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "409", description = "Duplicated extension identifier", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	
	GetValuesetResDTO getValuesetById(
			@PathVariable(name = API_PATH_ID_VAR) @ValidObjectId @Parameter(description = "Extension identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @NotBlank(message = "Extension cannot be blank") @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Extension does not match the expected size") String id)
			throws DocumentNotFoundException, OperationException;
}
