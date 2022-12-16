/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.deleteTransformMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getTransformByIdMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getTransformsMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.queryActiveTransformMockRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoException;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.TransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.JsonUtility;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class TransformControllerTest extends AbstractTest {
	private final String TEST_ID_ROOT = "Root_A";

	private final String TEST_ID_ROOT_NOT_FOUND = "Root_A_NF";

	public final String TEST_JSON_TRANSFORM = "{\"rootMapIdentifier\":\"map1\",\"templateIdRoot\":\"Root_A\", \"version\":\"1.1\"}";

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
	void insertTransformTest() throws Exception {
		MockMultipartFile maps = new MockMultipartFile("maps", "map1.map",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartFile defs = new MockMultipartFile("structureDefinitions", "structureDefinition1.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartFile valueSets = new MockMultipartFile("valueSets", "valueSet1.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		TransformBodyDTO dto = JsonUtility.toJsonObject(TEST_JSON_TRANSFORM, TransformBodyDTO.class);

		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", dto.getVersion().getBytes()))
						.file(maps)
						.file(defs)
						.file(valueSets)
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().isCreated());

		// if reinserting same transform, will throw a 4XX error
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", dto.getVersion().getBytes()))
						.file(maps)
						.file(defs)
						.file(valueSets)
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().isConflict());

	}

	@Test
	void insertTransformFailedForExceptionsOnMongo() throws Exception {
		MockMultipartFile maps = new MockMultipartFile("maps", "map1.map",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartFile defs = new MockMultipartFile("structureDefinitions", "structureDefinition1.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartFile valueSets = new MockMultipartFile("valueSets", "valueSet1.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		TransformBodyDTO dto = JsonUtility.toJsonObject(TEST_JSON_TRANSFORM, TransformBodyDTO.class);

		// Mongo exception
		Mockito.doThrow(new MongoException("")).when(mongo).insert(any(TransformETY.class));

		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", dto.getVersion().getBytes()))
						.file(maps)
						.file(defs)
						.file(valueSets)
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is5xxServerError());

		// Generic exception
		Mockito.doThrow(new BusinessException("")).when(mongo).insert(any(TransformETY.class));

		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", dto.getVersion().getBytes()))
						.file(maps)
						.file(defs)
						.file(valueSets)
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is5xxServerError());

	}

	@Test
	void insertTransformWithEmptyBody() throws Exception {
		MockMultipartFile maps = new MockMultipartFile("maps", "map1.map",
				MediaType.APPLICATION_JSON_VALUE, "".getBytes());

		MockMultipartFile defs = new MockMultipartFile("structureDefinitions", "structureDefinition1.json",
				MediaType.APPLICATION_JSON_VALUE, "".getBytes());

		MockMultipartFile valueSets = new MockMultipartFile("valueSets", "valueSet1.json",
				MediaType.APPLICATION_JSON_VALUE, "".getBytes());

		TransformBodyDTO dto = JsonUtility.toJsonObject(TEST_JSON_TRANSFORM, TransformBodyDTO.class);

		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", dto.getVersion().getBytes()))
						.file(maps)
						.file(defs)
						.file(valueSets)
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().isBadRequest());

	}

	@Test
	void insertTransformWithWrongParams() throws Exception {
		MockMultipartFile maps = new MockMultipartFile("maps", "map1.map",
				MediaType.APPLICATION_JSON_VALUE, "".getBytes());

		MockMultipartFile defs = new MockMultipartFile("structureDefinitions", "structureDefinition1.json",
				MediaType.APPLICATION_JSON_VALUE, "".getBytes());

		MockMultipartFile valueSets = new MockMultipartFile("valueSets", "valueSet1.json",
				MediaType.APPLICATION_JSON_VALUE, "".getBytes());

		TransformBodyDTO dto = JsonUtility.toJsonObject(TEST_JSON_TRANSFORM, TransformBodyDTO.class);

		// templateIdRoot and version missing
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.file(maps)
						.file(defs)
						.file(valueSets)
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is4xxClientError());


		// version missing
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.file(maps)
						.file(defs)
						.file(valueSets)
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is4xxClientError());


		// wrong version
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", "version".getBytes()))
						.file(maps)
						.file(defs)
						.file(valueSets)
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is4xxClientError());

		// maps, definitions, valueset missing
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", dto.getVersion().getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is4xxClientError());

		// definitions, valueset missing
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", dto.getVersion().getBytes()))
						.file(maps)
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is4xxClientError());

		// valueset missing
		mvc.perform(MockMvcRequestBuilders
						.multipart(getBaseUrl() + "/transform")
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", dto.getVersion().getBytes()))
						.file(maps)
						.file(defs)
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is4xxClientError());

	}

	@Test
	void updateTransform() throws Exception {
		//// first insertion
		prepareCollection();

		// Update ETY
		MockMultipartFile newMap = new MockMultipartFile("maps", "map1.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartFile newStructureDefs = new MockMultipartFile("structureDefinitions", "structureDefinitions2.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartFile newValuesets = new MockMultipartFile("valueSets", "valueSet2.json",
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
						.file(newStructureDefs)
						.file(newValuesets)
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", "1.2".getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().is2xxSuccessful());

	}

	@Test
	void updateTransformSameVersion() throws Exception {
		//// first insertion
		prepareCollection();

		// Update ETY

		MockMultipartFile newMap = new MockMultipartFile("maps", "map1.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartFile newStructureDefs = new MockMultipartFile("structureDefinitions", "structureDefinitions2.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartFile newValuesets = new MockMultipartFile("valueSets", "valueSet2.json",
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
						.file(newMap)
						.file(newStructureDefs)
						.file(newValuesets)
						.part(new MockPart("rootMapIdentifier", dto.getRootMapIdentifier().getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", dto.getVersion().getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().isConflict());
	}

	@Test
	void updateTransformNotFound() throws Exception {
		// Update ETY

		MockMultipartFile newMap = new MockMultipartFile("maps", "map2.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartFile newStructureDefs = new MockMultipartFile("structureDefinitions", "structureDefinitions2.json",
				MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());

		MockMultipartFile newValuesets = new MockMultipartFile("valueSets", "valueSet2.json",
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
						.file(newStructureDefs)
						.file(newValuesets)
						.part(new MockPart("rootMapIdentifier", "notFound".getBytes()))
						.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
						.part(new MockPart("version", dto.getVersion().getBytes()))
						.contentType(MediaType.MULTIPART_FORM_DATA)
				)
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteTransformNotFoundTest() throws Exception {
		mvc.perform(deleteTransformMockRequest(TEST_ID_ROOT_NOT_FOUND, getBaseUrl())).andExpectAll(
				status().isNotFound());
	}

	@Test
	void deleteTransformTest() throws Exception {
		prepareCollection();
		mvc.perform(deleteTransformMockRequest(TEST_ID_ROOT, getBaseUrl())).andExpectAll(
				status().is2xxSuccessful());
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

		mvc.perform(getTransformsMockRequest(getBaseUrl())).andExpectAll(
				status().is2xxSuccessful());
	}

	@Test
	void getZeroActiveTransformsTest() throws Exception {
		this.deleteTransformTest();
		MvcResult res = mvc.perform(queryActiveTransformMockRequest(getBaseUrl())).andReturn();

		List<TransformDTO> resultList = new Gson().fromJson(res.getResponse().getContentAsString(), new TypeToken<List<TransformDTO>>(){}.getType());
		assertTrue(CollectionUtils.isEmpty(resultList));
	}

	@Test
	void getActiveTransformsOneFoundTest() throws Exception {
		prepareCollection();
		MvcResult res = mvc.perform(queryActiveTransformMockRequest(getBaseUrl())).andReturn();
		List<TransformDTO> resultList = new Gson().fromJson(res.getResponse().getContentAsString(), new TypeToken<List<TransformDTO>>(){}.getType());
		assertEquals(1, resultList.size());
	}
}
