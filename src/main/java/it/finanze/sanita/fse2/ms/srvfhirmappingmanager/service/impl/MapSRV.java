package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.MapCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.base.MapDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IMapRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IMapSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ChangeSetUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapSRV implements IMapSRV {

    @Autowired
    private IMapRepo repository;

    @Override
    public MapDTO findDocById(String id) throws OperationException, DocumentNotFoundException {
        // Get document
        MapETY doc = repository.findDocById(id);
        // Verify data
        if (doc == null) {
            throw new DocumentNotFoundException("The requested document does not exists");
        }
        // Bye bye
        return MapDTO.fromEntity(doc);
    }

    @Override
    public MapDTO findDocByName(String name) throws OperationException, DocumentNotFoundException {
        // Get document
        MapETY doc = repository.findDocByName(name);
        // Verify data
        if (doc == null) {
            throw new DocumentNotFoundException("The requested document does not exists");
        }
        // Bye bye
        return MapDTO.fromEntity(doc);
    }

    @Override
    public String insertDocByName(String name, String root, String extension, MultipartFile file) throws OperationException, DocumentAlreadyPresentException, DataProcessingException {
        // Check if given document already exists
        if(repository.isDocumentInserted(name)) {
            // Let the caller know about it
            throw new DocumentAlreadyPresentException("Cannot insert the given document, it already exists");
        }
        // Insert it
        MapETY doc = repository.insertDocByName(MapETY.fromMultipart(name, root, extension, file));
        // Return the filename
        return doc.getFilenameMap();
    }

    @Override
    public String updateDocByName(String name, MultipartFile file) throws OperationException, DocumentNotFoundException, DataProcessingException {
        // Get document
        MapETY doc = repository.findDocByName(name);
        // Check if given document already exists
        if(doc == null) {
            // Let the caller know about it
            throw new DocumentNotFoundException("Unable to update the given document, it does not exists");
        }
        // Update it
        doc = repository.updateDocByName(doc, MapETY.fromMultipart(name, doc.getTemplateIdRoot(), doc.getTemplateIdExtension(), file));
        // Return filename
        return doc.getFilenameMap();
    }

    @Override
    public String deleteDocByName(String name) throws OperationException, DocumentNotFoundException {
        // Check if given document already exists
        if(!repository.isDocumentInserted(name)) {
            // Let the caller know about it
            throw new DocumentNotFoundException("Unable to delete the given document, it does not exists");
        }
        // Update it
        MapETY doc = repository.deleteDocByName(name);
        // Return filename
        return doc.getFilenameMap();
    }

    /**
     * Retrieves the latest insertions according to the given timeframe
     *
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing insertions
     * @throws OperationException If a data-layer error occurs
     */
    @Override
    public List<ChangeSetDTO<MapCS>> getInsertions(Date lastUpdate) throws OperationException {
        // Retrieve insertions
        List<MapETY> insertions;
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
    public List<ChangeSetDTO<MapCS>> getDeletions(Date lastUpdate) throws OperationException {
        // Create empty container
        List<ChangeSetDTO<MapCS>> changes = new ArrayList<>();
        // Verify no null value has been provided
        if(lastUpdate != null) {
            // Retrieve deletions
            List<MapETY> deletions = repository.getDeletions(lastUpdate);
            // Iterate and populate
            changes = deletions.stream().map(ChangeSetUtility::toChangeset).collect(Collectors.toList());
        }
        return changes;
    }
}
