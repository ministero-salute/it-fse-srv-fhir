package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IDefinitionSRV {

    /**
     * Create definitions by files
     * @param version
     * @param files
     * @return
     * @throws DataProcessingException
     */
    Map<String, StructureDefinition> createDefinitions(String version, MultipartFile[] files) throws DataProcessingException;

    /**
     * Update definitions by files and appending the existing ones to new object
     * @param version
     * @param structureDefinitions
     * @param files
     * @return
     * @throws DataProcessingException
     */
    Map<String, StructureDefinition> updateValuesets(String version, List<StructureDefinition> structureDefinitions, MultipartFile[] files) throws DataProcessingException;
}
