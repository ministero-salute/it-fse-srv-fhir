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

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IChangeSetRepo.*;


/**
 * Model to save generic transform.
 */
@Document(collection = "#{@transformBean}")
@Data
@NoArgsConstructor
public class TransformETY {

	public static final String FIELD_TEMPLATE_ID_ROOT = "template_id_root";
	public static final String FIELD_VERSION = "version";
	public static final String FIELD_ROOT_MAP = "root_map";
	public static final String FIELD_MAPS = "maps";
	public static final String FIELD_VALUESETS = "valuesets";
	public static final String FIELD_DEFINITIONS = "definitions";

	@Id
	private String id;
	
	@Field(name = FIELD_TEMPLATE_ID_ROOT)
	private String templateIdRoot;
	
	@Field(name = FIELD_VERSION)
	private String version;

	@Field(name = FIELD_INSERTION_DATE)
	private Date insertionDate; 
	
	@Field(name = FIELD_LAST_UPDATE)
	private Date lastUpdateDate; 

	@Field(name = FIELD_ROOT_MAP)
	private String rootStructureMap;

	@Field(name = FIELD_MAPS)
	private List<StructureMap> structureMaps;

	@Field(name = FIELD_VALUESETS)
	private List<StructureValueset> structureValuesets;

	@Field(name = FIELD_DEFINITIONS)
	private List<StructureDefinition> structureDefinitions;

	@Field(name = FIELD_DELETED)
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