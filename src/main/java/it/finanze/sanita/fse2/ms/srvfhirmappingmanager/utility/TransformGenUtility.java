/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureDefinition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureMap;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureValueset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TransformGenUtility {
    private TransformGenUtility() {}

    /**
     * Create definitions by files
     * @param version
     * @param files
     * @return
     * @throws DataProcessingException
     */
    public static Map<String, StructureDefinition> createDefinitions(String version, MultipartFile[] files) throws DataProcessingException {
        Map<String, StructureDefinition> filesToAdd = new HashMap<>();
        if(files != null) {
            for (MultipartFile file : files) {
                String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
                filesToAdd.put(fileName, StructureDefinition.fromMultipart(fileName, version, file));
            }
        }
        return filesToAdd;
    }

    /**
     * Update definitions by files and appending the existing ones to new object
     * @param version
     * @param structureDefinitions
     * @param files
     * @return
     * @throws DataProcessingException
     */
    public static Map<String, StructureDefinition> updateDefinitions(String version, List<StructureDefinition> structureDefinitions, MultipartFile[] files) throws DataProcessingException {
        Map<String, StructureDefinition> filesToAdd = new HashMap<>();

        if(files != null) {
            for (MultipartFile file : files) {
                String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
                filesToAdd.put(fileName, StructureDefinition.fromMultipart(fileName, version, file));
            }
        }

        // Append to filesToAdd object that are not passed as files
        for (StructureDefinition definition : structureDefinitions) {
            if (!filesToAdd.containsKey(definition.getNameDefinition())) {
                filesToAdd.put(definition.getNameDefinition(), definition);
            }
        }

        return filesToAdd;
    }

    /**
     * Create maps by files
     * @param rootMapFileName
     * @param files
     * @return
     * @throws DataProcessingException
     * @throws DocumentNotFoundException
     */
    public static Map<String, StructureMap> createMaps(String rootMapFileName, MultipartFile[] files) throws DataProcessingException, DocumentNotFoundException {
        Map<String, StructureMap> filesToAdd = new HashMap<>();
        try {
//        	checkRootMap(rootMapFileName, files);
        	boolean rootFind = false;
        	for (MultipartFile file : files) {
        		String fileString = new String(file.getBytes());
        		Pattern pattern = Pattern.compile("^ *map.*= *\"(.*)\"");
        		Matcher matcher = pattern.matcher(fileString);
        		String name = FilenameUtils.removeExtension(file.getOriginalFilename());
        		
        		if(matcher.find()) {
        			name = matcher.group(1);
        		}
        		
        		if(Boolean.FALSE.equals(rootFind) && rootMapFileName.equals(name)) {
        			rootFind = true;
        		}
        		
        		filesToAdd.put(name, StructureMap.fromMultipart(name, file));
        	}
        	
        	if(!rootFind) {
        		throw new BusinessException("Root not found ");
        	}
        } catch(Exception ex) {
        	log.error("Error while perform create maps : " , ex);
        	throw new BusinessException("Error while perform create maps : " , ex);
        }
        
        return filesToAdd;
    }

    /**
     * Update existing maps by files, appending the ones not passed to new object
     * @param structureMaps
     * @param files
     * @return
     * @throws DataProcessingException
     */
    public static Map<String, StructureMap> updateMaps(List<StructureMap> structureMaps, MultipartFile[] files) throws DataProcessingException {
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

    /**
     * Insert valuesets and return inserted entities to service
     * @param files
     * @return
     * @throws OperationException
     * @throws DocumentAlreadyPresentException
     * @throws DataProcessingException
     */
    public static Map<String, StructureValueset> createValuesets(MultipartFile[] files) throws DataProcessingException {
        Map<String, StructureValueset> filesToAdd = new HashMap<>();
        if(files != null) {
            for (MultipartFile file : files) {
                String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
                filesToAdd.put(fileName, StructureValueset.fromMultipart(fileName, file));
            }
        }
        // Insert it
        return filesToAdd;
    }

    /**
     * Update existing valuesets by files and appending the ones not passed in the new object
     * @param structureValuesets
     * @param files
     * @return
     * @throws DataProcessingException
     */
    public static Map<String, StructureValueset> updateValuesets(List<StructureValueset> structureValuesets, MultipartFile[] files) throws DataProcessingException {
        Map<String, StructureValueset> filesToAdd = new HashMap<>();

        if(files != null) {
            for (MultipartFile file : files) {
                String fileName = FilenameUtils.removeExtension(file.getOriginalFilename());
                filesToAdd.put(fileName, StructureValueset.fromMultipart(fileName, file));
            }
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
