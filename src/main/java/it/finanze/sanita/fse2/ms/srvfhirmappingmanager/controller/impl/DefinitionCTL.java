package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.IDefinitionCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.DeleteDefinitionResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.GetDefinitionResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.UpdateDefinitionResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.UploadDefinitionResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.base.DefinitionDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IDefinitionSRV;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DefinitionCTL extends AbstractCTL implements IDefinitionCTL {

    /**
     * Document service layer
     */
    @Autowired
    private IDefinitionSRV definitionSRV;

    @Override
    public GetDefinitionResDTO getDefinitionByName(String name) throws DocumentNotFoundException, OperationException {
        // Retrieve document by name
        DefinitionDTO out = definitionSRV.findDocByName(name);
        // Return response
        return new GetDefinitionResDTO(getLogTraceInfo(), out);
    }

    @Override
    public UploadDefinitionResDTO uploadDefinition(String version, MultipartFile file) throws DocumentAlreadyPresentException, OperationException, DataProcessingException {
        // Insert document
        String filename = definitionSRV.insertDocByName(FilenameUtils.removeExtension(file.getOriginalFilename()) , version, file);
        // Return response
        return new UploadDefinitionResDTO(getLogTraceInfo(), new UploadDefinitionResDTO.Payload(filename));
    }

    @Override
    public UpdateDefinitionResDTO updateDefinition(String name, MultipartFile file) throws DocumentNotFoundException, OperationException, DataProcessingException {
        // Update document
        String filename = definitionSRV.updateDocByName(name, file);
        // Return response
        return new UpdateDefinitionResDTO(getLogTraceInfo(), new UpdateDefinitionResDTO.Payload(filename));
    }

    @Override
    public DeleteDefinitionResDTO deleteDefinition(String name) throws DocumentNotFoundException, OperationException {
        // Update document
        String filename = definitionSRV.deleteDocByName(name);
        // Return response
        return new DeleteDefinitionResDTO(getLogTraceInfo(), new DeleteDefinitionResDTO.Payload(filename));
    }

    @Override
    public GetDefinitionResDTO getDefinitionById(String id) throws DocumentNotFoundException, OperationException {
        // Get document
        DefinitionDTO out = definitionSRV.findDocById(id);
        // Return response
        return new GetDefinitionResDTO(getLogTraceInfo(), out);
    }
}