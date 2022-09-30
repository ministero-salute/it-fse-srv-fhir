package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.IValuesetCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.DeleteValuesetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.GetValuesetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.UpdateValuesetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.UploadValuesetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.base.ValuesetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IValuesetSRV;

@RestController
public class ValuesetCTL extends AbstractCTL implements IValuesetCTL {

    /**
     * Document service layer
     */
    @Autowired
    private IValuesetSRV service;


    @Override
    public GetValuesetResDTO getValuesetByName(String name) throws DocumentNotFoundException, OperationException {
        ValuesetDTO out = service.findDocByName(name);
        return new GetValuesetResDTO(getLogTraceInfo(), out);
    }

    @Override
    public UploadValuesetResDTO uploadValueset(MultipartFile[] files) throws DocumentAlreadyPresentException, OperationException, DataProcessingException {
        service.insertDocsByName(files);
        return new UploadValuesetResDTO(getLogTraceInfo());
    }

    @Override
    public UpdateValuesetResDTO updateValueset(String name, MultipartFile file) throws DocumentNotFoundException, OperationException, DataProcessingException {
        String filename = service.updateDocByName(name, file);
        return new UpdateValuesetResDTO(getLogTraceInfo(), new UpdateValuesetResDTO.Payload(filename));
    }

    @Override
    public DeleteValuesetResDTO deleteValueset(String name) throws DocumentNotFoundException, OperationException {
        String filename = service.deleteDocByName(name);
        return new DeleteValuesetResDTO(getLogTraceInfo(), new DeleteValuesetResDTO.Payload(filename));
    }

    @Override
    public GetValuesetResDTO getValuesetById(String id) throws DocumentNotFoundException, OperationException {
        ValuesetDTO out = service.findDocById(id);
        return new GetValuesetResDTO(getLogTraceInfo(), out);
    }
}
