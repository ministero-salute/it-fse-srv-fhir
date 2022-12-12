/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.specs.TransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;

public final class ChangeSetUtility {

	private ChangeSetUtility() {
		
	}

	/**
	 * Creates a ChangesetDTO from TransformETY
	 * @param entity
	 * @return
	 */
	public static ChangeSetDTO<TransformCS> transformToChangeset(TransformETY entity) {
		return new ChangeSetDTO<>(entity.getId(), new TransformCS(entity.getTemplateIdRoot(), entity.getVersion()));
	}
}
