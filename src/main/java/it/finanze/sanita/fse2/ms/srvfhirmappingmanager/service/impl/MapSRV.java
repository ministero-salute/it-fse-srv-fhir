package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureMap;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IMapSRV;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MapSRV implements IMapSRV {

    @Override
    public Map<String, StructureMap> createMaps(String rootMapFileName, MultipartFile[] files) throws DataProcessingException, DocumentNotFoundException {
        this.checkRootMap(rootMapFileName, files);
        Map<String, StructureMap> filesToAdd = new HashMap<>();
        for (MultipartFile file : files) {
            String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
            filesToAdd.put(fileName, StructureMap.fromMultipart(fileName, file));
        }
        return filesToAdd;
    }

    @Override
    public Map<String, StructureMap> updateMaps(List<StructureMap> structureMaps, MultipartFile[] files) throws DataProcessingException {
        Map<String, StructureMap> filesToAdd = new HashMap<>();
        for (MultipartFile file : files) {
            String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
            filesToAdd.put(fileName, StructureMap.fromMultipart(fileName, file));
        }

        // Append to filesToAdd object that are not passed as files
        for (StructureMap map : structureMaps) {
            if (!filesToAdd.containsKey(map.getNameMap())) {
                filesToAdd.put(map.getNameMap(), map);
            }
        }

        return filesToAdd;
    }

    private void checkRootMap(String rootMapFileName, MultipartFile[] files) throws DocumentNotFoundException {
        String sanitizedRootMapFileName = FilenameUtils.removeExtension(rootMapFileName);
        Optional<MultipartFile> rootMapFile = Arrays.stream(files).filter(file -> sanitizedRootMapFileName.equals(FilenameUtils.removeExtension(file.getOriginalFilename()))).findFirst();
        if (!rootMapFile.isPresent()) {
            throw new DocumentNotFoundException("Root map file is not passed in files");
        }
    }
}
