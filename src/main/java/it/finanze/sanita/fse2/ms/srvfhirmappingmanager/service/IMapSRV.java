package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IMapSRV {
    /**
     * Create maps by files
     * @param rootMapFileName
     * @param files
     * @return
     * @throws DataProcessingException
     * @throws DocumentNotFoundException
     */
    Map<String, StructureMap> createMaps(String rootMapFileName, MultipartFile[] files) throws DataProcessingException, DocumentNotFoundException;

    /**
     * Update existing maps by files, appending the ones not passed to new object
     * @param structureMaps
     * @param files
     * @return
     * @throws DataProcessingException
     */
    Map<String, StructureMap> updateMaps(List<StructureMap> structureMaps, MultipartFile[] files) throws DataProcessingException;
}