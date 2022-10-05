package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Definition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Map;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Valueset;
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
	
	@Field(name = "map")
	private List<Map> maps;

	@Field(name = "valueset")
	private List<Valueset> valuesets;

	@Field(name = "definition")
	private List<Definition> definitions;

	@Field(name = "deleted")
	private boolean deleted;

	public static TransformETY fromComponents(String templateIdRoot, String version, Date insertionDate, Date lastUpdateDate, List<Map> maps, List<Valueset> valuesets, List<Definition> definitions) {
		TransformETY entity = new TransformETY();
		entity.setTemplateIdRoot(templateIdRoot);
		entity.setVersion(version);
		entity.setInsertionDate(insertionDate);
		entity.setLastUpdateDate(lastUpdateDate);
		entity.setMaps(maps);
		entity.setValuesets(valuesets);
		entity.setDefinitions(definitions);
		entity.setDeleted(false);
		return entity;
	}
}