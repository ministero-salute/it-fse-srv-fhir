/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright 2023 Ministero della Salute
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IChangeSetRepo.*;

/**
 * Model to save generic transform.
 */
@Document(collection = "#{@transformBean}")
@Data
@Slf4j
@NoArgsConstructor
public class TransformETY {
	
	public static final String FIELD_TEMPLATE_ID_ROOT = "template_id_root";
	public static final String FIELD_VERSION = "version";
	public static final String FIELD_URI = "uri";
	public static final String FIELD_FILENAME = "filename";
	public static final String FIELD_CONTENT = "content";
	public static final String FIELD_TYPE = "type";

	@Id
	private String id;

	@Field(name = FIELD_FILENAME)
	private String filename;

	@Field(name = FIELD_TEMPLATE_ID_ROOT)
	private List<String> templateIdRoot;
	
	@Field(name = FIELD_VERSION)
	private String version;

	@Field(name = FIELD_CONTENT)
	private Binary content;
	
	@Field(name = FIELD_URI )
	private String uri;
	
	@Field(name = FIELD_TYPE)
	private FhirTypeEnum type;

	@Field(name = FIELD_INSERTION_DATE)
	private Date insertionDate;

	@Field(name = FIELD_LAST_UPDATE)
	private Date lastUpdateDate;

	@Field(name = FIELD_DELETED)
	private boolean deleted;

	public void setContent(MultipartFile file) throws DataProcessingException {
		if(file != null) {
			try {
				this.content = new Binary(file.getBytes());
			} catch (IOException e) {
				throw new DataProcessingException(Logs.ERR_ETY_BINARY_CONVERSION, e);
			}
		} else {
			log.warn("Skipping setContent() file is null");
		}
	}
	
	public static TransformETY fromComponents(String uri, String version, List<String> roots, FhirTypeEnum type, MultipartFile file) throws DataProcessingException {
		Date date = new Date();
		TransformETY e = new TransformETY();
		e.setUri(uri);
		e.setVersion(version);
		if (type == FhirTypeEnum.Map) e.setTemplateIdRoot(roots);
		e.setType(type);
		e.setFilename(file.getOriginalFilename());
		e.setContent(file);
		e.setInsertionDate(date);
		e.setLastUpdateDate(date);
		e.setDeleted(false);
		return e;
	}
	

}
