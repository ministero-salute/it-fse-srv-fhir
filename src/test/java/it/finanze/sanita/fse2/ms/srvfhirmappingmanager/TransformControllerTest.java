/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getAllTransformMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getTransformByIdMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.API_PATH_FILE_VAR;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.API_PATH_TYPE_VAR;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.API_PATH_URI_VAR;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.API_PATH_VERSION_VAR;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.MongoException;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.TransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.JsonUtility;

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
		mvc.perform(get("/status").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpectAll(
				status().isOk());
	}

	@Test
	void insertTransformTest() throws Exception {
		MockMultipartFile map = new MockMultipartFile("map", "map1.map",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		TransformBodyDTO dto = JsonUtility.toJsonObject(TEST_JSON_TRANSFORM, TransformBodyDTO.class);

		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart(API_PATH_URI_VAR, "uri-test".getBytes()))
						.part(new MockPart(API_PATH_VERSION_VAR, dto.getVersion().getBytes()))
						.part(new MockPart(API_PATH_TYPE_VAR, FhirTypeEnum.Map.toString().getBytes()))
						.part(new MockPart("template_id_root", dto.getTemplateIdRoot().toString().getBytes()))
						.file(new MockMultipartFile(API_PATH_FILE_VAR, map.getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().isCreated());

		// if reinserting same transform, will throw a 4XX error
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart(API_PATH_URI_VAR, "uri-test".getBytes()))
						.part(new MockPart(API_PATH_VERSION_VAR, dto.getVersion().getBytes()))
						.part(new MockPart(API_PATH_TYPE_VAR, FhirTypeEnum.Map.toString().getBytes()))
						.part(new MockPart("template_id_root", dto.getTemplateIdRoot().toString().getBytes()))
						.file(new MockMultipartFile(API_PATH_FILE_VAR, map.getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().isConflict());

	}

	@Test
	void insertTransformFailedForExceptionsOnMongo() throws Exception {
		MockMultipartFile map = new MockMultipartFile("map", "map1.map",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		TransformBodyDTO dto = JsonUtility.toJsonObject(TEST_JSON_TRANSFORM, TransformBodyDTO.class);

		// Mongo exception
		Mockito.doThrow(new MongoException("")).when(mongo).insert(any(TransformETY.class));

		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart(API_PATH_URI_VAR, "uri-test".getBytes()))
						.part(new MockPart(API_PATH_VERSION_VAR, dto.getVersion().getBytes()))
						.part(new MockPart(API_PATH_TYPE_VAR, FhirTypeEnum.Map.toString().getBytes()))
						.file(new MockMultipartFile(API_PATH_FILE_VAR, map.getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is5xxServerError());

		// Generic exception
		Mockito.doThrow(new BusinessException("")).when(mongo).insert(any(TransformETY.class));

		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart(API_PATH_URI_VAR, "uri-test".getBytes()))
						.part(new MockPart(API_PATH_VERSION_VAR, dto.getVersion().getBytes()))
						.part(new MockPart(API_PATH_TYPE_VAR, FhirTypeEnum.Map.toString().getBytes()))
						.file(new MockMultipartFile(API_PATH_FILE_VAR, map.getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is5xxServerError());

	}

	@Test
	void insertTransformWithEmptyBody() throws Exception {
		MockMultipartFile map = new MockMultipartFile("map", "map1.map",
				MediaType.APPLICATION_JSON_VALUE, "".getBytes());

		TransformBodyDTO dto = JsonUtility.toJsonObject(TEST_JSON_TRANSFORM, TransformBodyDTO.class);

		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart(API_PATH_URI_VAR, "uri-test".getBytes()))
						.part(new MockPart(API_PATH_VERSION_VAR, dto.getVersion().getBytes()))
						.part(new MockPart(API_PATH_TYPE_VAR, FhirTypeEnum.Map.toString().getBytes()))
						.file(new MockMultipartFile(API_PATH_FILE_VAR, map.getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().isBadRequest());

	}

	@Test
	void insertTransformWithWrongParams() throws Exception {
		MockMultipartFile map = new MockMultipartFile("map", "map1.map",
				MediaType.APPLICATION_JSON_VALUE, "".getBytes());

		TransformBodyDTO dto = JsonUtility.toJsonObject(TEST_JSON_TRANSFORM, TransformBodyDTO.class);

		// uri and version missing
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart(API_PATH_TYPE_VAR, FhirTypeEnum.Map.toString().getBytes()))
						.file(new MockMultipartFile(API_PATH_FILE_VAR, map.getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is4xxClientError());


		// version missing
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart(API_PATH_URI_VAR, "uri-test".getBytes()))
						.part(new MockPart(API_PATH_TYPE_VAR, FhirTypeEnum.Map.toString().getBytes()))
						.file(new MockMultipartFile(API_PATH_FILE_VAR, map.getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is4xxClientError());


		// wrong version
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart(API_PATH_URI_VAR, "uri-test".getBytes()))
						.part(new MockPart(API_PATH_VERSION_VAR, "version-wrong".getBytes()))
						.part(new MockPart(API_PATH_TYPE_VAR, FhirTypeEnum.Map.toString().getBytes()))
						.file(new MockMultipartFile(API_PATH_FILE_VAR, map.getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is4xxClientError());

		// map missing
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart(API_PATH_URI_VAR, "uri-test".getBytes()))
						.part(new MockPart(API_PATH_VERSION_VAR, dto.getVersion().getBytes()))
						.part(new MockPart(API_PATH_TYPE_VAR, FhirTypeEnum.Map.toString().getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is4xxClientError());

	}

	@Test
	void updateTransform() throws Exception {
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
		prepareCollection();
		
		MvcResult result = mvc.perform(getAllTransformMockRequest()).andReturn();
		String response = result.getResponse().getContentAsString();
		Gson gson = new Gson();
		JsonObject json = gson.fromJson(response, JsonObject.class);
		JsonArray items = json.getAsJsonArray("items");
		String id = items.get(0).getAsJsonObject().getAsJsonPrimitive("id").getAsString();
		
		mvc.perform(getTransformByIdMockRequest(id)).andExpect(status().is2xxSuccessful());
	}

	@Test
	void findTransformByIdDocumentNotFoundTest() throws Exception {
		mvc.perform(getTransformByIdMockRequest("690000000000000000000000")).andExpect(status().isNotFound());
	}

	@Test
	void findTransformByIdExceptionsTest() throws Exception {
		prepareCollection();
		Mockito.doThrow(new MongoException("")).when(mongo).findOne(any(Query.class), ArgumentMatchers.eq(TransformETY.class));
		mvc.perform(getTransformByIdMockRequest("690000000000000000000000")).andExpectAll(status().is5xxServerError());
		Mockito.doThrow(new BusinessException("")).when(mongo).findOne(any(Query.class), ArgumentMatchers.eq(TransformETY.class));
		mvc.perform(getTransformByIdMockRequest("690000000000000000000000")).andExpectAll(status().is5xxServerError());
	}

	@Test
	void getTransformsTest() throws Exception {
		prepareCollection();

		MockMultipartHttpServletRequestBuilder builderGet = MockMvcRequestBuilders.multipart(getBaseUrl() + "/transform");

		builderGet.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("GET");
				return request;
			}
		});
		
		mvc.perform(builderGet
				.part(new MockPart(API_PATH_URI_VAR, TEST_URI.getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA)
		)
		.andExpect(status().is2xxSuccessful());
	}
	
	
	
}
