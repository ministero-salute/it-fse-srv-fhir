package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.specs.XSLTransformCS;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDocumentDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.GetDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class EntityDtoTest extends AbstractTest {


    private final String TEST_XSLT_ID = "as63hFd3aM"; 
    private final String TEST_XSLT_NAME = "NAME_XSLT"; 
    private final String TEST_XSLT_ROOT = "ROOT_XSLT"; 
    private final String TEST_XSLT_VERSION = "VERSION_XSLT"; 
    private final Binary TEST_XSLT_CONTENT = new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes());  

    
    // Test Cases 
    @Test
    void createXslTransformDto() {
    	XslTransformDTO xslTransform = new XslTransformDTO(); 
    	Date dateNow = new Date(); 
    	
    	xslTransform.setNameXslTransform(TEST_XSLT_NAME); 
    	xslTransform.setContentXslTransform(TEST_XSLT_CONTENT); 
    	xslTransform.setTemplateIdRoot(TEST_XSLT_ROOT);
		xslTransform.setVersion(TEST_XSLT_VERSION); 
    	xslTransform.setInsertionDate(dateNow); 
    	xslTransform.setLastUpdate(dateNow); 
    	
    	
    	assertEquals(xslTransform.getClass(), XslTransformDTO.class); 
    	
    	assertEquals(String.class, xslTransform.getNameXslTransform().getClass()); 
    	assertEquals(Binary.class, xslTransform.getContentXslTransform().getClass()); 
    	assertEquals(String.class, xslTransform.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, xslTransform.getVersion().getClass()); 
    	assertEquals(Date.class, xslTransform.getInsertionDate().getClass()); 
    	assertEquals(Date.class, xslTransform.getLastUpdate().getClass()); 
    	
    	assertEquals(TEST_XSLT_NAME, xslTransform.getNameXslTransform()); 
    	assertEquals(TEST_XSLT_CONTENT, xslTransform.getContentXslTransform()); 
    	assertEquals(TEST_XSLT_ROOT, xslTransform.getTemplateIdRoot()); 
    	assertEquals(TEST_XSLT_VERSION, xslTransform.getVersion()); 
    	assertEquals(dateNow, xslTransform.getInsertionDate()); 
    	assertEquals(dateNow, xslTransform.getLastUpdate()); 


    	
    } 
    
    @Test
    void createXslTransformEty() {
    	XslTransformETY xslTransform = new XslTransformETY(); 
    	Date dateNow = new Date(); 
    	
    	xslTransform.setId(TEST_XSLT_ID);
    	xslTransform.setNameXslTransform(TEST_XSLT_NAME); 
    	xslTransform.setContentXslTransform(TEST_XSLT_CONTENT); 
    	xslTransform.setTemplateIdRoot(TEST_XSLT_ROOT); 
		xslTransform.setVersion(TEST_XSLT_VERSION);
    	xslTransform.setInsertionDate(dateNow); 
    	xslTransform.setLastUpdateDate(dateNow); 
    	
    	assertEquals(XslTransformETY.class, xslTransform.getClass()); 
    	assertEquals(String.class, xslTransform.getId().getClass()); 
    	assertEquals(String.class, xslTransform.getNameXslTransform().getClass()); 
    	assertEquals(Binary.class, xslTransform.getContentXslTransform().getClass()); 
    	assertEquals(String.class, xslTransform.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, xslTransform.getVersion().getClass()); 
    	assertEquals(Date.class, xslTransform.getInsertionDate().getClass()); 
    	assertEquals(Date.class, xslTransform.getLastUpdateDate().getClass()); 
    	
    	assertEquals(TEST_XSLT_ID, xslTransform.getId()); 
    	assertEquals(TEST_XSLT_NAME, xslTransform.getNameXslTransform()); 
    	assertEquals(TEST_XSLT_CONTENT, xslTransform.getContentXslTransform()); 
    	assertEquals(TEST_XSLT_ROOT, xslTransform.getTemplateIdRoot()); 
    	assertEquals(TEST_XSLT_VERSION, xslTransform.getVersion()); 
    	assertEquals(dateNow, xslTransform.getInsertionDate()); 
    	assertEquals(dateNow, xslTransform.getLastUpdateDate()); 

    	
    } 
    
    
    @Test
    void createChangeSetDto() {
    	ChangeSetDTO<XSLTransformCS> changeset = new ChangeSetDTO<>();
    	
    	changeset.setId("id"); 
		changeset.setDescription(new XSLTransformCS("templateIdRoot", "version"));

    	assertEquals(String.class, changeset.getId().getClass()); 
    	assertEquals(XSLTransformCS.class, changeset.getDescription().getClass());
    	
    	assertEquals("id", changeset.getId()); 
    	assertEquals("templateIdRoot", changeset.getDescription().getTemplateIdRoot());
		assertEquals("version", changeset.getDescription().getVersion()); 
    	
    } 
    
    
    @Test
    void createGetDocumentResDto() {
    	LogTraceInfoDTO logInfo = new LogTraceInfoDTO("testSpanId", "testTraceId"); 
    	
    	XslTransformDocumentDTO xslTransformDto = new XslTransformDocumentDTO("testId", "testIdRoot", "testName", "testContent", "testVersion", null); 


    	GetDocumentResDTO getDocumentResDto = new GetDocumentResDTO(logInfo, xslTransformDto); 
    	

    	assertEquals(String.class, getDocumentResDto.getTraceID().getClass()); 
    	assertEquals(String.class, getDocumentResDto.getSpanID().getClass()); 
    	assertEquals(XslTransformDocumentDTO.class, getDocumentResDto.getDocument().getClass()); 
    	
    	assertEquals("testTraceId", getDocumentResDto.getTraceID()); 
    	assertEquals("testSpanId", getDocumentResDto.getSpanID()); 
		assertEquals(xslTransformDto, getDocumentResDto.getDocument()); 
    	
    } 
    

    	
 }


