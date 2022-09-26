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
        // Get document
        ValuesetETY doc = repository.findDocById(id);
        // Verify data
        if (doc == null) {
            throw new DocumentNotFoundException("The requested document does not exists");
        }
        // Bye bye
        return ValuesetDTO.fromEntity(doc);
    }

    @Override
    public ValuesetDTO findDocByName(String name) throws OperationException, DocumentNotFoundException {
        // Get document
        ValuesetETY doc = repository.findDocByName(name);
        // Verify data
        if (doc == null) {
            throw new DocumentNotFoundException("The requested document does not exists");
        }
        // Bye bye
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
    public String updateDocByName(String name, MultipartFile file) throws OperationException, DocumentNotFoundException, DataProcessingException {
        // Get document
        ValuesetETY doc = repository.findDocByName(name);
        // Check if given document already exists
        if(doc == null) {
            // Let the caller know about it
            throw new DocumentNotFoundException("Unable to update the given document, it does not exists");
        }
        // Update it
        doc = repository.updateDocByName(doc, ValuesetETY.fromMultipart(name, file));
        // Return filename
        return doc.getFilenameValueset();
    }

    @Override
    public String deleteDocByName(String name) throws OperationException, DocumentNotFoundException {
        // Check if given document already exists
        if(!repository.isDocumentInserted(name)) {
            // Let the caller know about it
            throw new DocumentNotFoundException("Unable to delete the given document, it does not exists");
        }
        // Update it
        ValuesetETY doc = repository.deleteDocByName(name);
        // Return filename
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
        // Retrieve insertions
        List<ValuesetETY> insertions;
        // Verify no null value has been provided
        if(lastUpdate != null) {
            insertions = repository.getInsertions(lastUpdate);
        }else{
            insertions = repository.getEveryActiveDocument();
        }
        // Iterate and populate
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
        // Create empty container
        List<ChangeSetDTO<ValuesetCS>> changes = new ArrayList<>();
        // Verify no null value has been provided
        if(lastUpdate != null) {
            // Retrieve deletions
            List<ValuesetETY> deletions = repository.getDeletions(lastUpdate);
            // Iterate and populate
            changes = deletions.stream().map(ChangeSetUtility::toChangeset).collect(Collectors.toList());
        }
        return changes;
    }
}
