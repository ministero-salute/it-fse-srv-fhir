package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.deleteXslTransformMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getXslTransformByIdMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getXslTransformsMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.queryXslTransformMockRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl.XslTransformCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.XslTransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IXslTransformSRV;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(XslTransformCTL.class)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class XslTransformControllerTest extends AbstractTest {

    private final String TEST_NAME_XSLT = "Name_A"; 
    private final String TEST_ID_ROOT = "Root_A"; 
    private final String TEST_ID_VERSION = "Version_A"; 
    
    private final String TEST_ID_ROOT_INV = "Root_A_INV"; 
    private final String TEST_ID_VERSION_INV = "Version_A_INV"; 

    private final String TEST_ID_ROOT_NOT_FOUND = "Root_A_NF"; 
    private final String TEST_ID_VERSION_NOT_FOUND = "Version_A_NF"; 
    
	public final String TEST_JSON_XSLT = "{\"nameXslTransform\":\"Test_AB\",\"templateIdRoot\":\"Root_AB\", \"version\":\"Version_AB\"}"; 

	
	@Autowired
	private MockMvc mvc; 
	
	@MockBean
	private Tracer tracer; 
	
	@MockBean
	private IXslTransformSRV xslTransformService; 
	

	@Test
	void insertXslTransform() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper(); 
	    MockMultipartFile multipartFile = new MockMultipartFile("content_xsl_transform", "xslTransform_post.xml", MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes()); 
    	
    	XslTransformBodyDTO dto = new XslTransformBodyDTO(); 
    	dto.setTemplateIdRoot(TEST_ID_ROOT); 
		dto.setVersion(TEST_ID_VERSION);

	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart("http://127.0.0.1:9085/v1/xslt"); 
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setParameter("content_xsl_transform", "test");
	            try {
	            	request.setMethod("POST");
					request.setParameter("body", objectMapper.writeValueAsString(dto));
				} catch (JsonProcessingException e) {
					log.error("Error: ", e);
				}
	            return request;
	        }
	    }); 
	    
	    mvc.perform(builder
	            .file(new MockMultipartFile("content_xsl_transform", multipartFile.getBytes()))
	            .content(objectMapper.writeValueAsString(dto))
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(status().is2xxSuccessful()); 
	}
	
	
	@Test
	void insertXslTransformEmptyBody() throws Exception {
	    MockMultipartFile multipartFile = new MockMultipartFile("content_xsl_transform", "xslTransform_post.xml", MediaType.APPLICATION_JSON_VALUE, "".getBytes()); 

	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart("http://127.0.0.1:9085/v1/xslt"); 
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("POST");
	            return request;
	        }
	    }); 
	    
	    mvc.perform(builder
	            .file(multipartFile)
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(status().is2xxSuccessful()); 
	  
	} 
    
	
	@Test
	void updateXslTransform() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper(); 
	    MockMultipartFile multipartFile = new MockMultipartFile("content_xsl_transform", "xslTransform_post.xml", MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes()); 
    	
    	XslTransformBodyDTO dto = new XslTransformBodyDTO(); 
    	dto.setTemplateIdRoot(TEST_ID_ROOT); 
		dto.setVersion(TEST_ID_VERSION);

	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart("http://127.0.0.1:9085/v1/xslt"); 
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setParameter("content_xsl_transform", "test");
	            try {
	            	request.setMethod("PUT");
					request.setParameter("body", objectMapper.writeValueAsString(dto));
				} catch (JsonProcessingException e) {
					log.error("Error: ", e);
				}
	            return request;
	        }
	    }); 
	    
	    mvc.perform(builder
	            .file(new MockMultipartFile("content_xsl_transform", multipartFile.getBytes()))
	            .content(objectMapper.writeValueAsString(dto))
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(status().is2xxSuccessful()); 
	    
	} 
	
	@Test
	void updateXslTransformEmptyBody() throws Exception {
	    MockMultipartFile multipartFile = new MockMultipartFile("content_xsl_transform", "xslTransform.xml", MediaType.APPLICATION_JSON_VALUE, "".getBytes()); 

	    MockMultipartHttpServletRequestBuilder builder =
	            MockMvcRequestBuilders.multipart("http://127.0.0.1:9085/v1/xslt"); 
	    
	    builder.with(new RequestPostProcessor() {
	        @Override
	        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
	            request.setMethod("PUT");
	            return request;
	        }
	    }); 
	    
	    mvc.perform(builder
	            .file(multipartFile)
	            .contentType(MediaType.MULTIPART_FORM_DATA))
	            .andExpect(status().is2xxSuccessful()); 
	    
	} 
	
	 @Test
	void deleteXslTransformNotFoundTest() throws Exception {
		
		mvc.perform(deleteXslTransformMockRequest(TEST_ID_ROOT_NOT_FOUND, TEST_ID_VERSION_NOT_FOUND)).andExpectAll(
	            status().is4xxClientError()
	        ); 
	}  
	
	@Test
	void findXslTransformByIdRootAndExtensionTest() throws Exception {
		List<XslTransformDTO> dtoList = new ArrayList<XslTransformDTO>(); 
		XslTransformDTO dto = new XslTransformDTO(); 
		
		dto.setNameXslTransform(TEST_NAME_XSLT); 
		dto.setTemplateIdRoot(TEST_ID_ROOT); 
		dto.setInsertionDate(new Date()); 
		dto.setLastUpdate(new Date()); 
		dtoList.add(dto); 
		
		xslTransformService.insertAll(dtoList); 
		
		
		when(xslTransformService.findByTemplateIdRootAndVersion(TEST_ID_ROOT, TEST_ID_VERSION))
			.thenReturn(dto); 
	
		mvc.perform(queryXslTransformMockRequest(TEST_ID_ROOT, TEST_ID_VERSION)).andExpectAll(
	            status().is2xxSuccessful()
	        );
	} 
	
	@Test
	void findXslTransformByIdRootAndExtensionInvalidRootTest() throws Exception {
		List<XslTransformDTO> dtoList = new ArrayList<XslTransformDTO>(); 
		XslTransformDTO dto = new XslTransformDTO(); 
		
		dto.setNameXslTransform(TEST_NAME_XSLT); 
		dto.setTemplateIdRoot(TEST_ID_ROOT); 
		dto.setInsertionDate(new Date()); 
		dto.setLastUpdate(new Date()); 
		dtoList.add(dto); 
		
		xslTransformService.insertAll(dtoList); 
		
		
		when(xslTransformService.findByTemplateIdRootAndVersion(TEST_ID_ROOT_INV, TEST_ID_VERSION))
			.thenReturn(new XslTransformDTO()); 
	
		mvc.perform(queryXslTransformMockRequest(TEST_ID_ROOT, TEST_ID_VERSION)).andExpectAll(
	            status().is(200)
	        );
	}   
	
	@Test
 	void findXslTransformByIdDocumentNotFoundTest() throws Exception {

		when(xslTransformService.findById("690000000000000000000000"))
			.thenThrow(new DocumentNotFoundException("Document Not Found")); 
	
		mvc.perform(getXslTransformByIdMockRequest("690000000000000000000000")).andExpect(
	            status().is4xxClientError()
	        );
	}  
	
	@Test
	void findXslTransformByIdRootAndVersionTest() throws Exception {
		List<XslTransformDTO> dtoList = new ArrayList<XslTransformDTO>(); 
		XslTransformDTO dto = new XslTransformDTO(); 
		
		dto.setNameXslTransform(TEST_NAME_XSLT); 
		dto.setTemplateIdRoot(TEST_ID_ROOT); 
		dto.setInsertionDate(new Date()); 
		dto.setLastUpdate(new Date()); 
		dtoList.add(dto); 
		
		xslTransformService.insertAll(dtoList); 
	
		when(xslTransformService.findByTemplateIdRootAndVersion(TEST_ID_ROOT, TEST_ID_VERSION_INV))
			.thenReturn(new XslTransformDTO()); 
	
		mvc.perform(queryXslTransformMockRequest(TEST_ID_ROOT, TEST_ID_VERSION_INV)).andExpect(
	            status().is(200)
	        );
	} 
	
	@Test
	void getXslTransformsTest() throws Exception {
		when(xslTransformService.findAll())
			.thenReturn(new ArrayList<>()); 
		
		mvc.perform(getXslTransformsMockRequest()).andExpectAll(
	            status().is2xxSuccessful()
	        );
	} 
	
	@Test
	void getXslTransformJsonObject() {
		XslTransformBodyDTO dto = XslTransformCTL.getXsltJSONObject(TEST_JSON_XSLT); 
		
		assertEquals(XslTransformBodyDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getTemplateIdRoot().getClass()); 
		assertEquals(String.class, dto.getVersion().getClass()); 
		
		assertEquals("Root_AB", dto.getTemplateIdRoot()); 
		assertEquals("Version_AB", dto.getVersion()); 

	}
	
   
	
}
