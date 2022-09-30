package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.ValuesetCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.base.ValuesetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import org.springframework.web.multipart.MultipartFile;

public interface IValuesetSRV extends IChangeSetSRV<ValuesetCS> {
	
    ValuesetDTO findDocById(String id) throws OperationException, DocumentNotFoundException;
    
    ValuesetDTO findDocByName(String name) throws OperationException, DocumentNotFoundException;
    
    String insertDocByName(String name, MultipartFile file) throws OperationException, DocumentAlreadyPresentException, DataProcessingException;
    
    String updateDocByName(String name, MultipartFile file) throws OperationException, DocumentNotFoundException, DataProcessingException;
    
    String deleteDocByName(String name) throws OperationException, DocumentNotFoundException;
    
    void insertDocsByName(MultipartFile[] files) throws OperationException, DocumentAlreadyPresentException, DataProcessingException;
    
}
