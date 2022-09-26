package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.DeleteDefinitionResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.GetDefinitionResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.UpdateDefinitionResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.UploadDefinitionResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.validators.ValidObjectId;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsRoutes.*;

/**
 * Definition controller
 *
 * @author G. Baittiner
 */
@RequestMapping(path = API_DEFINITION_MAPPER)
@Tag(name = API_DEFINITION_TAG)
@Validated
public interface IDefinitionCTL {

    @GetMapping(API_PATH_EXTS)
    GetDefinitionResDTO getDefinitionByName(
        @PathVariable(name = API_PATH_NAME_VAR)
        String name
    ) throws DocumentNotFoundException, OperationException;

    @PostMapping(
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    UploadDefinitionResDTO uploadDefinition(
        @RequestPart
        String name,
        @RequestPart
        String version,
        @RequestPart
        MultipartFile file
    ) throws DocumentAlreadyPresentException, OperationException, DataProcessingException;

    @PutMapping(
        value = API_PATH_EXTS,
        produces = {MediaType.APPLICATION_JSON_VALUE},
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    UpdateDefinitionResDTO updateDefinition(
        @PathVariable(name = API_PATH_NAME_VAR)
        String name,
        @RequestPart
        MultipartFile file
    ) throws DocumentNotFoundException, OperationException, DataProcessingException;

    @DeleteMapping(API_PATH_EXTS)
    DeleteDefinitionResDTO deleteDefinition(
        @PathVariable(name = API_PATH_NAME_VAR)
        String name
    ) throws DocumentNotFoundException, OperationException;

    @GetMapping(API_GET_ONE_BY_ID)
    GetDefinitionResDTO getDefinitionById(
        @PathVariable(name = API_PATH_ID_VAR)
        @ValidObjectId
        String id
    ) throws DocumentNotFoundException, OperationException;
}
