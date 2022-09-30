package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.ValuesetCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.base.ValuesetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IValuesetRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.ValuesetETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IValuesetSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ChangeSetUtility;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValuesetSRV implements IValuesetSRV {

    @Autowired
    private IValuesetRepo repository;

    @Override
    public ValuesetDTO findDocById(String id) throws OperationException, DocumentNotFoundException {
        ValuesetETY doc = repository.findDocById(id);
        if (doc == null) {
            throw new DocumentNotFoundException("The requested document does not exists");
        }
        return ValuesetDTO.fromEntity(doc);
    }

    @Override
    public ValuesetDTO findDocByName(String name) throws OperationException, DocumentNotFoundException {
        ValuesetETY doc = repository.findDocByName(name);
        if (doc == null) {
            throw new DocumentNotFoundException("The requested document does not exists");
        }
        return ValuesetDTO.fromEntity(doc);
    }

    @Override
    public String insertDocByName(String name, MultipartFile file) throws OperationException, DocumentAlreadyPresentException, DataProcessingException {
        // Check if given document already exists
        if(repository.isDocumentInserted(name)) {
            // Let the caller know about it
            throw new DocumentAlreadyPresentException("Cannot insert the given document, it already exists");
        }
        // Insert it
        ValuesetETY doc = repository.insertDocByName(ValuesetETY.fromMultipart(name, file));
        // Return the filename
        return doc.getFilenameValueset();
    }
    
    @Override
    public void insertDocsByName(MultipartFile[] files) throws OperationException, DocumentAlreadyPresentException, DataProcessingException {

    	List<String> filesToCheck = new ArrayList<>();
    	List<ValuesetETY> filesToAdd = new ArrayList<>();
    	for(MultipartFile file : files) {
    		String name = FilenameUtils.removeExtension(file.getOriginalFilename());
    		filesToCheck.add(name);
    		filesToAdd.add(ValuesetETY.fromMultipart(name, file));
    	}
        if(!repository.isDocumentsInserted(filesToCheck).isEmpty()) {
        	 String duplicatedFiles = filesToCheck.stream().collect(Collectors.joining(", "));
            throw new DocumentAlreadyPresentException("Cannot insert the given document, it already exists:" + duplicatedFiles);
        }
        // Insert it
        repository.insertAll(filesToAdd);
    }

    @Override
    public String updateDocByName(String name, MultipartFile file) throws OperationException, DocumentNotFoundException, DataProcessingException {
        ValuesetETY doc = repository.findDocByName(name);
        if(doc == null) {
            throw new DocumentNotFoundException("Unable to update the given document, it does not exists");
        }
        doc = repository.updateDocByName(doc, ValuesetETY.fromMultipart(name, file));
        return doc.getFilenameValueset();
    }

    @Override
    public String deleteDocByName(String name) throws OperationException, DocumentNotFoundException {
        if(!repository.isDocumentInserted(name)) {
            throw new DocumentNotFoundException("Unable to delete the given document, it does not exists");
        }
        ValuesetETY doc = repository.deleteDocByName(name);
        return doc.getFilenameValueset();
    }

    /**
     * Retrieves the latest insertions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing insertions
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<ChangeSetDTO<ValuesetCS>> getInsertions(Date lastUpdate) throws OperationException {
        List<ValuesetETY> insertions;
        if(lastUpdate != null) {
            insertions = repository.getInsertions(lastUpdate);
        } else{
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
    public List<ChangeSetDTO<ValuesetCS>> getDeletions(Date lastUpdate) throws OperationException {
        List<ChangeSetDTO<ValuesetCS>> changes = new ArrayList<>();
        if(lastUpdate != null) {
            List<ValuesetETY> deletions = repository.getDeletions(lastUpdate);
            changes = deletions.stream().map(ChangeSetUtility::toChangeset).collect(Collectors.toList());
        }
        return changes;
    }
}
