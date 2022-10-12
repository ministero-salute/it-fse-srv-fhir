package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
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

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.XslTransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.GetXsltResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IXslTransformSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.JsonUtility;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ComponentScan(basePackages = { Constants.ComponentScan.BASE })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class XslTransformControllerTest extends AbstractTest {

	private final String TEST_NAME_XSLT = "Name_A";
	private final String TEST_ID_ROOT = "Root_A";
	private final String TEST_ID_VERSION = "1.0";

	private final String TEST_ID_ROOT_NOT_FOUND = "Root_A_NF";
	private final String TEST_ID_VERSION_NOT_FOUND = "Version_A_NF";

	public final String TEST_JSON_XSLT = "{\"nameXslTransform\":\"Test_AB\",\"templateIdRoot\":\"Root_AB\", \"version\":\"Version_AB\"}";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private Tracer tracer;

	@SpyBean
	private IXslTransformSRV xslTransformService;

	@BeforeEach
	void setup() {
		mongo.dropCollection(XslTransformETY.class);
	}

	@Test
	void insertXslTransform() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "xslTransform_post.xsl",
				MediaType.APPLICATION_JSON_VALUE, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet/>".getBytes());

		XslTransformBodyDTO dto = new XslTransformBodyDTO();
		dto.setTemplateIdRoot(TEST_ID_ROOT);
		dto.setVersion(TEST_ID_VERSION);

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(getBaseUrl() + "/xslt");

		builder.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("POST");
				return request;
			}
		});
		
		mvc.perform(builder
				.file(multipartFile)
				.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
				.part(new MockPart("version", dto.getVersion().getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	void insertXslTransformEmptyBody() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "xslTransform_post.xsl",
				MediaType.APPLICATION_JSON_VALUE, "".getBytes());

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(getBaseUrl() + "/xslt");

		builder.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("POST");
				return request;
			}
		});

		XslTransformBodyDTO dto = new XslTransformBodyDTO();
		dto.setTemplateIdRoot(TEST_ID_ROOT);
		dto.setVersion(TEST_ID_VERSION);

		mvc.perform(builder
				.file(multipartFile)
				.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
				.part(new MockPart("version", dto.getVersion().getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateFileNotFoundTest() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "xslTransform_post.xsl",
				MediaType.APPLICATION_JSON_VALUE, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet/>".getBytes());

		XslTransformBodyDTO dto = new XslTransformBodyDTO();
		dto.setTemplateIdRoot(TEST_ID_ROOT);
		dto.setVersion(TEST_ID_VERSION);

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(getBaseUrl() + "/xslt");

		builder.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setParameter("file", "test");
				request.setMethod("PUT");
				return request;
			}
		});
		mvc.perform(builder
				.file(multipartFile)
				.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
				.part(new MockPart("version", dto.getVersion().getBytes())).contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateSameVersionConflict() throws Exception {

		MockMultipartFile multipartFile = new MockMultipartFile("file", "xslTransform_post.xsl",
			MediaType.APPLICATION_JSON_VALUE, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet/>".getBytes());

		XslTransformBodyDTO dto = new XslTransformBodyDTO();
		dto.setTemplateIdRoot(TEST_ID_ROOT);
		dto.setVersion(TEST_ID_VERSION);

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(getBaseUrl() + "/xslt");

		builder.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("POST");
				return request;
			}
		});

		mvc.perform(builder
				.file(multipartFile)
				.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
				.part(new MockPart("version", dto.getVersion().getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().is2xxSuccessful());

		builder = MockMvcRequestBuilders.multipart(getBaseUrl() + "/xslt");

		builder.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
				return request;
			}
		});
		mvc.perform(builder
				.file(multipartFile)
				.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
				.part(new MockPart("version", dto.getVersion().getBytes())).contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isConflict());
	}

	@Test
	void testComparison () {
		assertTrue(ValidationUtility.isMajorVersion("2.0", "1.0"));
		assertFalse(ValidationUtility.isMajorVersion("1.0", "2.0"));
		assertTrue(ValidationUtility.isMajorVersion("1.1", "1.0"));
		assertTrue(ValidationUtility.isMajorVersion("0.8", "0.7"));
		assertFalse(ValidationUtility.isMajorVersion("0.7", "0.8"));
	}


	@Test
	void updateHappyPath() throws Exception {

		MockMultipartFile multipartFile = new MockMultipartFile("file", "xslTransform_post.xsl",
			MediaType.APPLICATION_JSON_VALUE, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xsl:stylesheet/>".getBytes());

		XslTransformBodyDTO dto = new XslTransformBodyDTO();
		dto.setTemplateIdRoot(TEST_ID_ROOT);
		dto.setVersion(TEST_ID_VERSION);

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(getBaseUrl() + "/xslt");

		builder.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("POST");
				return request;
			}
		});

		mvc.perform(builder
				.file(multipartFile)
				.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
				.part(new MockPart("version", dto.getVersion().getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().is2xxSuccessful());

		builder = MockMvcRequestBuilders.multipart(getBaseUrl() + "/xslt");

		builder.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
				return request;
			}
		});
		dto.setVersion("2.0");
		mvc.perform(builder
				.file(multipartFile)
				.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
				.part(new MockPart("version", dto.getVersion().getBytes())).contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isOk());
	}

	@Test
	void updateXslTransformEmptyBody() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "xslTransform_post.xsl",
			MediaType.APPLICATION_JSON_VALUE, "".getBytes());

		MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(getBaseUrl() + "/xslt");

		XslTransformBodyDTO dto = new XslTransformBodyDTO();
		dto.setTemplateIdRoot(TEST_ID_ROOT);
		dto.setVersion(TEST_ID_VERSION);

		builder.with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
				return request;
			}
		});

		mvc.perform(builder
				.file(multipartFile)
				.part(new MockPart("templateIdRoot", dto.getTemplateIdRoot().getBytes()))
				.part(new MockPart("version", dto.getVersion().getBytes()))
				.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isBadRequest());
	}

	@Test
	void deleteXslTransformNotFoundTest() throws Exception {

		mvc.perform(MockRequests.deleteXslTransformMockRequest(TEST_ID_ROOT_NOT_FOUND, TEST_ID_VERSION_NOT_FOUND)).andExpectAll(
				status().isNotFound());
	}

	@Test
	void findXslTransformByIdRootAndExtensionTest() throws Exception {
		List<XslTransformDTO> dtoList = new ArrayList<XslTransformDTO>();
		XslTransformDTO dto = new XslTransformDTO();

		dto.setNameXslTransform(TEST_NAME_XSLT);
		dto.setTemplateIdRoot(TEST_ID_ROOT);
		dto.setVersion(TEST_ID_VERSION);
		dto.setInsertionDate(new Date());
		dto.setLastUpdate(new Date());
		dtoList.add(dto);
		
		xslTransformService.insertAll(dtoList);

		mvc.perform(MockRequests.queryXslTransformMockRequest(TEST_ID_ROOT, TEST_ID_VERSION)).andExpectAll(
				status().isOk());
	}

	@Test
	void findXslTransformByIdRootAndExtensionInvalidRootTest() throws Exception {
		List<XslTransformDTO> dtoList = new ArrayList<XslTransformDTO>();
		XslTransformDTO dto = new XslTransformDTO();

		dto.setNameXslTransform(TEST_NAME_XSLT);
		dto.setTemplateIdRoot(TEST_ID_ROOT);
		dto.setVersion(TEST_ID_VERSION);
		dto.setInsertionDate(new Date());
		dto.setLastUpdate(new Date());
		dtoList.add(dto);

		xslTransformService.insertAll(dtoList);

		mvc.perform(MockRequests.queryXslTransformMockRequest(TEST_ID_ROOT, TEST_ID_VERSION)).andExpect(
			status().isOk());
	}

	@Test
	void findXslTransformByIdDocumentNotFoundTest() throws Exception {
		mvc.perform(MockRequests.getXslTransformByIdMockRequest("690000000000000000000001")).andExpect(
				status().is4xxClientError());
	}

	@Test
	void findXslTransformByIdRootAndVersionTest() throws Exception {
		List<XslTransformDTO> dtoList = new ArrayList<XslTransformDTO>();
		XslTransformDTO dto = new XslTransformDTO();

		dto.setNameXslTransform(TEST_NAME_XSLT);
		dto.setTemplateIdRoot(TEST_ID_ROOT);
		dto.setVersion(TEST_ID_VERSION);
		dto.setInsertionDate(new Date());
		dto.setLastUpdate(new Date());
		dtoList.add(dto);

		xslTransformService.insertAll(dtoList);

		mvc.perform(MockRequests.queryXslTransformMockRequest(TEST_ID_ROOT, TEST_ID_VERSION)).andExpect(
				status().is(200));
	}

	@Test
	void getXslTransformsTest() throws Exception {
		List<XslTransformDTO> dtoList = new ArrayList<XslTransformDTO>();
		XslTransformDTO dto = new XslTransformDTO();

		dto.setNameXslTransform(TEST_NAME_XSLT);
		dto.setTemplateIdRoot(TEST_ID_ROOT);
		dto.setVersion(TEST_ID_VERSION);
		dto.setInsertionDate(new Date());
		dto.setLastUpdate(new Date());
		dto.setContentXslTransform(new Binary("test".getBytes()));
		dtoList.add(dto);

		xslTransformService.insertAll(dtoList);

		MvcResult result = mvc.perform(MockRequests.getXslTransformsMockRequest()).andExpect(status().isOk()).andReturn();
		GetXsltResponseDTO output = JsonUtility.toJsonObject(result.getResponse().getContentAsString(), GetXsltResponseDTO.class);
		assertEquals(1, output.getBody().size());
	}

}
