package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.mongo.impl.XslTransformRepo;



public abstract class AbstractTest {

    @Autowired
    protected XslTransformRepo repository; 
    

    /**
     * This is a test collection initialized on MongoDB 
     */
    public static final String XSLT_TEST_NAME_A = "test_xslt_A";
    public static final String XSLT_TEST_NAME_B = "test_xslt_B";
    public static final String XSLT_TEST_NAME_C = "test_xslt_C";
    public static final String XSLT_TEST_NAME_D = "test_xslt_D"; 
    public static final String XSLT_TEST_NAME_E = "test_xslt_E"; 

    
    public static final String XSLT_TEST_ROOT_A = "Root_A";
    public static final String XSLT_TEST_ROOT_B = "Root_B";
    public static final String XSLT_TEST_ROOT_C = "Root_C";
    public static final String XSLT_TEST_ROOT_D = "Root_D"; 
    public static final String XSLT_TEST_ROOT_E = "Root_E";
    
    public static final String XSLT_TEST_VERSION_A = "Version_A";
    public static final String XSLT_TEST_VERSION_B = "Version_B";
    public static final String XSLT_TEST_VERSION_C = "Version_C";
    public static final String XSLT_TEST_VERSION_D = "Version_D";
    public static final String XSLT_TEST_VERSION_E = "Version_E";
    
    public static final String XSLT_TEST_STRING = "Hello World!";
    public static final String XSLT_TEST_STRING_UPDATED = "Hello World, folks!";

    
    
    /**
     * This collection does not exist
     */
    public static final String XSLT_TEST_FAKE_NAME = "test_xslt_F";
    /**
     * Test collection name
     */
    public static final String XSLT_TEST_COLLECTION = "test_xsl_transform";
    /**
     * Sample parameter for multiple tests
     */
    public static final String XSLT_TEST_NAME = "name_xslTransform";
    /**
     * Sample parameter for multiple tests
     */
    public static final String XSLT_TEST_ROOT = "test_root";
    /**
     * Sample parameter for multiple tests
     */
    public static final String XSLT_TEST_EXTENSION = "test_extension";
    /**
    * Sample parameter for multiple tests
    */
   public static final String XSLT_TEST_VERSION = "version";
    /**
     * Directory containing sample files to upload as test
     */
    public static final Path XSLT_SAMPLE_FILES = Paths.get(
        "src",
        "test",
        "resources",
        "xsl_transform",
        "files",
        "standard");
    /**
     * Directory containing modified files used to replace sample ones
     */
    public static final Path XSLT_MOD_SAMPLE_FILES = Paths.get(
        "src",
        "test",
        "resources",
        "xsl_transform",
        "files",
        "modified");

    @Autowired
    public MongoTemplate mongo;

    protected AbstractTest() {

    }

    protected void populateXslTransform() {
        
    	XslTransformETY xslTransformA = new XslTransformETY(); 
    	xslTransformA.setNameXslTransform(XSLT_TEST_NAME_A); 
     	xslTransformA.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()));
    	xslTransformA.setTemplateIdRoot(XSLT_TEST_ROOT_A); 
    	xslTransformA.setTemplateIdExtension(XSLT_TEST_VERSION); 
    	xslTransformA.setInsertionDate(new Date()); 
    	xslTransformA.setLastUpdateDate(new Date()); 
    	
    	XslTransformETY xslTransformB = new XslTransformETY(); 
    	xslTransformB.setNameXslTransform(XSLT_TEST_NAME_B); 
     	xslTransformB.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()));
    	xslTransformB.setTemplateIdRoot(XSLT_TEST_ROOT_B); 
    	xslTransformB.setTemplateIdExtension(XSLT_TEST_VERSION); 
    	xslTransformB.setInsertionDate(new Date()); 
    	xslTransformB.setLastUpdateDate(new Date()); 
    	
    	XslTransformETY xslTransformC = new XslTransformETY(); 
    	xslTransformC.setNameXslTransform(XSLT_TEST_NAME_C); 
     	xslTransformC.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()));
    	xslTransformC.setTemplateIdRoot(XSLT_TEST_ROOT_C); 
    	xslTransformC.setTemplateIdExtension(XSLT_TEST_VERSION); 
    	xslTransformC.setInsertionDate(new Date()); 
    	xslTransformC.setLastUpdateDate(new Date()); 
    	
        mongo.insert(xslTransformA, XSLT_TEST_COLLECTION);
        mongo.insert(xslTransformB, XSLT_TEST_COLLECTION);
        mongo.insert(xslTransformC, XSLT_TEST_COLLECTION);
    }

    private void createTestSchema() {
        mongo.createCollection(XSLT_TEST_COLLECTION);
    } 
    
    protected void initTestRepository() throws Exception {
    	createTestSchema(); 
        populateXslTransform(); 
    }

    protected void dropTestSchema() {
          mongo.dropCollection(XSLT_TEST_COLLECTION); 
    }
    
}
