package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Definition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IDefinitionSRV;

@Service
public class DefinitionSRV implements IDefinitionSRV {

    @Override
    public List<Definition> insertDocsByName(String version, MultipartFile[] files) throws DataProcessingException {
        List<Definition> filesToAdd = new ArrayList<>();
        for(MultipartFile file : files) {
            String name = FilenameUtils.removeExtension(file.getOriginalFilename());
            filesToAdd.add(Definition.fromMultipart(name, version, file));
        }
        return filesToAdd;
    }
}
