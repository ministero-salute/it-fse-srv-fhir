/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import brave.Tracer;
import com.mongodb.MongoException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.TransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.JsonUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.*;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class TransformControllerTest extends AbstractTest {
	
	private final String TEST_URI = "uri-test";

	private final String TEST_URI_NF = "uri-test-NF";

	public final String TEST_JSON_TRANSFORM = "{\"filename\":\"map1.map\",\"templateIdRoot\":[\"2.16.840.1.113883.2.9.10.1.1\"],\"version\":\"0.1\",\"content\":\"content-test\",\"uri\":\"uri-test\",\"type\":\"Map\",\"insertion_date\":{\"$date\":{\"$numberLong\":\"1676370555758\"}},\"last_update_date\":{\"$date\":{\"$numberLong\":\"1676370555758\"}},\"deleted\":false}";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private Tracer tracer;

	@SpyBean
	private ITransformSRV transformSRV;

	@BeforeEach
	void init() {
		mongo.dropCollection(TransformETY.class);
	}
	
	@Test
	void livenessCheckCtlTest() throws Exception {
		mvc.perform(getLiveness()).andExpect(status().isOk());
	}

	@Test
	void insertTransformTest() throws Exception {
		mvc.perform(createTransform(createTestEntity())).andExpect(status().isCreated());
		// if reinserting same transform, will throw a 409 error
		mvc.perform(createTransform(createTestEntity())).andExpect(status().isConflict());
	}

	@Test
	void insertTransformFailedForExceptionsOnMongo() throws Exception {
		// Provide knowledge
		doThrow(new MongoException("Test error")).when(mongo).insert(any(TransformETY.class));
		mvc.perform(createTransform(createTestEntity())).andExpect(status().is5xxServerError());

		// Provide knowledge
		doThrow(new BusinessException("Test error")).when(mongo).insert(any(TransformETY.class));
		mvc.perform(createTransform(createTestEntity())).andExpect(status().is5xxServerError());
	}

	@Test
	void insertTransformWithEmptyBody() throws Exception {
		// Provide knowledge
		TransformETY e = createTestEntity();
		e.setContent(new MockMultipartFile(API_PATH_FILE_VAR, new byte[0]));
		// Execute
		mvc.perform(createTransform(e)).andExpect(status().isBadRequest());
	}

	@Test
	void insertTransformWithWrongParams() throws Exception {
		// Provide knowledge
		TransformETY e = createTestEntity();
		e.setUri(null);
		e.setVersion(null);
		// uri and version missing
		mvc.perform(createTransform(e)).andExpect(status().is4xxClientError());
		// Set URI
		e.setUri(MOCK_URI_ETY);
		// version missing
		mvc.perform(createTransform(e)).andExpect(status().is4xxClientError());
		e.setVersion(MOCK_FILENAME_ETY);
		// wrong version
		mvc.perform(createTransform(e)).andExpect(status().is4xxClientError());
		e.setContent(null);
		// map missing
		mvc.perform(createTransform(e)).andExpect(status().is4xxClientError());
	}

	@Test
	void updateTransform() throws Exception {
		//// first insertion
		prepareCollection();

		// Update ETY
		MockMultipartFile newMap = new MockMultipartFile("map", "map1.map",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartHttpServletRequestBuilder builderUpdate = MockMvcRequestBuilders.multipart(getBaseUrl() + "/transform");

		builderUpdate.with(request -> {
			request.setMethod(PUT.name());
			return request;
		});

		mvc.perform(builderUpdate
					.part(new MockPart(API_PATH_URI_VAR, "uri-test".getBytes()))
					.part(new MockPart(API_PATH_VERSION_VAR, "0.2".getBytes()))
					.file(new MockMultipartFile(API_PATH_FILE_VAR, newMap.getBytes()))
					.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is2xxSuccessful());

	}

	@Test
	void updateTransformSameVersion() throws Exception {
		//// first insertion
		prepareCollection();

		// Update ETY

		MockMultipartFile newMap = new MockMultipartFile("map", "map1.map",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartHttpServletRequestBuilder builderUpdate = MockMvcRequestBuilders.multipart(getBaseUrl() + "/transform");

		builderUpdate.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
				return request;
			}
		});

		TransformBodyDTO dto = JsonUtility.toJsonObject(TEST_JSON_TRANSFORM, TransformBodyDTO.class);

		// if same version
		mvc.perform(builderUpdate
					.part(new MockPart(API_PATH_URI_VAR, "uri-test".getBytes()))
					.part(new MockPart(API_PATH_VERSION_VAR, dto.getVersion().getBytes()))
					.file(new MockMultipartFile(API_PATH_FILE_VAR, newMap.getBytes()))
					.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().isConflict());
	}

	@Test
	void updateTransformNotFound() throws Exception {
		// Update ETY

		MockMultipartFile newMap = new MockMultipartFile("map", "map2.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartHttpServletRequestBuilder builderUpdate = MockMvcRequestBuilders.multipart(getBaseUrl() + "/transform");

		builderUpdate.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
				return request;
			}
		});

		TransformBodyDTO dto = JsonUtility.toJsonObject(TEST_JSON_TRANSFORM, TransformBodyDTO.class);

		mvc.perform(builderUpdate
						.file(newMap)
						.part(new MockPart(API_PATH_URI_VAR, "uri-test2".getBytes()))
						.part(new MockPart(API_PATH_VERSION_VAR, dto.getVersion().getBytes()))
						.file(new MockMultipartFile(API_PATH_FILE_VAR, newMap.getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteTransformNotFoundTest() throws Exception {
		
		MockMultipartHttpServletRequestBuilder builderDelete = MockMvcRequestBuilders.multipart(getBaseUrl() + "/transform");

		builderDelete.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("DELETE");
				return request;
			}
		});
		
		mvc.perform(builderDelete
				.part(new MockPart(API_PATH_URI_VAR, TEST_URI_NF.getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA)
		)
		.andExpect(status().isNotFound());
	}

	@Test
	void deleteTransformTest() throws Exception {
		prepareCollection();
		
		MockMultipartHttpServletRequestBuilder builderDelete = MockMvcRequestBuilders.multipart(getBaseUrl() + "/transform");

		builderDelete.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("DELETE");
				return request;
			}
		});
		
		mvc.perform(builderDelete
				.part(new MockPart(API_PATH_URI_VAR, TEST_URI.getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA)
		)
		.andExpect(status().is2xxSuccessful());
		
	}
	
	@Test
	void findTransformByIdDocumentTest() throws Exception {
		TransformETY e = mongo.insert(createTestEntity());
		mvc.perform(getTransformByIdMockRequest(e.getId())).andExpect(status().is2xxSuccessful());
	}

	@Test
	void findTransformByIdDocumentNotFoundTest() throws Exception {
		mvc.perform(getTransformByIdMockRequest(MOCK_FAKE_ID_ETY)).andExpect(status().isNotFound());
	}

	@Test
	void findTransformByIdExceptionsTest() throws Exception {
		prepareCollection();
		doThrow(new MongoException("")).when(mongo).findOne(any(Query.class), ArgumentMatchers.eq(TransformETY.class));
		mvc.perform(getTransformByIdMockRequest(MOCK_FAKE_ID_ETY)).andExpectAll(status().is5xxServerError());
		doThrow(new BusinessException("")).when(mongo).findOne(any(Query.class), ArgumentMatchers.eq(TransformETY.class));
		mvc.perform(getTransformByIdMockRequest(MOCK_FAKE_ID_ETY)).andExpectAll(status().is5xxServerError());
	}

	@Test
	void getTransformsTest() throws Exception {
		prepareCollection();
		mvc.perform(getTransformByUri(MOCK_URI_ETY)).andExpect(status().is2xxSuccessful());
	}
	
	
	
}
