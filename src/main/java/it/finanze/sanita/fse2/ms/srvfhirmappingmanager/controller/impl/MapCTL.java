package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.IMapCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.DeleteMapResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.GetMapResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.UpdateMapResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.UploadMapResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.base.MapDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IMapSRV;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MapCTL extends AbstractCTL implements IMapCTL {

    /**
     * Document service layer
     */
    @Autowired
    private IMapSRV service;


    @Override
    public GetMapResDTO getMapByName(String name) throws DocumentNotFoundException, OperationException {
        // Retrieve document by name
        MapDTO out = service.findDocByName(name);
        // Return response
        return new GetMapResDTO(getLogTraceInfo(), out);
    }

    @Override
    public UploadMapResDTO uploadMap(String root, String extension, MultipartFile file) throws DocumentAlreadyPresentException, OperationException, DataProcessingException {
        // Insert document
        String filename = service.insertDocByName(FilenameUtils.removeExtension(file.getOriginalFilename()) , root, extension, file);
        // Return response
        return new UploadMapResDTO(getLogTraceInfo(), new UploadMapResDTO.Payload(filename));
    }

    @Override
    public UpdateMapResDTO updateMap(String name, MultipartFile file) throws DocumentNotFoundException, OperationException, DataProcessingException {
        // Update document
        String filename = service.updateDocByName(name, file);
        // Return response
        return new UpdateMapResDTO(getLogTraceInfo(), new UpdateMapResDTO.Payload(filename));
    }

    @Override
    public DeleteMapResDTO deleteMap(String name) throws DocumentNotFoundException, OperationException {
        // Update document
        String filename = service.deleteDocByName(name);
        // Return response
        return new DeleteMapResDTO(getLogTraceInfo(), new DeleteMapResDTO.Payload(filename));
    }

    @Override
    public GetMapResDTO getMapById(String id) throws DocumentNotFoundException, OperationException {
        // Get document
        MapDTO out = service.findDocById(id);
        // Return response
        return new GetMapResDTO(getLogTraceInfo(), out);
    }
}
