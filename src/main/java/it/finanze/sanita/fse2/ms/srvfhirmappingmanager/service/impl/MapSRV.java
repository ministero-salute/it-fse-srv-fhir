package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Map;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IMapSRV;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapSRV implements IMapSRV {

    @Override
    public List<Map> insertDocsByName(String root, String extension, MultipartFile[] files) throws DataProcessingException {
        List<Map> filesToAdd = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
            filesToAdd.add(Map.fromMultipart(fileName, root, extension, file));
        }
        return filesToAdd;
    }
}
