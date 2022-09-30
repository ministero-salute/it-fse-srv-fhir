package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.DefinitionCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.base.DefinitionDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IDefinitionRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.DefinitionETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IDefinitionSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ChangeSetUtility;

@Service
public class DefinitionSRV implements IDefinitionSRV {

    @Autowired
    private IDefinitionRepo repository;

    @Override
    public DefinitionDTO findDocById(String id) throws OperationException, DocumentNotFoundException {
        DefinitionETY doc = repository.findDocById(id);
        if (doc == null) {
            throw new DocumentNotFoundException("The requested document does not exists");
        }
        return DefinitionDTO.fromEntity(doc);
    }

    @Override
    public DefinitionDTO findDocByName(String name) throws OperationException, DocumentNotFoundException {
        DefinitionETY doc = repository.findDocByName(name);
        if (doc == null) {
            throw new DocumentNotFoundException("The requested document does not exists");
        }
        return DefinitionDTO.fromEntity(doc);
    }

    @Override
    public String insertDocByName(String name, String version, MultipartFile file) throws OperationException, DocumentAlreadyPresentException, DataProcessingException {
        if(repository.isDocumentInserted(name)) {
            throw new DocumentAlreadyPresentException("Cannot insert the given document, it already exists");
        }
        DefinitionETY doc = repository.insertDocByName(DefinitionETY.fromMultipart(name, version, file));
        return doc.getFilenameDefinition();
    }

    @Override
    public String updateDocByName(String name, MultipartFile file) throws OperationException, DocumentNotFoundException, DataProcessingException {
        DefinitionETY doc = repository.findDocByName(name);
        if(doc == null) {
            throw new DocumentNotFoundException("Unable to update the given document, it does not exists");
        }
        doc = repository.updateDocByName(doc, DefinitionETY.fromMultipart(name, doc.getVersionDefinition(), file));
        return doc.getFilenameDefinition();
    }

    @Override
    public String deleteDocByName(String name) throws OperationException, DocumentNotFoundException {
        if(!repository.isDocumentInserted(name)) {
            throw new DocumentNotFoundException("Unable to delete the given document, it does not exists");
        }
        DefinitionETY doc = repository.deleteDocByName(name);
        return doc.getFilenameDefinition();
    }

    /**
     * Retrieves the latest insertions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing insertions
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<ChangeSetDTO<DefinitionCS>> getInsertions(Date lastUpdate) throws OperationException {
        List<DefinitionETY> insertions;
        if(lastUpdate != null) {
            insertions = repository.getInsertions(lastUpdate);
        } else {
            insertions = repository.getEveryActiveDocument();
        }
        return insertions.stream().map(ChangeSetUtility::toChangeset).collect(Collectors.toList());
    }

    /**
     * Retrieves the latest deletions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing deletions
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<ChangeSetDTO<DefinitionCS>> getDeletions(Date lastUpdate) throws OperationException {
        List<ChangeSetDTO<DefinitionCS>> changes = new ArrayList<>();
        if(lastUpdate != null) {
            List<DefinitionETY> deletions = repository.getDeletions(lastUpdate);
            changes = deletions.stream().map(ChangeSetUtility::toChangeset).collect(Collectors.toList());
        }
        return changes;
    }
    
    @Override
    public void insertDocsByName(String version, MultipartFile[] files) throws OperationException, DocumentAlreadyPresentException, DataProcessingException {
    	List<String> filesToCheck = new ArrayList<>();
    	List<DefinitionETY> filesToAdd = new ArrayList<>();
    	for(MultipartFile file : files) {
    		String name = FilenameUtils.removeExtension(file.getOriginalFilename());
    		filesToCheck.add(name);
    		filesToAdd.add(DefinitionETY.fromMultipart(name, version, file));
    	}
    	
        if(!repository.isDocumentsInserted(filesToCheck).isEmpty()) {
        	 String duplicatedFiles = filesToCheck.stream().collect(Collectors.joining(", "));
            throw new DocumentAlreadyPresentException("Cannot insert the given document, it already exists:" + duplicatedFiles);
        }
        repository.insertAll(filesToAdd);
    }
}
