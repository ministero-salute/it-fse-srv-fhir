package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Valueset;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IValuesetSRV;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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
    public List<Valueset> insertDocsByName(MultipartFile[] files) throws OperationException, DocumentAlreadyPresentException, DataProcessingException {
        List<Valueset> filesToAdd = new ArrayList<>();
        for(MultipartFile file : files) {
            String name = FilenameUtils.removeExtension(file.getOriginalFilename());
            filesToAdd.add(Valueset.fromMultipart(name, file));
        }
        // Insert it
        return filesToAdd;
    }
}
