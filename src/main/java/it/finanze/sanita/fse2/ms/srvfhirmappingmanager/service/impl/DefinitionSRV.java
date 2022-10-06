package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureDefinition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IDefinitionSRV;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefinitionSRV implements IDefinitionSRV {

    @Override
    public Map<String, StructureDefinition> createDefinitions(String version, MultipartFile[] files) throws DataProcessingException {
        Map<String, StructureDefinition> filesToAdd = new HashMap<>();
        for(MultipartFile file : files) {
            String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
            filesToAdd.put(fileName, StructureDefinition.fromMultipart(fileName, version, file));
        }
        return filesToAdd;
    }

    @Override
    public Map<String, StructureDefinition> updateValuesets(String version, List<StructureDefinition> structureDefinitions, MultipartFile[] files) throws DataProcessingException {
        Map<String, StructureDefinition> filesToAdd = new HashMap<>();
        for (MultipartFile file : files) {
            String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
            filesToAdd.put(fileName, StructureDefinition.fromMultipart(fileName, version, file));
        }

        // Append to filesToAdd object that are not passed as files
        for (StructureDefinition definition : structureDefinitions) {
            if (!filesToAdd.containsKey(definition.getNameDefinition())) {
                filesToAdd.put(definition.getNameDefinition(), definition);
            }
        }

        return filesToAdd;
    }
}
