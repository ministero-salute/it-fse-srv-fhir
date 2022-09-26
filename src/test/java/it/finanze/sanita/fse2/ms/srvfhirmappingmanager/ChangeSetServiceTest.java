package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.XSLTransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IXslTransformSRV;


/**
 * Test ChangeSet generation at service level
 */
@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class ChangeSetServiceTest extends AbstractTest{

    @Autowired
    private IXslTransformSRV xslTransformSRV;

    @BeforeEach
    void setup() {
        mongo.dropCollection(XslTransformETY.class);
    }

    @AfterAll
    void tearDown() {
       mongo.dropCollection(XslTransformETY.class);
    }

    @Test
    @DisplayName("Test XSLT ChangeSet Service")
    void testXsltChangeSet() throws Exception {
        xslTransformDataPreparation();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date queryDate = sdf.parse("2022-06-01T10:00:00");

        List<ChangeSetDTO<XSLTransformCS>> allXSLT = xslTransformSRV.getInsertions(null);
        assertEquals(4, allXSLT.size());

        List<ChangeSetDTO<XSLTransformCS>> insertions = xslTransformSRV.getInsertions(queryDate);
        assertEquals(2, insertions.size());

        List<ChangeSetDTO<XSLTransformCS>> deletions = xslTransformSRV.getDeletions(queryDate);
        assertEquals(1, deletions.size());
        
    }

    private void xslTransformDataPreparation() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date insertionDate = sdf.parse("2022-06-18T10:00:00");
        Date oldInsertionDate = sdf.parse("2021-05-4T10:00:00");
        Date lastUpdate = sdf.parse("2022-06-20T10:00:00");


        XslTransformETY xslTransformA = new XslTransformETY(); 
    	xslTransformA.setNameXslTransform(XSLT_TEST_NAME_A); 
     	xslTransformA.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()));
    	xslTransformA.setTemplateIdRoot(XSLT_TEST_ROOT_A); 
        xslTransformA.setTemplateIdExtension(XSLT_TEST_VERSION_A);
        xslTransformA.setInsertionDate(oldInsertionDate);
    	
    	XslTransformETY xslTransformB = new XslTransformETY(); 
    	xslTransformB.setNameXslTransform(XSLT_TEST_NAME_B); 
     	xslTransformB.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()));
    	xslTransformB.setTemplateIdRoot(XSLT_TEST_ROOT_B); 
        xslTransformB.setTemplateIdExtension(XSLT_TEST_VERSION_B);
        xslTransformB.setInsertionDate(insertionDate);
    	
    	XslTransformETY xslTransformC = new XslTransformETY(); 
    	xslTransformC.setNameXslTransform(XSLT_TEST_NAME_C); 
     	xslTransformC.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()));
    	xslTransformC.setTemplateIdRoot(XSLT_TEST_ROOT_C); 
        xslTransformC.setTemplateIdExtension(XSLT_TEST_VERSION_C);
        xslTransformC.setInsertionDate(insertionDate);
        xslTransformC.setLastUpdateDate(lastUpdate);

        XslTransformETY xslTransformD = new XslTransformETY(); 
    	xslTransformD.setNameXslTransform(XSLT_TEST_NAME_D); 
     	xslTransformD.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()));
    	xslTransformD.setTemplateIdRoot(XSLT_TEST_ROOT_D); 
        xslTransformD.setTemplateIdExtension(XSLT_TEST_VERSION_D);
        xslTransformD.setInsertionDate(oldInsertionDate);
        xslTransformD.setLastUpdateDate(lastUpdate);

        XslTransformETY xslTransformE = new XslTransformETY(); 
    	xslTransformE.setNameXslTransform(XSLT_TEST_NAME_E); 
     	xslTransformE.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, XSLT_TEST_STRING.getBytes()));
    	xslTransformE.setTemplateIdRoot(XSLT_TEST_ROOT_E); 
        xslTransformE.setTemplateIdExtension(XSLT_TEST_VERSION_E);
        xslTransformE.setInsertionDate(oldInsertionDate);
        xslTransformE.setLastUpdateDate(lastUpdate);
        xslTransformE.setDeleted(true);
    	
        mongo.insert(xslTransformA, XSLT_TEST_COLLECTION);
        mongo.insert(xslTransformB, XSLT_TEST_COLLECTION);
        mongo.insert(xslTransformC, XSLT_TEST_COLLECTION);
        mongo.insert(xslTransformD, XSLT_TEST_COLLECTION);
        mongo.insert(xslTransformE, XSLT_TEST_COLLECTION);
    }
    
}
