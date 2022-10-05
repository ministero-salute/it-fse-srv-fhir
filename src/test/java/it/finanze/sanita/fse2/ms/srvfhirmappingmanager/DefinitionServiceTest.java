package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeAll;
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
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.base.DefinitionDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Definition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IDefinitionSRV;


import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
@TestMethodOrder(MethodOrderer.MethodName.class)
class DefinitionServiceTest extends AbstractTest {


    @Autowired
    private IDefinitionSRV definitionSRV;

    @BeforeAll
    void setup() throws ParseException{
        mongo.dropCollection(Definition.class);
        dataPreparation();
    }

    @Test
    @DisplayName("Find Doc by ID")
    void testA_FindDocById() throws OperationException, DocumentNotFoundException{

		assertThrows(DocumentNotFoundException.class, () -> definitionSRV.findDocById("aaa"));

        List<Definition> definitions = mongo.findAll(Definition.class);

        DefinitionDTO def = definitionSRV.findDocById(definitions.get(0).getId());
        assertNotNull(def);
        
    }


    @Test
    @DisplayName("Insert Doc by Name")
    void testB_insertDocByNameTest() throws OperationException, DocumentAlreadyPresentException, IOException {

        MockMultipartFile file = createFakeFile(DEFINITION_TEST_FILENAME_A);

        assertThrows(DocumentAlreadyPresentException.class, () -> definitionSRV.insertDocByName(DEFINITION_TEST_NAME_A, DEFINITION_TEST_VERSION_A, file));

        String def = definitionSRV.insertDocByName(DEFINITION_TEST_NAME_B, DEFINITION_TEST_VERSION_B, file);
        assertEquals(file.getOriginalFilename(), def);
    }

   @Test
   @DisplayName("Update Doc by Name")
   void testC_updateDocByNameTest() throws DataProcessingException, OperationException, DocumentNotFoundException {

        MockMultipartFile file = createFakeFile("testFile1");

        assertThrows(DocumentNotFoundException.class, () -> definitionSRV.updateDocByName("name-not-existing", file));

        String def = definitionSRV.updateDocByName(DEFINITION_TEST_NAME_A, file);
        assertEquals(file.getOriginalFilename(), def);
        
        
   }


   @Test
   @DisplayName("Delete Doc by Name")
   void testD_deleteDocByNameTest() throws DataProcessingException, OperationException, DocumentNotFoundException {

        assertThrows(DocumentNotFoundException.class, () -> definitionSRV.deleteDocByName("name-not-existing"));

        String def = definitionSRV.deleteDocByName(DEFINITION_TEST_NAME_A);
        assertNotNull(def);
   }



    void dataPreparation() throws ParseException{

		Definition definitionA = new Definition();
		definitionA.setNameDefinition(DEFINITION_TEST_NAME_A);
		definitionA.setFilenameDefinition(DEFINITION_TEST_FILENAME_A);
		definitionA.setVersionDefinition(DEFINITION_TEST_VERSION_A);
		definitionA.setContentDefinition(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
		definitionA.setInsertionDate(new Date());


        mongo.save(definitionA);


	}





    
}
