package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Content;


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
			contact.setEmail(customOpenapi.getContactMail());
			openApi.getInfo().setContact(contact);

			// Adding extensions
			openApi.getInfo().addExtension("x-api-id", customOpenapi.getApiId());
			openApi.getInfo().addExtension("x-summary", customOpenapi.getApiSummary());

			// Adding servers
			final List<Server> servers = new ArrayList<>();
			final Server devServer = new Server();
			devServer.setDescription("Gateway Dispatcher Development URL");
			devServer.setUrl("http://localhost:" + customOpenapi.getPort());
			devServer.addExtension("x-sandbox", true);

			servers.add(devServer);
			openApi.setServers(servers);

			openApi.getComponents().getSchemas().values().forEach(this::setAdditionalProperties);


			openApi.getPaths().values().stream().filter(item -> item.getPost() != null).forEach(item -> {

				final Schema<MediaType> schema = item.getPost().getRequestBody().getContent().get(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE).getSchema();

				schema.additionalProperties(false);
				if(schema.getProperties().get(Constants.App.CONTENT_MULTIPART_FILE) != null){
					schema.getProperties().get(Constants.App.CONTENT_MULTIPART_FILE).setMaxLength(customOpenapi.getFileMaxLength());
				}
				

			});

			openApi.getPaths().values().stream().filter(item -> item.getPut() != null).forEach(item -> {

				final Schema<MediaType> schema = item.getPut().getRequestBody().getContent().get(org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE).getSchema();

				schema.additionalProperties(false);
				if(schema.getProperties().get(Constants.App.CONTENT_MULTIPART_FILE) != null){
					schema.getProperties().get(Constants.App.CONTENT_MULTIPART_FILE).setMaxLength(customOpenapi.getFileMaxLength());
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