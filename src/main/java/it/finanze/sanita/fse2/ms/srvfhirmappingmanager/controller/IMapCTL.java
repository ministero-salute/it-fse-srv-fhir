package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_EXTS_STRING_MAX;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsOA.OA_EXTS_STRING_MIN;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_GET_ONE_BY_ID;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_MAP_MAPPER;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_MAP_TAG;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_PATH_EXTS;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_PATH_ID_VAR;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.API_PATH_NAME_VAR;

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
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.DeleteMapResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.GetMapResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.UpdateMapResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.UploadMapResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators.ValidObjectId;

/**
 * Map controller
 *
 * @author G. Baittiner
 */
@RequestMapping(path = API_MAP_MAPPER)
@Tag(name = API_MAP_TAG)
@Validated
public interface IMapCTL {

	@Operation(summary = "Get map by Name", description = "Insert name to map document")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Documents uploaded", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@GetMapping(API_PATH_EXTS)
	GetMapResDTO getMapByName(
			@PathVariable(name = API_PATH_NAME_VAR)
			@Parameter(description = "Extension identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @NotBlank(message = "Name cannot be blank") @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Extension does not match the expected size") String name)
			throws DocumentNotFoundException, OperationException;

	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	@Operation(summary = "Upload Map", description = "Insert file and fields properly to upload file")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Documents uploaded", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "409", description = "Duplicated extension identifier", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	UploadMapResDTO uploadMap(
			@Parameter(description = "Root identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @NotBlank(message = "Root cannot be blank") @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "root does not match the expected size") String root,
			@Parameter(description = "Extension identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @NotBlank(message = "Extension cannot be blank") @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Extension does not match the expected size") String extension,
			@RequestPart("file") MultipartFile file)
			throws DocumentAlreadyPresentException, OperationException, DataProcessingException;


	@Operation(summary = "Update documents by name", description = "Insert name and file to update")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Documents uploaded", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "409", description = "Duplicated extension identifier", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable entity", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@PutMapping(value = API_PATH_EXTS, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	UpdateMapResDTO updateMap(
			@PathVariable(name = API_PATH_NAME_VAR) 
			@Parameter(description = "Extension identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @NotBlank(message = "Extension cannot be blank") @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Extension does not match the expected size") String name,
			@RequestPart("file") MultipartFile file)
			throws DocumentNotFoundException, OperationException, DataProcessingException;



			////


	@DeleteMapping(API_PATH_EXTS)
	@Operation(summary = "Delete map", description = "Insert name do delete")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Documents uploaded", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	DeleteMapResDTO deleteMap(
			@PathVariable(name = API_PATH_NAME_VAR) 
			@Parameter(description = "Extension identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @NotBlank(message = "Extension cannot be blank") @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Extension does not match the expected size") String name)
			throws DocumentNotFoundException, OperationException;


			////



	@Operation(summary = "Get map by id", description = "Insert id to retrieve map")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Documents uploaded", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ChangeSetResDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDTO.class)))
	})
	@GetMapping(API_GET_ONE_BY_ID)
	GetMapResDTO getMapById(
			@PathVariable(name = API_PATH_ID_VAR)
			@Parameter(description = "Extension identifier", schema = @Schema(minLength = OA_EXTS_STRING_MIN, maxLength = OA_EXTS_STRING_MAX)) @NotBlank(message = "Extension cannot be blank") @Size(min = OA_EXTS_STRING_MIN, max = OA_EXTS_STRING_MAX, message = "Extension does not match the expected size")
			@ValidObjectId String id)
			throws DocumentNotFoundException, OperationException;
}
