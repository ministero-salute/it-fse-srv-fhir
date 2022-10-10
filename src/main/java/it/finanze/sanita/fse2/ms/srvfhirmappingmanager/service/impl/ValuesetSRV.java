package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureValueset;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IValuesetSRV;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValuesetSRV implements IValuesetSRV {
    /**
     * Insert valuesets and return inserted entities to service
     * @param files
     * @return
     * @throws OperationException
     * @throws DocumentAlreadyPresentException
     * @throws DataProcessingException
     */
    @Override
    public Map<String, StructureValueset> createValuesets(MultipartFile[] files) throws DataProcessingException {
        Map<String, StructureValueset> filesToAdd = new HashMap<>();
        for(MultipartFile file : files) {
            String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
            filesToAdd.put(fileName, StructureValueset.fromMultipart(fileName, file));
        }
        // Insert it
        return filesToAdd;
    }

    @Override
    public Map<String, StructureValueset> updateValuesets(List<StructureValueset> structureValuesets, MultipartFile[] files) throws DataProcessingException {
        Map<String, StructureValueset> filesToAdd = new HashMap<>();
        for (MultipartFile file : files) {
            String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
            filesToAdd.put(fileName, StructureValueset.fromMultipart(fileName, file));
        }

        // Append to filesToAdd object that are not passed as files
        for (StructureValueset valueset : structureValuesets) {
            if (!filesToAdd.containsKey(valueset.getNameValueset())) {
                filesToAdd.put(valueset.getNameValueset(), valueset);
            }
        }

        return filesToAdd;
    }
}
