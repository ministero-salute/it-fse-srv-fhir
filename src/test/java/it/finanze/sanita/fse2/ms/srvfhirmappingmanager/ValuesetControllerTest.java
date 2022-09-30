package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.deleteValuesetByNameMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getValuesetByNameMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getValuesetByIdMockRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.ValuesetETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IValuesetSRV;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
public class ValuesetControllerTest extends AbstractTest {

	@MockBean
    private IValuesetSRV service;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Tracer tracer;

    @BeforeAll
    void setup(){
        mongo.dropCollection(ValuesetETY.class);
        dataPreparation();
    }
	
    @Test
	void getValuesetByNameTest() throws Exception {

        mvc.perform(getValuesetByNameMockRequest(VALUESET_TEST_NAME_A, getBaseUrl()))
			.andExpectAll(
				status().is2xxSuccessful(),
				content().contentType(APPLICATION_JSON_VALUE)
			);
    }

	@Test
	void getValuesetByIdTest() throws Exception {
		mvc.perform(getValuesetByIdMockRequest(VALUESET_TEST_ID_A, getBaseUrl()))
			.andExpectAll(
				status().is2xxSuccessful(),
				content().contentType(APPLICATION_JSON_VALUE)
			);
		
	}

	@Test
	void insertValuesetTest() throws Exception {
		
	    MockMultipartFile multipartFile = new MockMultipartFile(VALUESET_TEST_NAME_C, VALUESET_TEST_NAME_C, MediaType.APPLICATION_JSON_VALUE, FILE_TEST_STRING.getBytes()); 
	    
	    mvc.perform(MockMvcRequestBuilders.multipart(getBaseUrl() + "/valueset")
	            .file(new MockMultipartFile("file", multipartFile.getBytes()))
				.param("name", VALUESET_TEST_NAME_C)
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	        .andExpect(MockMvcResultMatchers.status().isOk()); 
		
	} 

	@Test
	void updateValuesetTest() throws Exception {

	    MockMultipartFile multipartFile = new MockMultipartFile(VALUESET_TEST_NAME_A, VALUESET_TEST_NAME_A, MediaType.APPLICATION_JSON_VALUE, FILE_TEST_STRING_UPDATED.getBytes()); 
	    when(
                service.updateDocByName(anyString(), any())).thenReturn("");
	    
	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart(getBaseUrl() + "/valueset/" + VALUESET_TEST_NAME_A); 
	    
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
	void deleteValuesetTest() throws Exception {

		mvc.perform(deleteValuesetByNameMockRequest(VALUESET_TEST_NAME_B, getBaseUrl()))
			.andExpectAll(
				status().is2xxSuccessful(),
				content().contentType(APPLICATION_JSON_VALUE)
			);
	}

    void dataPreparation() {
		ValuesetETY valuesetA = new ValuesetETY();
		valuesetA.setId(VALUESET_TEST_ID_A);
		valuesetA.setNameValueset(VALUESET_TEST_NAME_A);
		valuesetA.setFilenameValueset(VALUESET_TEST_FILENAME_A);
		valuesetA.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
		valuesetA.setInsertionDate(new Date());
		valuesetA.setLastUpdateDate(new Date());

		ValuesetETY valuesetB = new ValuesetETY();
		valuesetB.setId(VALUESET_TEST_ID_B);
		valuesetB.setNameValueset(VALUESET_TEST_NAME_B);
		valuesetB.setFilenameValueset(VALUESET_TEST_FILENAME_B);
		valuesetB.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
		valuesetB.setInsertionDate(new Date());
		valuesetB.setLastUpdateDate(new Date());

		mongo.insert(valuesetA);
		mongo.insert(valuesetB);


	}
}
