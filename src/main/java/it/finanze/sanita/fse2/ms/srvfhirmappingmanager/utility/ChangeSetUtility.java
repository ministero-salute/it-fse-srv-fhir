package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.DefinitionCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.MapCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.ValuesetCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.XSLTransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.DefinitionETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.ValuesetETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;

public final class ChangeSetUtility {

	private ChangeSetUtility() {
		
	}

	/**
	 * Creates a ChangesetDTO from a XslTransformETY.
	 * @param entity
	 * @return
	 */
	public static ChangeSetDTO<XSLTransformCS> xslTransformToChangeset(XslTransformETY entity) {
		return new ChangeSetDTO<>(entity.getId(), new XSLTransformCS(entity.getTemplateIdRoot(), entity.getVersion()));
    }

	public static ChangeSetDTO<ValuesetCS> toChangeset(ValuesetETY entity) {
		return new ChangeSetDTO<>(entity.getId(), new ValuesetCS(entity.getNameValueset()));
	}

	public static ChangeSetDTO<DefinitionCS> toChangeset(DefinitionETY entity) {
		return new ChangeSetDTO<>(entity.getId(), new DefinitionCS(entity.getNameDefinition(), entity.getVersionDefinition()));
	}

	public static ChangeSetDTO<MapCS> toChangeset(MapETY entity) {
		return new ChangeSetDTO<>(entity.getId(), new MapCS(entity.getNameMap(), entity.getTemplateIdRoot(), entity.getTemplateIdExtension()));
	}
}
