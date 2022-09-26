package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.DefinitionCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.base.DefinitionDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import org.springframework.web.multipart.MultipartFile;

public interface IDefinitionSRV extends IChangeSetSRV<DefinitionCS> {
    DefinitionDTO findDocById(String id) throws OperationException, DocumentNotFoundException;
    DefinitionDTO findDocByName(String name) throws OperationException, DocumentNotFoundException;
    String insertDocByName(String name, String version, MultipartFile file) throws OperationException, DocumentAlreadyPresentException, DataProcessingException;
    String updateDocByName(String name, MultipartFile file) throws OperationException, DocumentNotFoundException, DataProcessingException;
    String deleteDocByName(String name) throws OperationException, DocumentNotFoundException;
}
