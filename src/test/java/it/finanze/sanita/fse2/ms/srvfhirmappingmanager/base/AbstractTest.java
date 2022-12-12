/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.BsonBinarySubType;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureMap;

public abstract class AbstractTest {

	// --- XSLT
	public static final String XSLT_TEST_NAME_A = "test_xslt_A";
	public static final String XSLT_TEST_NAME_B = "test_xslt_B";
	public static final String XSLT_TEST_NAME_C = "test_xslt_C";
	public static final String XSLT_TEST_NAME_D = "test_xslt_D";
	public static final String XSLT_TEST_NAME_E = "test_xslt_E";

	public static final String XSLT_TEST_ROOT_A = "Root_A";
	public static final String XSLT_TEST_ROOT_B = "Root_B";
	public static final String XSLT_TEST_ROOT_C = "Root_C";
	public static final String XSLT_TEST_ROOT_D = "Root_D";
	public static final String XSLT_TEST_ROOT_E = "Root_E";

	public static final String XSLT_TEST_VERSION_A = "Version_A";
	public static final String XSLT_TEST_VERSION_B = "Version_B";
	public static final String XSLT_TEST_VERSION_C = "Version_C";
	public static final String XSLT_TEST_VERSION_D = "Version_D";
	public static final String XSLT_TEST_VERSION_E = "Version_E";

	public static final String XSLT_TEST_ID_A = "ID_A";
	public static final String FILE_TEST_STRING = "Hello World!";
	public static final String XSLT_TEST_STRING_UPDATED = "Hello World, folks!";
	public static final String XSLT_TEST_VERSION = "version";

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

	protected AbstractTest() {
		this.xslTransformEntities = new HashMap<>();

	}

	protected void populateXslTransform() {

		XslTransformETY xslTransformA = new XslTransformETY();
		xslTransformA.setNameXslTransform(XSLT_TEST_NAME_A);
		xslTransformA.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
		xslTransformA.setTemplateIdRoot(XSLT_TEST_ROOT_A);
		xslTransformA.setVersion(XSLT_TEST_VERSION);
		xslTransformA.setInsertionDate(new Date());
		xslTransformA.setLastUpdateDate(new Date());

		XslTransformETY xslTransformB = new XslTransformETY();
		xslTransformB.setNameXslTransform(XSLT_TEST_NAME_B);
		xslTransformB.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
		xslTransformB.setTemplateIdRoot(XSLT_TEST_ROOT_B);
		xslTransformB.setVersion(XSLT_TEST_VERSION);
		xslTransformB.setInsertionDate(new Date());
		xslTransformB.setLastUpdateDate(new Date());

		XslTransformETY xslTransformC = new XslTransformETY();
		xslTransformC.setNameXslTransform(XSLT_TEST_NAME_C);
		xslTransformC.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
		xslTransformC.setTemplateIdRoot(XSLT_TEST_ROOT_C);
		xslTransformC.setVersion(XSLT_TEST_VERSION);
		xslTransformC.setInsertionDate(new Date());
		xslTransformC.setLastUpdateDate(new Date());

		mongo.save(xslTransformA);
		mongo.save(xslTransformB);
		mongo.save(xslTransformC);

	}
	private void createXsltTestSchema() {
		mongo.createCollection(XslTransformETY.class);
	}

	protected void initTestRepository() throws Exception {
		createXsltTestSchema();

		populateXslTransform();
	}

	public static MockMultipartFile createFakeFile(String filename) {
		return new MockMultipartFile("files", filename, "multipart/form-data", "Hello world!".getBytes());
	}

	public final String FAKE_INVALID_DTO_ID = "||----test";
	public static final String XSLTRANSFORM_TEST_EXTS_A = "POCD_MT000040UV02";

	public static final String XSLTRANSFORM_TEST_EXTS_B = "POCD_MT000040UV03";

	public static final String XSLTRANSFORM_TEST_EXTS_C = "POCD_MT000040UV04";

	public static final String XSLTRANSFORM_TEST_EXTS_D = "7POCD_MT000040UV05";

	/**
	 * Sample parameter for multiple tests
	 */
	public static final String SCHEMA_TEST_ROOT_B = "file.xsd";
	/**
	 * Directory containing sample files to upload as test
	 */
	public static final Path XSLT_SAMPLE_FILES = Paths.get("src", "test", "resources", "xslTransform");

	/**
	 * Directory containing modified files used to replace sample ones
	 */
	public static final Path XSLT_MOD_SAMPLE_FILES = Paths.get("src", "test", "resources", "xslTransform", "files",
			"modified");

	private final Map<String, XslTransformETY> xslTransformEntities;
	// private final Map<String, XslTransformETY> xslTransformReplacements;

	protected void clearTestEntities() {
		this.xslTransformEntities.clear();

	}

	protected Map<String, XslTransformETY> getXslTransformEntities() {
		return new HashMap<>(xslTransformEntities);
	}

	protected List<XslTransformETY> getXslTransformEntitiesToUpload() {
		return new ArrayList<>(xslTransformEntities.values());
	}

	protected void setupTestEntities() throws IOException {
		addXslTransformTestEntityToMap(XSLTRANSFORM_TEST_EXTS_A);
		addXslTransformTestEntityToMap(XSLTRANSFORM_TEST_EXTS_B);
		addXslTransformTestEntityToMap(XSLTRANSFORM_TEST_EXTS_C);
		addXslTransformTestEntityToMap(XSLTRANSFORM_TEST_EXTS_D);
		// setXslTransformTestEntityToReplace();
	}

	private void addXslTransformTestEntityToMap(String extension) throws IOException {
		// List of all files inside the sample modified directory
		try (Stream<Path> files = Files.list(XSLT_SAMPLE_FILES)) {
			// Convert to list
			List<Path> samples = files.collect(Collectors.toList());

			MultipartFile fakeFile = createFakeMultipart(XSLT_TEST_ID_A);

			// Add to each map and convert
			for (Path path : samples) {
				this.xslTransformEntities
						.put(path.getFileName().toString(), XslTransformETY.fromPath(XSLT_TEST_NAME_A, XSLT_TEST_ID_A,
								XSLT_TEST_VERSION_A, fakeFile));
			}
		}
	}

	public static MockMultipartFile createFakeMultipart(String filename) {
		return new MockMultipartFile("files", filename, APPLICATION_XML_VALUE, "Hello world!".getBytes());
	}

	public void prepareCollection() {
		mongo.insert(Document.parse(TRANSFORM_DEMO_ETY), mongo.getCollectionName(TransformETY.class));
	}
}
