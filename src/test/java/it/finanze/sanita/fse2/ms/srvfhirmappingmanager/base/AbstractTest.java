/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY.fromComponents;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.API_PATH_FILE_VAR;

public abstract class AbstractTest {

	public static final String MOCK_FAKE_ID_ETY = "63eb78f7d02c57742ff8367a";

	public static final String MOCK_URI_ETY = "https://ibm.example.com/StructureMap/MockEntity";
	public static final String MOCK_VERSION_ETY = "0.1";
	public static final String MOCK_NEW_VERSION_ETY = "0.2";
	public static final String MOCK_TMP_ROOT_0_ETY = "2.16.840.1.113883.2.9.10.1.1";
	public static final String MOCK_TMP_ROOT_1_ETY = "2.16.840.1.113883.2.9.10.1.2";
	public static final FhirTypeEnum MOCK_TYPE_ETY = FhirTypeEnum.Map;
	public static final List<String> MOCK_ROOTS_ETY = Arrays.asList(MOCK_TMP_ROOT_0_ETY, MOCK_TMP_ROOT_1_ETY);
	public static final String MOCK_FILENAME_ETY = "MockEntity.map";
	public static final byte[] MOCK_FILE_CONTENT_ETY = "hello-world".getBytes();
	public static final byte[] MOCK_FILE_NEW_CONTENT_ETY = "updated-content".getBytes();
	public static final MockMultipartFile MOCK_FILE_ETY = new MockMultipartFile(
		API_PATH_FILE_VAR, MOCK_FILENAME_ETY, null, MOCK_FILE_CONTENT_ETY
	);
	public static final MockMultipartFile MOCK_FILE_NEW_ETY = new MockMultipartFile(
		API_PATH_FILE_VAR, MOCK_FILENAME_ETY, null, MOCK_FILE_NEW_CONTENT_ETY
	);

	protected String getBaseUrl() {
		return "/v1";
	}

	@SpyBean
	protected MongoTemplate mongo;

	public final String FAKE_INVALID_DTO_ID = "||----test";

	public void prepareCollection() throws DataProcessingException {
		mongo.insert(createTestEntity());
	}

	protected TransformETY createTestEntity() throws DataProcessingException {
		return fromComponents(
			MOCK_URI_ETY,
			MOCK_VERSION_ETY,
			MOCK_ROOTS_ETY,
			MOCK_TYPE_ETY,
			MOCK_FILE_ETY
		);
	}

}
