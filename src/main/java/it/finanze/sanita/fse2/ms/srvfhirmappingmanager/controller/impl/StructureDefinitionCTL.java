package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.IStructureDefinitionCTL;
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

@RestController
public class StructureDefinitionCTL extends AbstractCTL implements IStructureDefinitionCTL {

    /**
     * Document service layer
     */
    @Autowired
    private IDefinitionSRV definitionSRV;

    @Override
    public GetDefinitionResDTO getDefinitionByName(String name) throws DocumentNotFoundException, OperationException {
        DefinitionDTO out = definitionSRV.findDocByName(name);
        return new GetDefinitionResDTO(getLogTraceInfo(), out);
    }

    @Override
    public UploadDefinitionResDTO uploadDefinition(MultipartFile[] files) throws DocumentAlreadyPresentException, OperationException, DataProcessingException {
        definitionSRV.insertDocsByName(null, files);
        return new UploadDefinitionResDTO(getLogTraceInfo());
    }

    @Override
    public UpdateDefinitionResDTO updateDefinition(String name, MultipartFile file) throws DocumentNotFoundException, OperationException, DataProcessingException {
        String filename = definitionSRV.updateDocByName(name, file);
        return new UpdateDefinitionResDTO(getLogTraceInfo(), new UpdateDefinitionResDTO.Payload(filename));
    }

    @Override
    public DeleteDefinitionResDTO deleteDefinition(String name) throws DocumentNotFoundException, OperationException {
        String filename = definitionSRV.deleteDocByName(name);
        return new DeleteDefinitionResDTO(getLogTraceInfo(), new DeleteDefinitionResDTO.Payload(filename));
    }

    @Override
    public GetDefinitionResDTO getDefinitionById(String id) throws DocumentNotFoundException, OperationException {
        DefinitionDTO out = definitionSRV.findDocById(id);
        return new GetDefinitionResDTO(getLogTraceInfo(), out);
    }
}