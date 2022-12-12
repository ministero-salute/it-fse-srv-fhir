/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import org.bson.Document;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;

public abstract class AbstractTest {

	public static final String TRANSFORM_TEST_ROOT_A = "T_Root_A";
	public static final String TRANSFORM_TEST_ROOT_B = "T_Root_B";
	public static final String TRANSFORM_TEST_ROOT_C = "T_Root_C";
	public static final String TRANSFORM_TEST_ROOT_D = "T_Root_D";
	public static final String TRANSFORM_TEST_ROOT_E = "T_Root_E";

	public static final String TRANSFORM_TEST_VERSION_A = "T_Version_A";
	public static final String TRANSFORM_TEST_VERSION_B = "T_Version_B";
	public static final String TRANSFORM_TEST_VERSION_C = "T_Version_C";
	public static final String TRANSFORM_TEST_VERSION_D = "T_Version_D";
	public static final String TRANSFORM_TEST_VERSION_E = "T_Version_E";

	public static final String TRANSFORM_DEMO_ETY = "{\"template_id_root\":\"Root_A\",\"version\":\"1.1\",\"insertion_date\":{\"$date\":{\"$numberLong\":\"1665493776553\"}},\"last_update_date\":{\"$date\":{\"$numberLong\":\"1665493776553\"}},\"root_map\":\"map1\",\"maps\":[{\"nameMap\":\"map1\",\"filenameMap\":\"map1.map\",\"contentMap\":{\"$binary\":{\"base64\":\"SGVsbG8gV29ybGQh\",\"subType\":\"00\"}}}],\"valuesets\":[{\"filenameValueset\":\"valueSet1.json\",\"nameValueset\":\"valueSet1\",\"contentValueset\":{\"$binary\":{\"base64\":\"SGVsbG8gV29ybGQh\",\"subType\":\"00\"}}}],\"definitions\":[{\"nameDefinition\":\"structureDefinition1\",\"filenameDefinition\":\"structureDefinition1.json\",\"contentDefinition\":{\"$binary\":{\"base64\":\"SGVsbG8gV29ybGQh\",\"subType\":\"00\"}}}],\"deleted\":false}";

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
		mongo.insert(Document.parse(TRANSFORM_DEMO_ETY), "test_transform");
	}
}
