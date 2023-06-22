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
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.API_PATH_FILE_VAR;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@Configuration
@SuppressWarnings("all")
public class OpenApiCFG {

	@Autowired
	private CustomSwaggerCFG customOpenapi;

	public OpenApiCFG() {
		// Empty constructor.
	}
	
	@Bean
	public OpenApiCustomiser openApiCustomiser() {

		return openApi -> {

			// Populating info section.
			openApi.getInfo().setTitle(customOpenapi.getTitle());
			openApi.getInfo().setVersion(customOpenapi.getVersion());
			openApi.getInfo().setDescription(customOpenapi.getDescription());
			openApi.getInfo().setTermsOfService(customOpenapi.getTermsOfService());

			// Adding contact to info section
			final Contact contact = new Contact();
			contact.setName(customOpenapi.getContactName());
			contact.setUrl(customOpenapi.getContactUrl());
			openApi.getInfo().setContact(contact);

			// Adding extensions
			openApi.getInfo().addExtension("x-api-id", customOpenapi.getApiId());
			openApi.getInfo().addExtension("x-summary", customOpenapi.getApiSummary());

			for (final Server server : openApi.getServers()) {
				final Pattern pattern = Pattern.compile("^https://.*");
				if (!pattern.matcher(server.getUrl()).matches()) {
					server.addExtension("x-sandbox", true);
				}
			}

			openApi.getComponents().getSchemas().values().forEach(this::setAdditionalProperties);

			openApi.getPaths().values().stream().filter(item -> item.getPost() != null).forEach(item -> {

				final Schema<MediaType> schema = item.getPost().getRequestBody().getContent().get(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE).getSchema();

				schema.additionalProperties(false);
				if(schema.getProperties().get(API_PATH_FILE_VAR) != null){
					schema.getProperties().get(API_PATH_FILE_VAR).setMaxLength(customOpenapi.getFileMaxLength());
				}
			});

			openApi.getPaths().values().stream().filter(item -> item.getPut() != null).forEach(item -> {

				final Schema<MediaType> schema = item.getPut().getRequestBody().getContent().get(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE).getSchema();

				schema.additionalProperties(false);
				if(schema.getProperties().get(API_PATH_FILE_VAR) != null){
					schema.getProperties().get(API_PATH_FILE_VAR).setMaxLength(customOpenapi.getFileMaxLength());
				}
			});
		};
	}

	private void disableAdditionalPropertiesToMultipart(Content content) {
        if (content.containsKey(MULTIPART_FORM_DATA_VALUE)) {
            content.get(MULTIPART_FORM_DATA_VALUE).getSchema().setAdditionalProperties(false);
        }
    }

	private void setAdditionalProperties(Schema<?> schema) {
		if (schema == null) return;
		schema.setAdditionalProperties(false);
		handleSchema(schema);
	}
	
	private void handleSchema(Schema<?> schema) {
		getProperties(schema).forEach(this::handleArraySchema);
		handleArraySchema(schema);
	}

	private Collection<Schema> getProperties(Schema<?> schema) {
		if (schema.getProperties() == null) return new ArrayList<>();
		return schema.getProperties().values();
	}

	private void handleArraySchema(Schema<?> schema) {
		ArraySchema arraySchema = getSchema(schema, ArraySchema.class);
		if (arraySchema == null) return;
		setAdditionalProperties(arraySchema.getItems());
	}

	private <T> T getSchema(Schema<?> schema, Class<T> clazz) {
	    try { return clazz.cast(schema); }
	    catch(ClassCastException e) { return null; }
	}

}