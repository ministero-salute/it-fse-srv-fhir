/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import org.bson.Document;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

public abstract class AbstractTest {

	public final String TRANSFORM_DEMO_ETY = "{\"filename\":\"map1.map\",\"templateIdRoot\":[\"2.16.840.1.113883.2.9.10.1.1\"],\"version\":\"0.1\",\"content\":{\"$binary\":{\"base64\":\"\",\"subType\":\"00\"}},\"uri\":\"uri-test\",\"type\":\"Map\",\"insertion_date\":{\"$date\":{\"$numberLong\":\"1676370555758\"}},\"last_update_date\":{\"$date\":{\"$numberLong\":\"1676370555758\"}},\"deleted\":false}";

	protected String getBaseUrl() {
		return "/v1";
	}

	@SpyBean
	protected MongoTemplate mongo;

	public static MockMultipartFile createFakeFile(String filename) {
		return new MockMultipartFile("files", filename, "multipart/form-data", "Hello world!".getBytes());
	}

	public final String FAKE_INVALID_DTO_ID = "||----test";
	
	/**
	 * Sample parameter for multiple tests
	 */
	public static final String SCHEMA_TEST_ROOT_B = "file.xsd";


	public static MockMultipartFile createFakeMultipart(String filename) {
		return new MockMultipartFile("files", filename, APPLICATION_XML_VALUE, "Hello world!".getBytes());
	}

	public void prepareCollection() {
		mongo.insert(Document.parse(TRANSFORM_DEMO_ETY), mongo.getCollectionName(TransformETY.class));
	}
}
