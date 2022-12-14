/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.base;

import lombok.Data;

/**
 * The Class TransformResponseDTO.
 *
 * 
 * Transform Response.
 */
@Data
public class CrudInfoDTO {

	private Integer maps;

	private Integer definitions;

	private Integer valuesets;

	public CrudInfoDTO() {
	}

	public CrudInfoDTO(Integer maps, Integer definitions, Integer valuesets) {
		super();
		this.maps = maps;
		this.definitions = definitions;
		this.valuesets = valuesets;
	}

}
