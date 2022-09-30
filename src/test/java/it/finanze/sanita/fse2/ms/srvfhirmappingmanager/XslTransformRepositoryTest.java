package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ComponentScan(basePackages = { Constants.ComponentScan.BASE })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class XslTransformRepositoryTest extends AbstractTest {
    
    // Test Data 
    private final String TEST_ID_XSLT = "Root_A"; 
    private final String TEST_ID_ROOT = "Root_A"; 
    private final String TEST_ID_VERSION = "Version_A"; 
    
    private final String TEST_INS_XSLT = "Root_E"; 
    private final String TEST_INS_ROOT = "Root_E"; 
    private final String TEST_INS_VERSION = "Version_E"; 
    
    private final String TEST_INS_MANY_XSLT_A = "Root_MANY_A"; 
    private final String TEST_INS_MANY_ROOT_A = "Root_MANY_A"; 
    private final String TEST_INS_MANY_VERSION_A = "Version_MANY_A"; 
    
    private final String TEST_INS_MANY_XSLT_B = "Root_MANY_B"; 
    private final String TEST_INS_MANY_ROOT_B = "Root_MANY_B"; 
    private final String TEST_INS_MANY_VERSION_B = "Root_MANY_B"; 
    
    private final String TEST_UPD_XSLT = "Root_UPD"; 
    private final String TEST_UPD_ROOT = "Root_UPD"; 
    private final String TEST_UPD_VERSION = "Version_UPD";
	
	private final String TEST_UPD_XSLT_NE = "Not Exist"; 
    private final String TEST_UPD_ROOT_NE = "Not Exist"; 
    private final String TEST_UPD_VERSION_NE = "Version Not Exist";
    
    private final String TEST_DEL_XSLT = "Root_DEL"; 
    private final String TEST_DEL_ROOT = "Root_DEL"; 
    private final String TEST_DEL_VERSION = "Version_DEL"; 
    
    
    
    
    @BeforeAll
    public void setup() throws Exception {
		this.dropTestSchema();
        this.initTestRepository();
    }

    
    @Test
    void insertTest() throws Exception {
    	XslTransformETY ety = new XslTransformETY(); 
    	
    	ety.setNameXslTransform(TEST_INS_XSLT);
    	ety.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	ety.setTemplateIdRoot(TEST_INS_ROOT);
    	ety.setVersion(TEST_INS_VERSION);
    	ety.setInsertionDate(new Date()); 
    	ety.setLastUpdateDate(new Date()); 

    	repository.insert(ety); 
    	
    	XslTransformETY retrievedEty = repository.findByTemplateIdRootAndVersion(TEST_INS_ROOT, TEST_INS_VERSION); 
    	
    	assertEquals(XslTransformETY.class, retrievedEty.getClass()); 
    	assertEquals(Binary.class, retrievedEty.getContentXslTransform().getClass()); 
    	assertEquals(String.class, retrievedEty.getNameXslTransform().getClass()); 
    	assertEquals(String.class, retrievedEty.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, retrievedEty.getVersion().getClass()); 
    	assertEquals(Date.class, retrievedEty.getInsertionDate().getClass()); 
    	assertEquals(Date.class, retrievedEty.getLastUpdateDate().getClass()); 
    	
    	assertEquals(TEST_INS_XSLT, retrievedEty.getNameXslTransform()); 
    	assertEquals(TEST_INS_ROOT, retrievedEty.getTemplateIdRoot()); 
    	assertEquals(TEST_INS_VERSION, retrievedEty.getVersion()); 

    } 
    
    @Test
    void insertAllTest() throws Exception {
    	XslTransformETY etyA = new XslTransformETY(); 
    	XslTransformETY etyB = new XslTransformETY(); 
    	List<XslTransformETY> etyList= new ArrayList<XslTransformETY>(); 

    	etyA.setNameXslTransform(TEST_INS_MANY_XSLT_A);
    	etyA.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	etyA.setTemplateIdRoot(TEST_INS_MANY_ROOT_A);
		etyA.setVersion(TEST_INS_MANY_VERSION_A);
    	etyA.setInsertionDate(new Date()); 
    	etyA.setLastUpdateDate(new Date()); 
    	
    	etyB.setNameXslTransform(TEST_INS_MANY_XSLT_B);
    	etyB.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	etyB.setTemplateIdRoot(TEST_INS_MANY_ROOT_B);
		etyB.setVersion(TEST_INS_MANY_VERSION_B);
    	etyB.setInsertionDate(new Date()); 
    	etyB.setLastUpdateDate(new Date()); 
    	
    	etyList.add(etyA); 
    	etyList.add(etyB); 

    	repository.insertAll(etyList); 
    	
    	XslTransformETY retrievedEtyA = repository.findByTemplateIdRootAndVersion(TEST_INS_MANY_ROOT_A, TEST_INS_MANY_VERSION_A); 
    	XslTransformETY retrievedEtyB = repository.findByTemplateIdRootAndVersion(TEST_INS_MANY_ROOT_B, TEST_INS_MANY_VERSION_B); 

    	
    	assertEquals(XslTransformETY.class, retrievedEtyA.getClass()); 
    	assertEquals(Binary.class, retrievedEtyA.getContentXslTransform().getClass()); 
    	assertEquals(String.class, retrievedEtyA.getNameXslTransform().getClass()); 
    	assertEquals(String.class, retrievedEtyA.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, retrievedEtyA.getVersion().getClass()); 
    	assertEquals(Date.class, retrievedEtyA.getLastUpdateDate().getClass()); 
    	assertEquals(Date.class, retrievedEtyA.getInsertionDate().getClass()); 
    	assertEquals(XslTransformETY.class, retrievedEtyB.getClass()); 
    	assertEquals(Binary.class, retrievedEtyB.getContentXslTransform().getClass()); 
    	assertEquals(String.class, retrievedEtyB.getNameXslTransform().getClass()); 
    	assertEquals(String.class, retrievedEtyB.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, retrievedEtyB.getVersion().getClass()); 
    	assertEquals(Date.class, retrievedEtyB.getInsertionDate().getClass()); 
    	assertEquals(Date.class, retrievedEtyB.getLastUpdateDate().getClass());
    	
    	assertEquals(TEST_INS_MANY_XSLT_A, retrievedEtyA.getNameXslTransform()); 
    	assertEquals(TEST_INS_MANY_ROOT_A, retrievedEtyA.getTemplateIdRoot()); 
    	assertEquals(TEST_INS_MANY_VERSION_A, retrievedEtyA.getVersion()); 
    	assertEquals(TEST_INS_MANY_XSLT_B, retrievedEtyB.getNameXslTransform()); 
    	assertEquals(TEST_INS_MANY_ROOT_B, retrievedEtyB.getTemplateIdRoot()); 
    	assertEquals(TEST_INS_MANY_VERSION_B, retrievedEtyB.getVersion()); 
    	
    } 
    
    @Test
    void updateTest() throws Exception {
    	
    	// Inserts the XslTransform to Update 
    	XslTransformETY etyToUpdate = new XslTransformETY(); 
    	
    	etyToUpdate.setNameXslTransform(TEST_UPD_XSLT);
    	etyToUpdate.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	etyToUpdate.setTemplateIdRoot(TEST_UPD_ROOT);
		etyToUpdate.setVersion(TEST_UPD_VERSION);
    	etyToUpdate.setInsertionDate(new Date());
    	etyToUpdate.setLastUpdateDate(new Date());

    	repository.insert(etyToUpdate); 
    	
    	
    	// Creates new updated XSLT and updates the XslTransform
    	XslTransformETY ety = new XslTransformETY(); 
    	
    	ety.setNameXslTransform(TEST_UPD_XSLT);
    	ety.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING_UPDATED.getBytes()));
    	ety.setTemplateIdRoot(TEST_UPD_ROOT);
		ety.setVersion(TEST_UPD_VERSION);
    	ety.setInsertionDate(new Date()); 
    	ety.setLastUpdateDate(new Date());

    	String updatedBinaryData = ety.getContentXslTransform().toString(); 
    	
		assertTrue(repository.update(ety)); 
    	
    	XslTransformETY retrievedEty = repository.findByTemplateIdRootAndVersion(TEST_UPD_ROOT, TEST_UPD_VERSION); 
    	
    	assertEquals(Binary.class, retrievedEty.getContentXslTransform().getClass()); 

    	assertEquals(0, retrievedEty.getContentXslTransform().getType()); 
    	assertEquals(updatedBinaryData, retrievedEty.getContentXslTransform().toString()); 


		/// Update non existing entity

    	XslTransformETY ety1 = new XslTransformETY(); 
    	
    	ety1.setNameXslTransform(TEST_UPD_XSLT_NE);
    	ety1.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING_UPDATED.getBytes()));
    	ety1.setTemplateIdRoot(TEST_UPD_ROOT_NE);
		ety1.setVersion(TEST_UPD_VERSION_NE);
    	ety1.setInsertionDate(new Date()); 
    	ety1.setLastUpdateDate(new Date());

    	assertFalse(repository.update(ety1)); 


    } 
    
    @Test
    void removeXslTransformTest() throws Exception {
    	
    	// Inserts the XslTransform to Update 
    	XslTransformETY etyToDelete = new XslTransformETY(); 
    	
    	etyToDelete.setNameXslTransform(TEST_DEL_XSLT);
    	etyToDelete.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	etyToDelete.setTemplateIdRoot(TEST_DEL_ROOT);
		etyToDelete.setVersion(TEST_DEL_VERSION);
    	etyToDelete.setInsertionDate(new Date()); 
    	etyToDelete.setLastUpdateDate(new Date());

    	repository.insert(etyToDelete); 
   
    	repository.remove(TEST_DEL_ROOT, TEST_DEL_VERSION); 
   
    	XslTransformETY retrievedEty = repository.findByTemplateIdRootAndVersion(TEST_DEL_ROOT, TEST_DEL_VERSION); 	

    	assertNull(retrievedEty.getNameXslTransform()); 
    	assertNull(retrievedEty.getTemplateIdRoot()); 
    	assertNull(retrievedEty.getVersion()); 
    	assertNull(retrievedEty.getInsertionDate()); 
    	assertNull(retrievedEty.getLastUpdateDate()); 

    } 
    
    @Test
    void findByTemplateIdRootAndVERSIONTest() throws Exception {
    	
    	// Inserts the XslTransform to Retrieve 
    	XslTransformETY etyToRetrieve = new XslTransformETY(); 
    	
    	etyToRetrieve.setNameXslTransform(TEST_ID_XSLT);
    	etyToRetrieve.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	etyToRetrieve.setTemplateIdRoot(TEST_ID_ROOT);
    	etyToRetrieve.setVersion(TEST_ID_VERSION);
    	etyToRetrieve.setInsertionDate(new Date());
    	etyToRetrieve.setLastUpdateDate(new Date());

    	repository.insert(etyToRetrieve); 
    	
    	
    	XslTransformETY ety = repository.findByTemplateIdRootAndVersion(TEST_ID_ROOT, TEST_ID_VERSION); 
    	
    	
    	assertEquals(XslTransformETY.class, ety.getClass()); 
    	assertEquals(Binary.class, ety.getContentXslTransform().getClass()); 
    	assertEquals(String.class, ety.getNameXslTransform().getClass()); 
    	assertEquals(String.class, ety.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, ety.getVersion().getClass()); 
    	assertEquals(Date.class, ety.getInsertionDate().getClass()); 
    	assertEquals(Date.class, ety.getLastUpdateDate().getClass()); 
    	
    	assertEquals("Root_A", ety.getNameXslTransform()); 
    	assertEquals("Root_A", ety.getTemplateIdRoot()); 
    	assertEquals( "Version_A", ety.getVersion()); 

    }
    
    
    @Test
    void findAllTest() throws Exception {
    	List<XslTransformETY> etyList = repository.findAll(); 
    	
    	XslTransformETY ety = etyList.get(0); 

    	assertEquals(etyList.getClass(), ArrayList.class); 

    	assertEquals(XslTransformETY.class, ety.getClass()); 
    	assertEquals(Binary.class, ety.getContentXslTransform().getClass()); 
    	assertEquals(String.class, ety.getNameXslTransform().getClass()); 
    	assertEquals(String.class, ety.getTemplateIdRoot().getClass()); 
    	assertEquals(String.class, ety.getVersion().getClass()); 
    	assertEquals(Date.class, ety.getInsertionDate().getClass()); 
    	assertEquals(Date.class, ety.getLastUpdateDate().getClass()); 
    
    }

    
    @Test
    void existByTemplateIdRootTest() throws Exception {
    	boolean existsEty = repository.existByTemplateIdRoot(TEST_ID_ROOT); 
    	
    	assertEquals(true, existsEty); 
    }
    
    @AfterAll
    public void teardown() {
        this.dropTestSchema();
    }
}
