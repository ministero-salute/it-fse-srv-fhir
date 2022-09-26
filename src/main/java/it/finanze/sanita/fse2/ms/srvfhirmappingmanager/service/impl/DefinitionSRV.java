package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefinitionSRV implements IDefinitionSRV {

    @Autowired
    private IDefinitionRepo repository;

    @Override
    public DefinitionDTO findDocById(String id) throws OperationException, DocumentNotFoundException {
        // Get document
        DefinitionETY doc = repository.findDocById(id);
        // Verify data
        if (doc == null) {
            throw new DocumentNotFoundException("The requested document does not exists");
        }
        // Bye bye
        return DefinitionDTO.fromEntity(doc);
    }

    @Override
    public DefinitionDTO findDocByName(String name) throws OperationException, DocumentNotFoundException {
        // Get document
        DefinitionETY doc = repository.findDocByName(name);
        // Verify data
        if (doc == null) {
            throw new DocumentNotFoundException("The requested document does not exists");
        }
        // Bye bye
        return DefinitionDTO.fromEntity(doc);
    }

    @Override
    public String insertDocByName(String name, String version, MultipartFile file) throws OperationException, DocumentAlreadyPresentException, DataProcessingException {
        // Check if given document already exists
        if(repository.isDocumentInserted(name)) {
            // Let the caller know about it
            throw new DocumentAlreadyPresentException("Cannot insert the given document, it already exists");
        }
        // Insert it
        DefinitionETY doc = repository.insertDocByName(DefinitionETY.fromMultipart(name, version, file));
        // Return the filename
        return doc.getFilenameDefinition();
    }

    @Override
    public String updateDocByName(String name, MultipartFile file) throws OperationException, DocumentNotFoundException, DataProcessingException {
        // Get document
        DefinitionETY doc = repository.findDocByName(name);
        // Check if given document already exists
        if(doc == null) {
            // Let the caller know about it
            throw new DocumentNotFoundException("Unable to update the given document, it does not exists");
        }
        // Update it
        doc = repository.updateDocByName(doc, DefinitionETY.fromMultipart(name, doc.getVersionDefinition(), file));
        // Return filename
        return doc.getFilenameDefinition();
    }

    @Override
    public String deleteDocByName(String name) throws OperationException, DocumentNotFoundException {
        // Check if given document already exists
        if(!repository.isDocumentInserted(name)) {
            // Let the caller know about it
            throw new DocumentNotFoundException("Unable to delete the given document, it does not exists");
        }
        // Update it
        DefinitionETY doc = repository.deleteDocByName(name);
        // Return filename
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
        // Retrieve insertions
        List<DefinitionETY> insertions;
        // Verify no null value has been provided
        if(lastUpdate != null) {
            insertions = repository.getInsertions(lastUpdate);
        } else {
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
    public List<ChangeSetDTO<DefinitionCS>> getDeletions(Date lastUpdate) throws OperationException {
        // Create empty container
        List<ChangeSetDTO<DefinitionCS>> changes = new ArrayList<>();
        // Verify no null value has been provided
        if(lastUpdate != null) {
            // Retrieve deletions
            List<DefinitionETY> deletions = repository.getDeletions(lastUpdate);
            // Iterate and populate
            changes = deletions.stream().map(ChangeSetUtility::toChangeset).collect(Collectors.toList());
        }
        return changes;
    }
}
