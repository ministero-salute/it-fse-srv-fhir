/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
@NoArgsConstructor
public class StructureDefinition {

    private String nameDefinition;
    private String filenameDefinition;
    private Binary contentDefinition;
    private String versionDefinition;

    public void setMultipartContentDefinition(MultipartFile file) throws DataProcessingException {
        try {
            this.contentDefinition = new Binary(file.getBytes());
        } catch (IOException e) {
            throw new DataProcessingException("Unable to encode multipart raw bytes into entity data", e);
        }
    }

    public static StructureDefinition fromMultipart(String name, String version, MultipartFile file) throws DataProcessingException {
        StructureDefinition entity = new StructureDefinition();
        entity.setFilenameDefinition(file.getOriginalFilename());
        entity.setNameDefinition(name);
        entity.setMultipartContentDefinition(file);
        entity.setVersionDefinition(version);
        return entity;
    }
    
    
    
    public static StructureDefinition fromPath(String name, String version, MultipartFile file) throws DataProcessingException {
    	StructureDefinition entity = new StructureDefinition();
        entity.setNameDefinition(name);
        entity.setFilenameDefinition(file.getOriginalFilename());
        entity.setMultipartContentDefinition(file);
        entity.setVersionDefinition(version);
        return entity;
    }

}
