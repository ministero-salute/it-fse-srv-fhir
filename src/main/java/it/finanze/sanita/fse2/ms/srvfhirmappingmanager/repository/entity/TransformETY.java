/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureDefinition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureMap;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureValueset;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;


/**
 * Model to save generic transform.
 */
@Document(collection = "#{@transformBean}")
@Data
@NoArgsConstructor
public class TransformETY {

	@Id
	private String id;
	
	@Field(name = "template_id_root")
	private String templateIdRoot;
	
	@Field(name = "version")
	private String version;

	@Field(name = "insertion_date")
	private Date insertionDate; 
	
	@Field(name = "last_update_date")
	private Date lastUpdateDate; 

	@Field(name = "root_map")
	private String rootStructureMap;

	@Field(name = "maps")
	private List<StructureMap> structureMaps;

	@Field(name = "valuesets")
	private List<StructureValueset> structureValuesets;

	@Field(name = "definitions")
	private List<StructureDefinition> structureDefinitions;

	@Field(name = "deleted")
	private boolean deleted;

	public static TransformETY fromComponents(String templateIdRoot, String version, String rootMap, List<StructureMap> structureMaps, List<StructureDefinition> structureDefinitions, List<StructureValueset> structureValuesets) {
		Date date = new Date();
		TransformETY entity = new TransformETY();
		entity.setTemplateIdRoot(templateIdRoot);
		entity.setVersion(version);
		entity.setInsertionDate(date);
		entity.setLastUpdateDate(date);
		entity.setRootStructureMap(rootMap);
		entity.setStructureMaps(structureMaps);
		entity.setStructureValuesets(structureValuesets);
		entity.setStructureDefinitions(structureDefinitions);
		entity.setDeleted(false);
		return entity;
	}
}