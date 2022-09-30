package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.deleteDefinitionByNameMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getDefinitionByIdMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getDefinitionByNameMockRequest;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.DefinitionETY;


import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ComponentScan
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
@TestMethodOrder(MethodOrderer.MethodName.class)
class DefinitionControllerTest extends AbstractTest {

	@MockBean
	private Tracer tracer;

	@Autowired
	private MockMvc mvc;

	@BeforeAll
	void setup() {
		mongo.dropCollection(DefinitionETY.class);
		dataPreparation();
	}

	@Test
	void getDefinitionByNameTest() throws Exception {
		mvc.perform(getDefinitionByNameMockRequest(DEFINITION_TEST_NAME_A, getBaseUrl()))
			.andExpectAll(
				status().is2xxSuccessful(),
				content().contentType(APPLICATION_JSON_VALUE)
			);
	}

	@Test
	void getDefinitionByIdTest() throws Exception {
		mvc.perform(getDefinitionByIdMockRequest(DEFINITION_TEST_ID_A, getBaseUrl()))
			.andExpectAll(
				status().is2xxSuccessful(),
				content().contentType(APPLICATION_JSON_VALUE)
			);
		
	}

	@Test
	void insertDefinitionTest() throws Exception {
		
	    MockMultipartFile multipartFile = new MockMultipartFile(DEFINITION_TEST_NAME_C, DEFINITION_TEST_NAME_C, MediaType.APPLICATION_JSON_VALUE, FILE_TEST_STRING.getBytes()); 
	    
	    mvc.perform(MockMvcRequestBuilders.multipart(getBaseUrl() + "/definition")
	            .file(new MockMultipartFile("file", multipartFile.getBytes()))
				.param("name", DEFINITION_TEST_NAME_C)
				.param("version", DEFINITION_TEST_VERSION_C)
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	        .andExpect(MockMvcResultMatchers.status().isOk()); 
		
	} 

	@Test
	void updateDefinitionTest() throws Exception {

	    MockMultipartFile multipartFile = new MockMultipartFile(DEFINITION_TEST_NAME_A, DEFINITION_TEST_NAME_A, MediaType.APPLICATION_JSON_VALUE, FILE_TEST_STRING_UPDATED.getBytes()); 
	    
	    
	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart(getBaseUrl() + "/definition/" + DEFINITION_TEST_NAME_A); 
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
	            return request;
	        }
	    }); 
	    
	    mvc.perform(builder
	            .file(new MockMultipartFile("file", multipartFile.getBytes()))
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	        .andExpect(status().is2xxSuccessful()); 
	    
	} 

	@Test
	void deleteDefinitionTest() throws Exception {

		mvc.perform(deleteDefinitionByNameMockRequest(DEFINITION_TEST_NAME_B, getBaseUrl()))
			.andExpectAll(
				status().is2xxSuccessful(),
				content().contentType(APPLICATION_JSON_VALUE)
			);

		

	}








	void dataPreparation() {
		DefinitionETY definitionA = new DefinitionETY();
		definitionA.setId(DEFINITION_TEST_ID_A);
		definitionA.setNameDefinition(DEFINITION_TEST_NAME_A);
		definitionA.setFilenameDefinition(DEFINITION_TEST_FILENAME_A);
		definitionA.setVersionDefinition(DEFINITION_TEST_VERSION_A);
		definitionA.setContentDefinition(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
		definitionA.setInsertionDate(new Date());

		DefinitionETY definitionB = new DefinitionETY();
		definitionB.setId(DEFINITION_TEST_ID_B);
		definitionB.setNameDefinition(DEFINITION_TEST_NAME_B);
		definitionB.setFilenameDefinition(DEFINITION_TEST_FILENAME_B);
		definitionB.setVersionDefinition(DEFINITION_TEST_VERSION_B);
		definitionB.setContentDefinition(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
		definitionB.setInsertionDate(new Date());

		mongo.insert(definitionA);
		mongo.insert(definitionB);


	}

}
