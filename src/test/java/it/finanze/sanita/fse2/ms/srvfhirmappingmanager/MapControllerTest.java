package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.deleteMapMockRequest;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.getMapByNameMockRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.base.MapDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IMapSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
/**
 * Test ChangeSet generation at service level
 */
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
@ComponentScan(basePackages = { Constants.ComponentScan.BASE })
class MapControllerTest extends AbstractTest {
    @MockBean
    private IMapSRV service;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private Tracer tracer;
    @BeforeAll
    public void setup() throws DataProcessingException, ParseException {
        mongo.dropCollection(MapETY.class);
        dataPreparation();
    }
    @AfterAll
    public void tearDown(){
        mongo.dropCollection(MapETY.class);
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "CdaItToBundle.map",
            "CdaToBundle.map",
            "CDAtoFHIRTypes.map",
            "step01.map" })
    @DisplayName("Get Map by Name Test")
    void getMapByNameTest(String mapName) throws Exception {
        final MapETY testMap = generateMapEntity(mapName);
        mongo.save(testMap);
        when(service.findDocByName(anyString())).thenReturn(MapDTO.fromEntity(testMap));
        mvc.perform(get(getBaseUrl() + "/map/" + mapName)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE));
    }
     private MapETY generateMapEntity(String mapName) {
        final byte[] mapContent = UtilsMisc.getFileFromInternalResources("structureMap/" + mapName);
        return new MapETY(mapName, mapName, mapName,
                new Binary(BsonBinarySubType.BINARY, mapContent), "templateIdRoot",
                "templateIdExtension", new Date(), new Date(), false);
    }
    @Test
    @DisplayName("Get Map By Name Fail Test")
    void getMapByNameFailTest() throws Exception {
        when(service.findDocByName(anyString())).thenThrow(new DocumentNotFoundException("Document not Found"));
        mvc.perform(getMapByNameMockRequest(MAP_TEST_NAME_B, getBaseUrl())).andExpectAll(
                status().is4xxClientError());
    }
    @Test
    @DisplayName("Get Map by ID Test")
    void getMapByIdTest() throws Exception {
        when(service.findDocByName(anyString())).thenReturn(any());
        mvc.perform(get(getBaseUrl() + "/map/" + MAP_ID_A)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_VALUE));
    }
  
    @Test
    @DisplayName("Upload Map Test")
    void uploadMapTest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("structure-map.json", "structure-map.json",MediaType.APPLICATION_JSON_VALUE, "Hello World!".getBytes());
        mvc.perform(MockMvcRequestBuilders.multipart(getBaseUrl() + "/map")
                .file(new MockMultipartFile("file", multipartFile.getBytes()))
                .param("name", MAP_TEST_NAME_C)
                .param("root", MAP_TEST_ROOT_C)
                .param("extension",MAP_TEMPLATE_EXTENSIONS_ID_C)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void updateMapTest() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(MAP_TEST_NAME_A, MAP_TEST_NAME_A, MediaType.APPLICATION_JSON_VALUE, FILE_TEST_STRING_UPDATED.getBytes());
        when(
                service.updateDocByName(anyString(), any())).thenReturn("");
        //Execute request
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(getBaseUrl() + "/map/" + MAP_TEST_NAME_A);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request){
                request.setMethod("PUT");
                return request;
            }
        });
        mvc.perform(builder
        .file(new MockMultipartFile("file", multipartFile.getBytes()))
        .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().is2xxSuccessful());
    }
    @Test
    @DisplayName("Delete Map Test")
    void deleteMapTest() throws Exception {
        when(service.deleteDocByName(anyString())).thenReturn(anyString());
            mvc.perform(deleteMapMockRequest(MAP_TEST_NAME_B, getBaseUrl()))
            .andExpectAll(
                status().is2xxSuccessful(),content().contentType(APPLICATION_JSON_VALUE)
                );
    }
    // @Test
    // @DisplayName("Delete Map Fail Test")
    // void deleteMapFailTest() throws Exception {
    //  when(service.deleteDocByName(anyString())).thenReturn(anyString());
    //  mvc.perform(deleteMapMockRequest("proviamo", getBaseUrl())).andExpectAll(
    //          status().is4xxClientError());
    // }
    void dataPreparation() throws ParseException, DataProcessingException {
        MultipartFile file = createFakeMultipart("file");
        MapETY MapA = new MapETY();
        //MapA.setId(MAP_ID_A);
        MapA.setNameMap(MAP_TEST_NAME_A);
        MapA.setFilenameMap(MAP_TEST_FILENAME_A);
        MapA.setTemplateIdRoot(MAP_TEMPLATE_ID_ROOT);
        MapA.setContentMap(file);
        MapA.setInsertionDate(new Date());
        MapETY MapB = new MapETY();
        ///ìMapB.setId(MAP_ID_B);
        MapB.setNameMap(MAP_TEST_NAME_B);
        MapB.setFilenameMap(MAP_TEST_FILENAME_B);
        MapB.setTemplateIdRoot(MAP_TEMPLATE_ID_ROOT);
        MapB.setContentMap(file);
        MapB.setInsertionDate(new Date());
        mongo.insert(MapB);
        mongo.insert(MapA);
    }
}