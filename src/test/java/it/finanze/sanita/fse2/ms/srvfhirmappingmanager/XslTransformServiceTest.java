package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDocumentDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl.XsltransformSRV; 


@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class XslTransformServiceTest extends AbstractTest {
	
    // Test Data 
    private final String TEST_XSLT_ID = "sd7awksdda"; 
    private final String TEST_XSLT_NAME = "testName"; 
    private final String TEST_XSLT_ROOT = "testRoot"; 
	private final String TEST_XSLT_VERSION = "testVersion";
    private final Binary TEST_XSLT_CONTENT = new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()); 

    private final String TEST_XSLT_NAME_INS = "testNameIns"; 
    private final String TEST_XSLT_ROOT_INS = "testRootIns"; 
    private final String TEST_XSLT_VERSION_INS = "testVersionIns"; 
    private final Binary TEST_XSLT_CONTENT_INS = new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()); 
    
    private final String TEST_XSLT_NAME_INS_THROW = "testNameInsThrow"; 
    private final String TEST_XSLT_ROOT_INS_THROW = "testRootInsThrow"; 
    private final Binary TEST_XSLT_CONTENT_INS_THROW = new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()); 
    
    private final String TEST_XSLT_NAME_INS_1 = "testNameIns_1"; 
    private final String TEST_XSLT_ROOT_INS_1 = "testRootIns_1"; 
    private final String TEST_XSLT_VERSION_INS_1 = "testVersionIns_1"; 
    private final Binary TEST_XSLT_CONTENT_INS_1 = new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()); 
    private final String TEST_XSLT_NAME_INS_2 = "testNameIns_2"; 
    private final String TEST_XSLT_ROOT_INS_2 = "testRootIns_2"; 
    private final String TEST_XSLT_VERSION_INS_2 = "testVersionIns_2"; 
    private final Binary TEST_XSLT_CONTENT_INS_2 = new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()); 
    private final String TEST_XSLT_NAME_INS_3 = "testNameIns_3"; 
    private final String TEST_XSLT_ROOT_INS_3 = "testRootIns_3"; 
	private final String TEST_XSLT_VERSION_INS_3 = "testVersionIns_3";
    private final Binary TEST_XSLT_CONTENT_INS_3 = new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()); 
    
    private final String TEST_XSLT_NAME_DEL = "testNameToDel"; 
    private final String TEST_XSLT_ROOT_DEL = "testRootDel"; 
    private final String TEST_XSLT_VERSION_DEL = "testVersionDel"; 
    private final Binary TEST_XSLT_CONTENT_DEL= new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()); 
    
    private final String TEST_XSLT_NAME_QUERY = "testNameQuery"; 
    private final String TEST_XSLT_ROOT_QUERY = "testRootQuery"; 
    private final String TEST_XSLT_VERSION_QUERY = "testVersionQuery"; 
    private final Binary TEST_XSLT_CONTENT_QUERY = new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()); 
    
    private final String TEST_XSLT_NAME_QUERY_DEL = "testNameQueryDel"; 
    private final String TEST_XSLT_ROOT_QUERY_DEL = "testRootQueryDel"; 
    private final String TEST_XSLT_VERSION_QUERY_DEL = "testVersionQueryDel"; 
    private final Binary TEST_XSLT_CONTENT_QUERY_DEL = new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()); 
    
    @Autowired 
    XsltransformSRV xslTransformService; 
    

    
    @BeforeAll
    public void setup() throws Exception {
		this.dropTestSchema();
        this.initTestRepository();
    } 
    
    
    @Test
    void insertTest() throws Exception{
    	XslTransformDTO dto = new XslTransformDTO(); 
    	
    	Date date = new Date(); 
    	
    	dto.setNameXslTransform(TEST_XSLT_NAME_INS); 
    	dto.setTemplateIdRoot(TEST_XSLT_ROOT_INS); 
    	dto.setContentXslTransform(TEST_XSLT_CONTENT_INS); 
		dto.setVersion(TEST_XSLT_VERSION_INS);
    	dto.setInsertionDate(date); 
    	dto.setLastUpdate(date); 

    	xslTransformService.insert(dto); 
    	
    	XslTransformDTO returnedDto = xslTransformService.findByTemplateIdRootAndVersion(TEST_XSLT_ROOT_INS, TEST_XSLT_VERSION_INS); 
    	
    	assertEquals(XslTransformDTO.class, returnedDto.getClass()); 
    	assertEquals(Binary.class, returnedDto.getContentXslTransform().getClass()); 
    	assertEquals(String.class, returnedDto.getNameXslTransform().getClass()); 
    	assertEquals(String.class, returnedDto.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, returnedDto.getVersion().getClass()); 
    	assertEquals(Date.class, returnedDto.getInsertionDate().getClass()); 
    	assertEquals(Date.class, returnedDto.getLastUpdate().getClass()); 
    	
    	assertEquals(TEST_XSLT_NAME_INS, returnedDto.getNameXslTransform()); 
    	assertEquals(TEST_XSLT_ROOT_INS, returnedDto.getTemplateIdRoot()); 
    	assertEquals(TEST_XSLT_VERSION_INS, returnedDto.getVersion()); 
    	assertEquals(TEST_XSLT_CONTENT_INS, returnedDto.getContentXslTransform()); 
    	assertEquals(date, returnedDto.getInsertionDate()); 
    	assertEquals(date, returnedDto.getLastUpdate()); 

        // INSERT SAME DOCUMENT
    	assertThrows(DocumentAlreadyPresentException.class, () -> xslTransformService.insert(dto));
    	 	
    } 
    
    @Test
    void insertXslTransformIfPresent() throws Exception {
    	XslTransformDTO dto = new XslTransformDTO(); 
    	
    	Date date = new Date(); 
    	
    	dto.setNameXslTransform(TEST_XSLT_NAME_INS_THROW); 
    	dto.setTemplateIdRoot(TEST_XSLT_ROOT_INS_THROW); 
    	dto.setContentXslTransform(TEST_XSLT_CONTENT_INS_THROW); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdate(date); 

    	xslTransformService.insert(dto); 
    	
    	XslTransformDTO dtoNew = new XslTransformDTO(); 
    	
    	
    	dtoNew.setNameXslTransform(TEST_XSLT_NAME_INS_THROW); 
    	dtoNew.setTemplateIdRoot(TEST_XSLT_ROOT_INS_THROW); 
    	dtoNew.setContentXslTransform(TEST_XSLT_CONTENT_INS_THROW); 
    	dtoNew.setInsertionDate(date); 
    	dtoNew.setLastUpdate(date); 
    	
    	 	
    	assertThrows(Exception.class, () -> {
    		xslTransformService.insert(dtoNew); 
    	}); 

    }
    
    @Test
    void insertManyTest() throws Exception {
    	List<XslTransformDTO> dtoList = new ArrayList<XslTransformDTO>(); 
    	XslTransformDTO dtoFirst = new XslTransformDTO(); 
    	XslTransformDTO dtoSecond = new XslTransformDTO(); 
    	
    	Date date = new Date(); 
    	
    	dtoFirst.setNameXslTransform(TEST_XSLT_NAME_INS_1); 
    	dtoFirst.setTemplateIdRoot(TEST_XSLT_ROOT_INS_1); 
    	dtoFirst.setContentXslTransform(TEST_XSLT_CONTENT_INS_1); 
		dtoFirst.setVersion(TEST_XSLT_VERSION_INS_1);
    	dtoFirst.setInsertionDate(date); 
    	dtoFirst.setLastUpdate(date); 
    	
    	dtoSecond.setNameXslTransform(TEST_XSLT_NAME_INS_2); 
    	dtoSecond.setTemplateIdRoot(TEST_XSLT_ROOT_INS_2); 
		dtoSecond.setVersion(TEST_XSLT_VERSION_INS_2);
    	dtoSecond.setContentXslTransform(TEST_XSLT_CONTENT_INS_2); 
    	dtoSecond.setInsertionDate(date); 
    	dtoSecond.setLastUpdate(date); 
    	
    	dtoList.add(dtoFirst); 
    	dtoList.add(dtoSecond); 
    	
    	xslTransformService.insertAll(dtoList); 
    	
    	
    	XslTransformDTO returnedDtoFirst = xslTransformService.findByTemplateIdRootAndVersion(TEST_XSLT_ROOT_INS_1, TEST_XSLT_VERSION_INS_1); 
    	XslTransformDTO returnedDtoSecond = xslTransformService.findByTemplateIdRootAndVersion(TEST_XSLT_ROOT_INS_2, TEST_XSLT_VERSION_INS_2); 

    	assertEquals(XslTransformDTO.class, returnedDtoFirst.getClass()); 
    	assertEquals(Binary.class, returnedDtoFirst.getContentXslTransform().getClass()); 
    	assertEquals(String.class, returnedDtoFirst.getNameXslTransform().getClass()); 
    	assertEquals(String.class, returnedDtoFirst.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, returnedDtoFirst.getVersion().getClass()); 
    	assertEquals(XslTransformDTO.class, returnedDtoSecond.getClass()); 
    	assertEquals(Binary.class, returnedDtoSecond.getContentXslTransform().getClass()); 
    	assertEquals(String.class, returnedDtoSecond.getNameXslTransform().getClass()); 
    	assertEquals(String.class, returnedDtoSecond.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, returnedDtoSecond.getVersion().getClass()); 
    	
    	assertEquals(TEST_XSLT_NAME_INS_1, returnedDtoFirst.getNameXslTransform()); 
    	assertEquals(TEST_XSLT_ROOT_INS_1, returnedDtoFirst.getTemplateIdRoot()); 
    	assertEquals(TEST_XSLT_VERSION_INS_1, returnedDtoFirst.getVersion()); 
    	assertEquals(TEST_XSLT_CONTENT_INS_1, returnedDtoFirst.getContentXslTransform()); 
    	assertEquals(TEST_XSLT_NAME_INS_2, returnedDtoSecond.getNameXslTransform()); 
    	assertEquals(TEST_XSLT_ROOT_INS_2, returnedDtoSecond.getTemplateIdRoot()); 
    	assertEquals(TEST_XSLT_VERSION_INS_2, returnedDtoSecond.getVersion()); 
    	assertEquals(TEST_XSLT_CONTENT_INS_2, returnedDtoSecond.getContentXslTransform()); 
    	
    } 
    
    @Test
    void deleteXslTransform() throws Exception{
    	XslTransformDTO dto = new XslTransformDTO(); 
    	
    	Date date = new Date(); 
    	
    	dto.setNameXslTransform(TEST_XSLT_NAME_DEL); 
    	dto.setTemplateIdRoot(TEST_XSLT_ROOT_DEL); 
    	dto.setContentXslTransform(TEST_XSLT_CONTENT_DEL); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdate(date); 

    	xslTransformService.insert(dto); 
    	
    	xslTransformService.delete(TEST_XSLT_ROOT_DEL, TEST_XSLT_VERSION_DEL); 
    	
    	XslTransformDTO emptyDtoToReturn = xslTransformService.findByTemplateIdRootAndVersion(
    			TEST_XSLT_ROOT_DEL, TEST_XSLT_VERSION_DEL); 
    	
    	
    	assertEquals(XslTransformDTO.class, emptyDtoToReturn.getClass()); 
    	
    	assertNull(emptyDtoToReturn.getTemplateIdRoot()); 


    }
    
    
    @Test
    void findTest() throws Exception {
    	XslTransformDTO dto = new XslTransformDTO(); 
    	
    	Date date = new Date(); 
    	
    	dto.setNameXslTransform(TEST_XSLT_NAME_QUERY); 
    	dto.setTemplateIdRoot(TEST_XSLT_ROOT_QUERY); 
    	dto.setContentXslTransform(TEST_XSLT_CONTENT_QUERY); 
		dto.setVersion(TEST_XSLT_VERSION_QUERY);
    	dto.setInsertionDate(date); 
    	dto.setLastUpdate(date); 

    	xslTransformService.insert(dto); 

    	XslTransformDTO returnedDto = xslTransformService.findByTemplateIdRootAndVersion(TEST_XSLT_ROOT_QUERY, TEST_XSLT_VERSION_QUERY); 
    	
    	
    	assertEquals(XslTransformDTO.class, returnedDto.getClass()); 
    	assertEquals(Binary.class, returnedDto.getContentXslTransform().getClass()); 
    	assertEquals(String.class, returnedDto.getNameXslTransform().getClass()); 
    	assertEquals(String.class, returnedDto.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, returnedDto.getVersion().getClass()); 
    	assertEquals(Date.class, returnedDto.getInsertionDate().getClass()); 
    	assertEquals(Date.class, returnedDto.getLastUpdate().getClass()); 
    	
    	assertEquals(TEST_XSLT_NAME_QUERY, returnedDto.getNameXslTransform()); 
    	assertEquals(TEST_XSLT_ROOT_QUERY, returnedDto.getTemplateIdRoot()); 
    	assertEquals(TEST_XSLT_VERSION_QUERY, returnedDto.getVersion()); 
    	assertEquals(TEST_XSLT_CONTENT_QUERY, returnedDto.getContentXslTransform()); 
    	assertEquals(date, returnedDto.getInsertionDate()); 
    	assertEquals(date, returnedDto.getLastUpdate()); 

		// FIND BY ID

    	XslTransformDocumentDTO returnedDto_1 = xslTransformService.findById(returnedDto.getId());
		assertNotNull(returnedDto_1); 
		    	
		// FIND BY ID - Non Existent ID 
    	
    	assertThrows(DocumentNotFoundException.class, () -> xslTransformService.findById("000000000000000000000001")); 

    } 
    
    @Test
    void findByTemplateIdRootAndVersionDeletedElementTest() throws Exception {
    	XslTransformDTO dto = new XslTransformDTO(); 
    	
    	Date date = new Date(); 
    	
    	dto.setNameXslTransform(TEST_XSLT_NAME_QUERY_DEL); 
    	dto.setTemplateIdRoot(TEST_XSLT_ROOT_QUERY_DEL); 
    	dto.setContentXslTransform(TEST_XSLT_CONTENT_QUERY_DEL); 
    	dto.setInsertionDate(date); 
    	dto.setLastUpdate(date); 

    	xslTransformService.insert(dto); 
    	
    	xslTransformService.delete(TEST_XSLT_ROOT_QUERY_DEL, TEST_XSLT_VERSION_QUERY_DEL); 

    	XslTransformDTO returnedDto = xslTransformService.findByTemplateIdRootAndVersion(TEST_XSLT_ROOT_QUERY_DEL, TEST_XSLT_VERSION_QUERY_DEL); 
    	
    	
    	assertEquals(true, returnedDto instanceof XslTransformDTO); 
    	
    	assertNull(returnedDto.getNameXslTransform()); 
    	assertNull(returnedDto.getContentXslTransform()); 
    	assertNull(returnedDto.getTemplateIdRoot()); 
    	assertNull(returnedDto.getVersion()); 

    } 
    
        
    @Test
    void getXslTransformsTest() throws OperationException, DocumentNotFoundException {

    	List<XslTransformDTO> dtoList = new ArrayList<XslTransformDTO>(); 
    	XslTransformDTO dtoFirst = new XslTransformDTO(); 
    	
    	Date date = new Date(); 
    	
    	dtoFirst.setNameXslTransform(TEST_XSLT_NAME_INS_3); 
    	dtoFirst.setTemplateIdRoot(TEST_XSLT_ROOT_INS_3); 
    	dtoFirst.setContentXslTransform(TEST_XSLT_CONTENT_INS_3); 
		dtoFirst.setVersion(TEST_XSLT_VERSION_INS_3);
    	dtoFirst.setInsertionDate(date); 
    	dtoFirst.setLastUpdate(date); 
    	
    	dtoList.add(dtoFirst); 
    	
    	xslTransformService.insertAll(dtoList); 
    	
    	List<XslTransformDTO> dtoGetList = xslTransformService.findAll(); 
    	XslTransformDTO dtoElem = dtoGetList.get(0); 
    	
    	assertEquals(ArrayList.class, dtoGetList.getClass()); 
    	assertEquals(XslTransformDTO.class, dtoElem.getClass()); 
    	
    	assertTrue(dtoGetList.size() > 0); 
    	
    	assertEquals(XslTransformDTO.class, dtoElem.getClass()); 
    	assertEquals(Binary.class, dtoElem.getContentXslTransform().getClass()); 
    	assertEquals(String.class, dtoElem.getNameXslTransform().getClass()); 
    	assertEquals(String.class, dtoElem.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, dtoElem.getVersion().getClass()); 
    	assertEquals(Date.class, dtoElem.getLastUpdate().getClass());
		
		XslTransformDocumentDTO ety = xslTransformService.findById(dtoElem.getId());
		assertNotNull(ety);
    	
    }
    
    @Test
    void buildDtoFromEtyTest() {
    	List<XslTransformETY> etyList = new ArrayList<XslTransformETY>(); 
		XslTransformETY XslTransformEty = new XslTransformETY(); 
		
		Date date = new Date(); 
		
		XslTransformEty.setId(TEST_XSLT_ID); 
		XslTransformEty.setNameXslTransform(TEST_XSLT_NAME); 
		XslTransformEty.setTemplateIdRoot(TEST_XSLT_ROOT); 
		XslTransformEty.setContentXslTransform(TEST_XSLT_CONTENT); 
		XslTransformEty.setTemplateIdExtension(TEST_XSLT_VERSION);
		XslTransformEty.setInsertionDate(date); 
		XslTransformEty.setLastUpdateDate(date); 
		
		etyList.add(XslTransformEty); 
		
		List<XslTransformDTO> dtoList = xslTransformService.buildDtoFromEty(etyList); 
		XslTransformDTO firstElemInParsedList = dtoList.get(0); 
		
		assertEquals(ArrayList.class, dtoList.getClass()); 
		assertEquals(1, dtoList.size()); 
		
		assertEquals(XslTransformDTO.class, firstElemInParsedList.getClass()); 
		assertEquals(String.class, firstElemInParsedList.getNameXslTransform().getClass()); 
		assertEquals(String.class, firstElemInParsedList.getTemplateIdRoot().getClass()); 
		assertEquals(String.class, firstElemInParsedList.getVersion().getClass()); 
		assertEquals(Binary.class, firstElemInParsedList.getContentXslTransform().getClass()); 
		assertEquals(Date.class, firstElemInParsedList.getInsertionDate().getClass()); 
		assertEquals(Date.class, firstElemInParsedList.getLastUpdate().getClass()); 

		assertEquals(TEST_XSLT_NAME, firstElemInParsedList.getNameXslTransform()); 
		assertEquals(TEST_XSLT_ROOT, firstElemInParsedList.getTemplateIdRoot()); 
		assertEquals(TEST_XSLT_VERSION, firstElemInParsedList.getVersion()); 
		assertEquals(TEST_XSLT_CONTENT, firstElemInParsedList.getContentXslTransform()); 
		assertEquals(date, firstElemInParsedList.getInsertionDate()); 
		assertEquals(date, firstElemInParsedList.getLastUpdate()); 
		
    } 
    
	@Test
	void parseEtyToDtoTest() {
		XslTransformETY XslTransformEty = new XslTransformETY(); 
		XslTransformDTO parsedDto = new XslTransformDTO(); 
		
		Date date = new Date(); 
		
		XslTransformEty.setId(TEST_XSLT_ID); 
		XslTransformEty.setNameXslTransform(TEST_XSLT_NAME); 
		XslTransformEty.setTemplateIdRoot(TEST_XSLT_ROOT); 
		XslTransformEty.setContentXslTransform(TEST_XSLT_CONTENT); 
		XslTransformEty.setTemplateIdExtension(TEST_XSLT_VERSION);
		XslTransformEty.setInsertionDate(date); 
		XslTransformEty.setLastUpdateDate(date); 

		parsedDto = xslTransformService.parseEtyToDto(XslTransformEty); 
		
		
		assertEquals(XslTransformDTO.class, parsedDto.getClass()); 
		assertEquals(String.class, parsedDto.getNameXslTransform().getClass()); 
		assertEquals(String.class, parsedDto.getTemplateIdRoot().getClass()); 
		assertEquals(String.class, parsedDto.getVersion().getClass()); 
		assertEquals(Binary.class, parsedDto.getContentXslTransform().getClass()); 
		assertEquals(Date.class, parsedDto.getInsertionDate().getClass()); 
		assertEquals(Date.class, parsedDto.getLastUpdate().getClass()); 

		assertEquals(TEST_XSLT_NAME, parsedDto.getNameXslTransform()); 
		assertEquals(TEST_XSLT_ROOT, parsedDto.getTemplateIdRoot()); 
		assertEquals(TEST_XSLT_VERSION, parsedDto.getVersion()); 
		assertEquals(TEST_XSLT_CONTENT, parsedDto.getContentXslTransform()); 
		assertEquals(date, parsedDto.getInsertionDate()); 
		assertEquals(date, parsedDto.getLastUpdate()); 

	} 
	
	@Test
	void parseDtoToEtyTest() {
		XslTransformDTO XslTransformDto = new XslTransformDTO(); 
		XslTransformETY parsedEty = new XslTransformETY(); 
		
		Date date = new Date(); 
		
		XslTransformDto.setNameXslTransform(TEST_XSLT_NAME); 
		XslTransformDto.setTemplateIdRoot(TEST_XSLT_ROOT); 
		XslTransformDto.setContentXslTransform(TEST_XSLT_CONTENT);
		XslTransformDto.setVersion(TEST_XSLT_VERSION);
		XslTransformDto.setInsertionDate(date); 
		XslTransformDto.setLastUpdate(date); 

		parsedEty = xslTransformService.parseDtoToEty(XslTransformDto); 
		
		
		assertEquals(XslTransformETY.class, parsedEty.getClass()); 
		assertEquals(String.class, parsedEty.getNameXslTransform().getClass()); 
		assertEquals(String.class, parsedEty.getTemplateIdRoot().getClass()); 
		assertEquals(String.class, parsedEty.getTemplateIdExtension().getClass()); 
		assertEquals(Binary.class, parsedEty.getContentXslTransform().getClass()); 
		assertEquals(Date.class, parsedEty.getInsertionDate().getClass()); 
		assertEquals(Date.class, parsedEty.getLastUpdateDate().getClass()); 

		assertEquals(TEST_XSLT_NAME, parsedEty.getNameXslTransform()); 
		assertEquals(TEST_XSLT_ROOT, parsedEty.getTemplateIdRoot()); 
		assertEquals(TEST_XSLT_VERSION, parsedEty.getTemplateIdExtension()); 
		assertEquals(TEST_XSLT_CONTENT, parsedEty.getContentXslTransform()); 
		assertEquals(date, parsedEty.getInsertionDate()); 
		assertEquals(date, parsedEty.getLastUpdateDate()); 

	} 
	

    @AfterAll
    public void teardown() {
        this.dropTestSchema();
    } 
    
    
    
}
