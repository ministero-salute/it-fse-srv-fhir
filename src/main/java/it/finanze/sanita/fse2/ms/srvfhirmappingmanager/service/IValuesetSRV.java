package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureValueset;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IValuesetSRV {
    /**
     * Create valuesets by files
     * @param files
     * @return
     * @throws DataProcessingException
     */
    Map<String, StructureValueset> createValuesets(MultipartFile[] files) throws DataProcessingException;

    /**
     * Update existing valuesets by files and appending the ones not passed in the new object
     * @param structureValuesets
     * @param files
     * @return
     * @throws DataProcessingException
     */
    Map<String, StructureValueset> updateValuesets(List<StructureValueset> structureValuesets, MultipartFile[] files) throws DataProcessingException;
}
