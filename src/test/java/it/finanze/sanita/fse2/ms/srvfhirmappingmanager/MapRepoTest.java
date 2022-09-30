package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.bson.types.Binary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.mongo.impl.MapRepo;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ComponentScan(basePackages = { Constants.ComponentScan.BASE })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class MapRepoTest extends AbstractTest {

    @Autowired
    private MapRepo repository;

    @BeforeAll
    public void setup() throws Exception {
        this.dropCollections();
    }

    @BeforeEach
    public void populateMapRepo() throws DataProcessingException {
        MapETY ety = new MapETY();
        ety.setNameMap(MAP_TEST_NAME_A);
        ety.setContentMap(createFakeMultipart(MAP_TEST_NAME_A));
        ety.setTemplateIdRoot(MAP_TEMPLATE_ID_ROOT);
        ety.setInsertionDate(new Date());
        ety.setLastUpdateDate(new Date());
        ety.setDeleted(false);

        mongo.insert(ety);
    }

    @Test
    void insertDocByNameTest() throws Exception {

        MapETY ety = new MapETY();
        ety.setNameMap(MAP_TEST_NAME_B);
        ety.setContentMap(createFakeMultipart(MAP_TEST_NAME_B));
        ety.setTemplateIdRoot(MAP_TEMPLATE_ID_ROOT);
        ety.setInsertionDate(new Date());
        ety.setLastUpdateDate(new Date());
        ety.setDeleted(false);

        repository.insertDocByName(ety);
        MapETY retrieved = repository.findDocByName(MAP_TEST_NAME_B);

        assertEquals(MapETY.class, retrieved.getClass());
        assertEquals(Binary.class, retrieved.getContentMap().getClass());
        assertEquals(String.class, retrieved.getNameMap().getClass());
        assertEquals(String.class, retrieved.getTemplateIdRoot().getClass());
        assertEquals(Date.class, retrieved.getInsertionDate().getClass());
        assertEquals(Date.class, retrieved.getLastUpdateDate().getClass());
    }

    @Test
    void isDocumentInsertedTest() throws DataProcessingException, OperationException {

        assertTrue(repository.isDocumentInserted(MAP_TEST_NAME_A));
    }

    @Test
    void isDocumentNotInsertedTest() throws OperationException {

        assertFalse(repository.isDocumentInserted(MAP_ID_E));

    }

    @Test
    void updateDocByNameTest() throws DataProcessingException, OperationException {
        MapETY etyB = new MapETY();
        etyB.setNameMap(MAP_TEST_NAME_B);
        etyB.setContentMap(createFakeMultipart(MAP_TEST_NAME_B));
        etyB.setTemplateIdRoot(MAP_TEMPLATE_ID_ROOT);
        etyB.setInsertionDate(new Date());
        etyB.setLastUpdateDate(new Date());
        etyB.setDeleted(false);

        MapETY etyC = new MapETY();
        etyC.setNameMap(MAP_TEST_NAME_C);
        etyC.setContentMap(createFakeMultipart(MAP_TEST_NAME_C));
        etyC.setTemplateIdRoot(MAP_TEMPLATE_ID_ROOT);
        etyC.setInsertionDate(new Date());
        etyC.setLastUpdateDate(new Date());
        etyC.setDeleted(false);

        mongo.insert(etyC);

        assertEquals(etyB, repository.updateDocByName(etyC, etyB));

    }

    @Test
    void deleteDocByNameTest() throws OperationException {

        MapETY deleted = repository.deleteDocByName(MAP_TEST_NAME_A);
        assertEquals(MapETY.class, deleted.getClass());
        assertEquals(Binary.class, deleted.getContentMap().getClass());
        assertEquals(String.class, deleted.getNameMap().getClass());
        assertEquals(String.class, deleted.getTemplateIdRoot().getClass());
        assertEquals(Date.class, deleted.getInsertionDate().getClass());
        assertEquals(Date.class, deleted.getLastUpdateDate().getClass());
    }

    @Test
    void findDocByIdTest() throws OperationException {

        MapETY retrieved = repository.findDocById(repository.findDocByName(MAP_TEST_NAME_A).getId());
        assertEquals(MapETY.class, retrieved.getClass());
        assertEquals(Binary.class, retrieved.getContentMap().getClass());
        assertEquals(String.class, retrieved.getNameMap().getClass());
        assertEquals(String.class, retrieved.getTemplateIdRoot().getClass());
        assertEquals(Date.class, retrieved.getInsertionDate().getClass());
        assertEquals(Date.class, retrieved.getLastUpdateDate().getClass());
    }

    @Test
    void findDocByNameTest() throws OperationException {

        MapETY retrieved = repository.findDocByName(MAP_TEST_NAME_A);
        assertEquals(MapETY.class, retrieved.getClass());
        assertEquals(Binary.class, retrieved.getContentMap().getClass());
        assertEquals(String.class, retrieved.getNameMap().getClass());
        assertEquals(String.class, retrieved.getTemplateIdRoot().getClass());
        assertEquals(Date.class, retrieved.getInsertionDate().getClass());
        assertEquals(Date.class, retrieved.getLastUpdateDate().getClass());

    }

    @Test
    void findDocByNameFailTest() throws OperationException {

        assertNull(repository.findDocByName("Fake name"));
    }
}