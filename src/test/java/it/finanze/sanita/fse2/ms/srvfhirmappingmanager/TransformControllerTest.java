/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.createTransform;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.deleteTransformByUri;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getLiveness;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getTransformByIdMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getTransformByUri;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.updateTransform;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.API_PATH_FILE_VAR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.mongodb.MongoException;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class TransformControllerTest extends AbstractTest {
	
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
	void updateTransformTest() throws Exception {
		// first insertion
		prepareCollection();
		// Provide knowledge
		TransformETY e = createTestEntity();
		e.setVersion(MOCK_NEW_VERSION_ETY);
		e.setContent(MOCK_FILE_NEW_ETY);
		
		mvc.perform(updateTransform(e)).andExpect(status().is2xxSuccessful());
	}

	@Test
	void updateTransformSameVersion() throws Exception {
		// First insertion
		prepareCollection();
		// Provide knowledge
		TransformETY e = createTestEntity();
		e.setContent(MOCK_FILE_NEW_ETY);
		// if inserting transform with same version, will throw a 409 error
		mvc.perform(updateTransform(e)).andExpect(status().isConflict());
	}

	@Test
	void updateTransformNotFound() throws Exception {
		// Provide knowledge
		TransformETY e = createTestEntity();
		mvc.perform(updateTransform(e)).andExpect(status().isNotFound());
	}

	@Test
	void deleteTransformNotFoundTest() throws Exception {
		mvc.perform(deleteTransformByUri(MOCK_URI_ETY)).andExpect(status().isNotFound());
	}

	@Test
	void deleteTransformTest() throws Exception {
		// First insertion
		prepareCollection();
		mvc.perform(deleteTransformByUri(MOCK_URI_ETY)).andExpect(status().is2xxSuccessful());
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
