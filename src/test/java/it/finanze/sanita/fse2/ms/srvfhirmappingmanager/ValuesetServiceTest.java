package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.base.ValuesetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IValuesetRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.ValuesetETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IValuesetSRV;

@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ValuesetServiceTest extends AbstractTest {
    
    @Autowired
    private IValuesetSRV valuesetSRV;

    @Autowired
    private IValuesetRepo valuesetRepo;

    @BeforeEach
    void setup() throws ParseException{
        mongo.dropCollection(ValuesetETY.class);
        dataPreparation();
    }

    @AfterEach
    void tearDown() {
       mongo.dropCollection(ValuesetETY.class);
    }


    @Test
    @DisplayName("Find Doc by ID")
    void findDocById() throws OperationException, DocumentNotFoundException{

		assertThrows(DocumentNotFoundException.class, () -> valuesetSRV.findDocById("aaa"));

        List<ValuesetETY> valuesets = mongo.findAll(ValuesetETY.class);

        ValuesetDTO valueset = valuesetSRV.findDocById(valuesets.get(0).getId());
        assertNotNull(valueset);
        
    }


    @Test
    @DisplayName("Insert Doc by Name")
    void insertDocByNameTest() throws OperationException, DocumentAlreadyPresentException, IOException {

        MockMultipartFile file = createFakeFile(VALUESET_TEST_FILENAME_A);

        assertThrows(DocumentAlreadyPresentException.class, () -> valuesetSRV.insertDocByName(VALUESET_TEST_NAME_A, file));

        String valueset = valuesetSRV.insertDocByName(VALUESET_TEST_NAME_B, file);
        assertTrue(valuesetRepo.isDocumentInserted(VALUESET_TEST_NAME_B));
        assertEquals(file.getOriginalFilename(), valueset);
        
    }

   @Test
   @DisplayName("Update Doc by Name")
   void updateDocByNameTest() throws DataProcessingException, OperationException, DocumentNotFoundException {

        MockMultipartFile file = createFakeFile("testFile1");

        assertThrows(DocumentNotFoundException.class, () -> valuesetSRV.updateDocByName("name-not-existing", file));

        String valueset = valuesetSRV.updateDocByName(VALUESET_TEST_NAME_A, file);
        assertEquals(file.getOriginalFilename(), valueset);
        
        
   }


   @Test
   @DisplayName("Delete Doc by Name")
   void deleteDocByNameTest() throws DataProcessingException, OperationException, DocumentNotFoundException {

        assertThrows(DocumentNotFoundException.class, () -> valuesetSRV.deleteDocByName("name-not-existing"));

        String valueset = valuesetSRV.deleteDocByName(VALUESET_TEST_NAME_A);
        assertNotNull(valueset);
   }

   @Test
   @DisplayName("Get Insertions")
   void getInsertionsTest() throws DataProcessingException, OperationException, DocumentNotFoundException {

    List<ValuesetETY> insertions;

    insertions = valuesetRepo.getEveryActiveDocument();
        assertNotNull(insertions);
        assertNotEquals(0, insertions.size());
   }

   @Test
   @DisplayName("Get Deletions")
   void getDeletionsTest() throws DataProcessingException, OperationException, DocumentNotFoundException {
    List<ValuesetETY> deletions;
    Date lastUpdate = new Date();

    deletions = valuesetRepo.getDeletions(lastUpdate);
        assertEquals(0, deletions.size());

        String valuesetDeleted = valuesetSRV.deleteDocByName(VALUESET_TEST_NAME_A);
        assertNotNull(valuesetDeleted);

        deletions = valuesetRepo.getDeletions(lastUpdate);
        assertEquals(1, deletions.size());

   }

    void dataPreparation() throws ParseException{

		ValuesetETY valuesetA = new ValuesetETY();
		valuesetA.setId(VALUESET_TEST_ID_A);
		valuesetA.setNameValueset(VALUESET_TEST_NAME_A);
		valuesetA.setFilenameValueset(VALUESET_TEST_FILENAME_A);
		valuesetA.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
		valuesetA.setInsertionDate(new Date());
		valuesetA.setLastUpdateDate(new Date());

        mongo.save(valuesetA);
	}

}
