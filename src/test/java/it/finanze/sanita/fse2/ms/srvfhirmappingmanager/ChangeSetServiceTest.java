package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.XSLTransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Definition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Map;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Valueset;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IDefinitionSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IMapSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IValuesetSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IXslTransformSRV;


/**
 * Test ChangeSet generation at service level
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class ChangeSetServiceTest extends AbstractTest{

    @Autowired
    private IXslTransformSRV xslTransformSRV;
    
    @Autowired
    private IValuesetSRV serviceVS;
    
    @Autowired
    private IDefinitionSRV serviceDF;
    

    @Autowired
    private IMapSRV serviceMP;

    @BeforeAll
    void setup() throws ParseException{
        mongo.dropCollection(XslTransformETY.class);
        mongo.dropCollection(Valueset.class);
        mongo.dropCollection(Definition.class);
        mongo.dropCollection(Map.class);

    }
    
    @Test
    @DisplayName("Test ValueSet Changeset Service")
    void testValuesetChangeset() throws ParseException, OperationException {
    	valuesetDataPreparation();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date queryDate = sdf.parse("2022-06-01T10:00:00");

        List<ChangeSetDTO<ValuesetCS>> allValueset = serviceVS.getInsertions(null);
        assertEquals(4, allValueset.size());

        List<ChangeSetDTO<ValuesetCS>> insertions = serviceVS.getInsertions(queryDate);
        assertEquals(2, insertions.size());

        List<ChangeSetDTO<ValuesetCS>> deletions = serviceVS.getDeletions(queryDate);
        assertEquals(1, deletions.size());
    	
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


    @Test
    @DisplayName("Test Get Definition ChangeSet Service")

    void    GetDefinitionChangeSetTest() throws ParseException, DataProcessingException, OperationException {
    		DefinitionDataPreparation();
    		
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date queryDate = sdf.parse("2022-06-01T10:00:00");

            List<ChangeSetDTO<DefinitionCS>> allValueset = serviceDF.getInsertions(null);
            assertEquals(4, allValueset.size());

            List<ChangeSetDTO<DefinitionCS>> insertions = serviceDF.getInsertions(queryDate);
            assertEquals(2, insertions.size());

            List<ChangeSetDTO<ValuesetCS>> deletions = serviceVS.getDeletions(queryDate);
            assertEquals(1, deletions.size());
    }

    @Test
    @DisplayName("Test Map ChangeSet Service")

    void GetMapChangesetTest() throws ParseException, DataProcessingException, OperationException {
    	MapChangesetDataPreparation();
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date queryDate = sdf.parse("2022-06-01T10:00:00");
    	
    	List<ChangeSetDTO<MapCS>> allValueset = serviceMP.getInsertions(null);
        assertEquals(4, allValueset.size());

        List<ChangeSetDTO<MapCS>> insertions = serviceMP.getInsertions(queryDate);
        assertEquals(2, insertions.size());

       
        List<ChangeSetDTO<MapCS>> deletions = serviceMP.getDeletions(queryDate);
        assertEquals(1, deletions.size());

    }
    
    
    private void valuesetDataPreparation() throws ParseException {

    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
         Date insertionDate = sdf.parse("2022-06-18T10:00:00");
         Date oldInsertionDate = sdf.parse("2021-05-4T10:00:00");
         Date lastUpdate = sdf.parse("2022-06-20T10:00:00");
         
         Valueset valuesetA = new Valueset();
         valuesetA.setFilenameValueset(VALUESET_TEST_FILENAME_A);
         valuesetA.setNameValueset(VALUESET_TEST_NAME_A);
         valuesetA.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
         valuesetA.setInsertionDate(oldInsertionDate);

         Valueset valuesetB = new Valueset();
         valuesetB.setFilenameValueset(VALUESET_TEST_FILENAME_B);
         valuesetB.setNameValueset(VALUESET_TEST_NAME_B);
         valuesetB.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
         valuesetB.setInsertionDate(insertionDate);
         
         Valueset valuesetC = new Valueset();
         valuesetC.setFilenameValueset(VALUESET_TEST_FILENAME_C);
         valuesetC.setNameValueset(VALUESET_TEST_NAME_C);
         valuesetC.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
         valuesetC.setInsertionDate(insertionDate);
         valuesetC.setLastUpdateDate(lastUpdate);

         Valueset valuesetD = new Valueset();
         valuesetD.setFilenameValueset(VALUESET_TEST_FILENAME_D);
         valuesetD.setNameValueset(VALUESET_TEST_NAME_D);
         valuesetD.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
         valuesetD.setInsertionDate(oldInsertionDate);
         valuesetD.setLastUpdateDate(lastUpdate);

         Valueset valuesetE = new Valueset();
         valuesetE.setFilenameValueset(VALUESET_TEST_FILENAME_E);
         valuesetE.setNameValueset(VALUESET_TEST_NAME_E);
         valuesetE.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
         valuesetE.setInsertionDate(oldInsertionDate);
         valuesetE.setLastUpdateDate(lastUpdate);
         valuesetE.setDeleted(true);
         
                
        mongo.save(valuesetA);
        mongo.save(valuesetB);
        mongo.save(valuesetC);
        mongo.save(valuesetD);
        mongo.save(valuesetE);
    }
    

    private void xslTransformDataPreparation() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date insertionDate = sdf.parse("2022-06-18T10:00:00");
        Date oldInsertionDate = sdf.parse("2021-05-4T10:00:00");
        Date lastUpdate = sdf.parse("2022-06-20T10:00:00");


        XslTransformETY xslTransformA = new XslTransformETY(); 
    	xslTransformA.setNameXslTransform(XSLT_TEST_NAME_A); 
     	xslTransformA.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	xslTransformA.setTemplateIdRoot(XSLT_TEST_ROOT_A); 
        xslTransformA.setVersion(XSLT_TEST_VERSION_A);
        xslTransformA.setInsertionDate(oldInsertionDate);
    	
    	XslTransformETY xslTransformB = new XslTransformETY(); 
    	xslTransformB.setNameXslTransform(XSLT_TEST_NAME_B); 
     	xslTransformB.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	xslTransformB.setTemplateIdRoot(XSLT_TEST_ROOT_B); 
        xslTransformB.setVersion(XSLT_TEST_VERSION_B);
        xslTransformB.setInsertionDate(insertionDate);
    	
    	XslTransformETY xslTransformC = new XslTransformETY(); 
    	xslTransformC.setNameXslTransform(XSLT_TEST_NAME_C); 
     	xslTransformC.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	xslTransformC.setTemplateIdRoot(XSLT_TEST_ROOT_C); 
        xslTransformC.setVersion(XSLT_TEST_VERSION_C);
        xslTransformC.setInsertionDate(insertionDate);
        xslTransformC.setLastUpdateDate(lastUpdate);

        XslTransformETY xslTransformD = new XslTransformETY(); 
    	xslTransformD.setNameXslTransform(XSLT_TEST_NAME_D); 
     	xslTransformD.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	xslTransformD.setTemplateIdRoot(XSLT_TEST_ROOT_D); 
        xslTransformD.setVersion(XSLT_TEST_VERSION_D);
        xslTransformD.setInsertionDate(oldInsertionDate);
        xslTransformD.setLastUpdateDate(lastUpdate);

        XslTransformETY xslTransformE = new XslTransformETY(); 
    	xslTransformE.setNameXslTransform(XSLT_TEST_NAME_E); 
     	xslTransformE.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
    	xslTransformE.setTemplateIdRoot(XSLT_TEST_ROOT_E); 
        xslTransformE.setVersion(XSLT_TEST_VERSION_E);
        xslTransformE.setInsertionDate(oldInsertionDate);
        xslTransformE.setLastUpdateDate(lastUpdate);
        xslTransformE.setDeleted(true);
    	

        mongo.save(xslTransformA);
        mongo.save(xslTransformB);
        mongo.save(xslTransformC);
        mongo.save(xslTransformD);
        mongo.save(xslTransformE);
    }
    
    
    private void DefinitionDataPreparation() throws ParseException, DataProcessingException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date insertionDate = sdf.parse("2022-06-18T10:00:00");
        Date oldInsertionDate = sdf.parse("2021-05-4T10:00:00");
        Date lastUpdate = sdf.parse("2022-06-20T10:00:00");
   
        Definition DefinitionTransformA = new Definition();
        DefinitionTransformA.setNameDefinition(DEFINITION_TEST_NAME_A); 
        DefinitionTransformA.setFilenameDefinition(DEFINITION_TEST_FILENAME_A);
        DefinitionTransformA.setMultipartContentDefinition(createFakeFile("test file"));
        DefinitionTransformA.setId(DEFINITION_ID_A); 
        DefinitionTransformA.setVersionDefinition(DEFINITION_TEST_VERSION_A);
        DefinitionTransformA.setInsertionDate(oldInsertionDate);
        
        
        Definition DefinitionTransformB = new Definition();
        DefinitionTransformB.setNameDefinition(DEFINITION_TEST_NAME_B); 
        DefinitionTransformB.setFilenameDefinition(DEFINITION_TEST_FILENAME_B);
        DefinitionTransformB.setMultipartContentDefinition(createFakeFile("test file"));
        DefinitionTransformB.setId(DEFINITION_ID_B); 
        DefinitionTransformB.setVersionDefinition(DEFINITION_TEST_VERSION_B);
        DefinitionTransformB.setInsertionDate(insertionDate); 

        
        Definition DefinitionTransformC = new Definition();
        DefinitionTransformC.setNameDefinition(DEFINITION_TEST_NAME_C); 
        DefinitionTransformC.setFilenameDefinition(DEFINITION_TEST_FILENAME_C);
        DefinitionTransformC.setMultipartContentDefinition(createFakeFile("test file"));
        DefinitionTransformC.setId(DEFINITION_ID_C); 
        DefinitionTransformC.setVersionDefinition(DEFINITION_TEST_VERSION_C);
        DefinitionTransformC.setInsertionDate(insertionDate); 
        DefinitionTransformC.setLastUpdateDate(lastUpdate); 


        
        Definition DefinitionTransformD = new Definition();
        DefinitionTransformD.setNameDefinition(DEFINITION_TEST_NAME_D); 
        DefinitionTransformD.setFilenameDefinition(DEFINITION_TEST_FILENAME_D);
        DefinitionTransformD.setMultipartContentDefinition(createFakeFile("test file"));
        DefinitionTransformD.setId(DEFINITION_ID_D); 
        DefinitionTransformD.setVersionDefinition(DEFINITION_TEST_VERSION_D);
        DefinitionTransformD.setInsertionDate(oldInsertionDate); 
        DefinitionTransformD.setLastUpdateDate(lastUpdate); 


        
        Definition DefinitionTransformE = new Definition();
        DefinitionTransformE.setNameDefinition(DEFINITION_TEST_NAME_E); 
        DefinitionTransformE.setFilenameDefinition(DEFINITION_TEST_FILENAME_E);
        DefinitionTransformE.setMultipartContentDefinition(createFakeFile("test file"));
        DefinitionTransformE.setId(DEFINITION_ID_E); 
        DefinitionTransformE.setVersionDefinition(DEFINITION_TEST_VERSION_E);
        DefinitionTransformE.setInsertionDate(oldInsertionDate); 
        DefinitionTransformE.setLastUpdateDate(lastUpdate); 
        DefinitionTransformE.setDeleted(true);

    
        mongo.save(DefinitionTransformA);
        mongo.save(DefinitionTransformB);
        mongo.save(DefinitionTransformC);
        mongo.save(DefinitionTransformD);
        mongo.save(DefinitionTransformE);
        
    }
  
    
    private void MapChangesetDataPreparation() throws ParseException, DataProcessingException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date insertionDate = sdf.parse("2022-06-18T10:00:00");
        Date oldInsertionDate = sdf.parse("2021-05-4T10:00:00");
        Date lastUpdate = sdf.parse("2022-06-20T10:00:00");
        
        Map MapA= new Map();
        Map MapB= new Map();
        Map MapC= new Map();
        Map MapD= new Map();
        Map MapE= new Map();

        
        
        MapA.setNameMap(MAP_TEST_NAME_A);
        MapA.setContentMap(createFakeFile("test file"));
        MapA.setId(MAP_ID_A);
        MapA.setTemplateIdExtension(MAP_TEMPLATE_EXTENSIONS_ID_A);
        MapA.setInsertionDate(oldInsertionDate);
 
        MapB.setNameMap(MAP_TEST_NAME_B);
        MapB.setContentMap(createFakeFile("test file"));
        MapB.setId(MAP_ID_B);
        MapB.setTemplateIdExtension(MAP_TEMPLATE_EXTENSIONS_ID_B);
        MapB.setInsertionDate(insertionDate);

        MapC.setNameMap(MAP_TEST_NAME_C);
        MapC.setContentMap(createFakeFile("test file"));
        MapC.setId(MAP_ID_C);
        MapC.setTemplateIdExtension(MAP_TEMPLATE_EXTENSIONS_ID_C);
        MapC.setInsertionDate(insertionDate);
        MapC.setLastUpdateDate(lastUpdate);

        MapD.setNameMap(MAP_TEST_NAME_D);
        MapD.setContentMap(createFakeFile("test file"));
        MapD.setId(MAP_ID_D);
        MapD.setTemplateIdExtension(MAP_TEMPLATE_EXTENSIONS_ID_D);
        MapD.setInsertionDate(oldInsertionDate);
        MapD.setLastUpdateDate(lastUpdate);


        MapE.setNameMap(MAP_TEST_NAME_E);
        MapE.setContentMap(createFakeFile("test file"));
        MapE.setId(MAP_ID_E);
        MapE.setTemplateIdExtension(MAP_TEMPLATE_EXTENSIONS_ID_E);
        MapE.setInsertionDate(oldInsertionDate);
        MapE.setLastUpdateDate(lastUpdate);
        MapE.setDeleted(true);
        
        
        mongo.save(MapA);
        mongo.save(MapB);
        mongo.save(MapC);
        mongo.save(MapD);
        mongo.save(MapE);
        
    }
    

}
