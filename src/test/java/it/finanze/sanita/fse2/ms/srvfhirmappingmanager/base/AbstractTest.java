package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.base.MapDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.DefinitionETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.ValuesetETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;

public abstract class AbstractTest {

 

    // --- XSLT
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

    public static final String XSLT_TEST_ID_A = "ID_A";
    public static final String XSLT_TEST_ID_B = "ID_B";
    public static final String XSLT_TEST_ID_C = "ID_C";
    public static final String XSLT_TEST_ID_D = "ID_D";
    public static final String XSLT_TEST_ID_E = "ID_E";

    // --- VALUESET
    public static final String VALUESET_TEST_ID_A = new ObjectId().toString();
    public static final String VALUESET_TEST_ID_B = new ObjectId().toString();

    public static final String VALUESET_TEST_NAME_A = "valueset_name_A";
    public static final String VALUESET_TEST_NAME_B = "valueset_name_B";
    public static final String VALUESET_TEST_NAME_C = "valueset_name_C";
    public static final String VALUESET_TEST_NAME_D = "valueset_name_D";
    public static final String VALUESET_TEST_NAME_E = "valueset_name_E";

    public static final String VALUESET_ID_A = "valueset_ID_A";
    public static final String VALUESET_ID_B = "valueset_ID_B";
    public static final String VALUESET_ID_C = "valueset_ID_C";
    public static final String VALUESET_ID_D = "valueset_ID_D";
    public static final String VALUESET_ID_E = "valueset_ID_E";

    public static final String VALUESET_TEST_FILENAME_A = "test_valueset_A";
    public static final String VALUESET_TEST_FILENAME_B = "test_valueset_B";
    public static final String VALUESET_TEST_FILENAME_C = "test_valueset_C";
    public static final String VALUESET_TEST_FILENAME_D = "test_valueset_D";
    public static final String VALUESET_TEST_FILENAME_E = "test_valueset_E";


    // --- DEFINITION
    public static final String DEFINITION_TEST_ID_A = new ObjectId().toString();
    public static final String DEFINITION_TEST_ID_B = new ObjectId().toString();


    public static final String DEFINITION_TEST_NAME_A = "definition_name_A";
    public static final String DEFINITION_TEST_NAME_B = "definition_name_B";
    public static final String DEFINITION_TEST_NAME_C = "definition_name_C";
    public static final String DEFINITION_TEST_NAME_D = "definition_name_D";
    public static final String DEFINITION_TEST_NAME_E = "definition_name_E";

    public static final String DEFINITION_TEST_VERSION_A = "definition_Version_A";
    public static final String DEFINITION_TEST_VERSION_B = "definition_Version_B";
    public static final String DEFINITION_TEST_VERSION_C = "definition_Version_C";
    public static final String DEFINITION_TEST_VERSION_D = "definition_Version_D";
    public static final String DEFINITION_TEST_VERSION_E = "definition_Version_E";

    public static final String DEFINITION_ID_A = "definition_ID_A";
    public static final String DEFINITION_ID_B = "definition_ID_B";
    public static final String DEFINITION_ID_C = "definition_ID_C";
    public static final String DEFINITION_ID_D = "definition_ID_D";
    public static final String DEFINITION_ID_E = "definition_ID_E";

    public static final String DEFINITION_TEST_FILENAME_A = "definition_filename_A";
    public static final String DEFINITION_TEST_FILENAME_B = "definition_filename_B";
    public static final String DEFINITION_TEST_FILENAME_C = "definition_filename_C";
    public static final String DEFINITION_TEST_FILENAME_D = "definition_filename_D";
    public static final String DEFINITION_TEST_FILENAME_E = "definition_filename_E";


    // --- MAP
    public static final String MAP_TEST_NAME_A = "map_name_A";
    public static final String MAP_TEST_NAME_B = "map_name_B";
    public static final String MAP_TEST_NAME_C = "map_name_C";
    public static final String MAP_TEST_NAME_D = "map_name_D";
    public static final String MAP_TEST_NAME_E = "map_name_E";

    public static final String MAP_ID_A = "map_ID_A";
    public static final String MAP_ID_B = "map_ID_B";
    public static final String MAP_ID_C = "map_ID_C";
    public static final String MAP_ID_D = "map_ID_D";
    public static final String MAP_ID_E = "map_ID_E";

    public static final String MAP_TEMPLATE_EXTENSIONS_ID_A = "map_template_extensions_A";
    public static final String MAP_TEMPLATE_EXTENSIONS_ID_B = "map_template_extensions_B";
    public static final String MAP_TEMPLATE_EXTENSIONS_ID_C = "map_template_extensions_C";
    public static final String MAP_TEMPLATE_EXTENSIONS_ID_D = "map_template_extensions_D";
    public static final String MAP_TEMPLATE_EXTENSIONS_ID_E = "map_template_extensions_E";

    public static final String MAP_TEST_VERSION_A = "map_Version_A";
    public static final String MAP_TEST_VERSION_B = "map_Version_B";
    public static final String MAP_TEST_VERSION_C = "map_Version_C";
    public static final String MAP_TEST_VERSION_D = "map_Version_D";
    public static final String MAP_TEST_VERSION_E = "map_Version_E";
    public static final String MAP_TEST_FILENAME_A = "map_File_A";
    public static final String MAP_TEST_FILENAME_B = "map_File_B";
    public static final String MAP_TEST_CONTENT_A = "map_content_A";
    public static final String MAP_TEST_ROOT_A = "map_Root_A";
    public static final String MAP_TEST_ROOT_B = "map_Root_B";
    public static final String MAP_TEST_ROOT_C = "map_Root_c";

    public static final String MAP_TEMPLATE_ID_ROOT = "map_id_root";

    public static final String FILE_TEST_STRING = "Hello World!";
    public static final String FILE_TEST_STRING_UPDATED = "Hello World! - updated";
    public static final String XSLT_TEST_STRING_UPDATED = "Hello World, folks!";

    public static final String FAKE_MULTIPART = "Fake MultipartFile";
    /**
     * This collection does not exist
     */
    public static final String XSLT_TEST_FAKE_NAME = "test_xslt_F";

    /*
     * Map does not exist
     */
    public static final String TEST_ID_NAME_NOT_FOUND = "Map_id_not_found";
    
     
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

    @Value("${server.port}")
    private int serverPort;

    protected String getBaseUrl() {

        return "http://localhost:" + String.valueOf(serverPort) + "/v1";
    }

    @Autowired
    public MongoTemplate mongo;

    protected AbstractTest() {

        this.definitionEntities = new HashMap<>();
        this.mapEntities = new HashMap<>();
        this.xslTransformEntities = new HashMap<>();
        this.valuesetEntities = new HashMap<>();

    }

    protected void populateXslTransform() {

        XslTransformETY xslTransformA = new XslTransformETY();
        xslTransformA.setNameXslTransform(XSLT_TEST_NAME_A);
        xslTransformA.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
        xslTransformA.setTemplateIdRoot(XSLT_TEST_ROOT_A);
        xslTransformA.setVersion(XSLT_TEST_VERSION);
        xslTransformA.setInsertionDate(new Date());
        xslTransformA.setLastUpdateDate(new Date());

        XslTransformETY xslTransformB = new XslTransformETY();
        xslTransformB.setNameXslTransform(XSLT_TEST_NAME_B);
        xslTransformB.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
        xslTransformB.setTemplateIdRoot(XSLT_TEST_ROOT_B);
        xslTransformB.setVersion(XSLT_TEST_VERSION);
        xslTransformB.setInsertionDate(new Date());
        xslTransformB.setLastUpdateDate(new Date());

        XslTransformETY xslTransformC = new XslTransformETY();
        xslTransformC.setNameXslTransform(XSLT_TEST_NAME_C);
        xslTransformC.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
        xslTransformC.setTemplateIdRoot(XSLT_TEST_ROOT_C);
        xslTransformC.setVersion(XSLT_TEST_VERSION);
        xslTransformC.setInsertionDate(new Date());
        xslTransformC.setLastUpdateDate(new Date());

        mongo.save(xslTransformA);
        mongo.save(xslTransformB);
        mongo.save(xslTransformC);

    }

    protected void populateMapTransform() throws DataProcessingException {

        MapETY MapA = new MapETY();
        MapA.setNameMap(MAP_TEST_NAME_A);
        MapA.setContentMap(createFakeMultipart("fake"));
        MapA.setTemplateIdRoot(MAP_TEST_ROOT_A);
        MapA.setInsertionDate(new Date());
        MapA.setLastUpdateDate(new Date());

        MapETY MapB = new MapETY();
        MapB.setNameMap(MAP_TEST_NAME_B);
        MapB.setContentMap(createFakeMultipart("fake"));
        MapB.setTemplateIdRoot(MAP_TEST_ROOT_B);
        MapB.setInsertionDate(new Date());
        MapB.setLastUpdateDate(new Date());
        MapETY MapC = new MapETY();
        MapC.setNameMap(MAP_TEST_NAME_C);
        MapC.setContentMap(createFakeMultipart("fake"));
        MapC.setTemplateIdRoot(MAP_TEST_ROOT_C);
        MapC.setInsertionDate(new Date());
        MapC.setLastUpdateDate(new Date());

        mongo.save(MapA);
        mongo.save(MapB);
        mongo.save(MapC);

    }

    protected void populateValuesetTransform() throws DataProcessingException {

        ValuesetETY ValuesetA = new ValuesetETY();
        ValuesetA.setNameValueset(VALUESET_TEST_NAME_A);
        ValuesetA.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
        ValuesetA.setDeleted(false);
        ValuesetA.setInsertionDate(new Date());
        ValuesetA.setLastUpdateDate(new Date());

        ValuesetETY ValuesetB = new ValuesetETY();
        ValuesetB.setNameValueset(VALUESET_TEST_NAME_B);
        ValuesetB.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
        ValuesetB.setDeleted(false);
        ValuesetB.setInsertionDate(new Date());
        ValuesetB.setLastUpdateDate(new Date());

        ValuesetETY ValuesetC = new ValuesetETY();
        ValuesetC.setNameValueset(VALUESET_TEST_NAME_C);
        ValuesetC.setContentValueset(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
        ValuesetC.setDeleted(false);
        ValuesetC.setInsertionDate(new Date());
        ValuesetC.setLastUpdateDate(new Date());

        mongo.save(ValuesetA);
        mongo.save(ValuesetB);
        mongo.save(ValuesetC);

    }

    protected void populateDefinitionTransform() throws DataProcessingException {

        DefinitionETY DefinitionA = new DefinitionETY();
        DefinitionA.setNameDefinition(DEFINITION_TEST_NAME_A);
        DefinitionA.setContentDefinition(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
        DefinitionA.setId(DEFINITION_ID_A);
        DefinitionA.setDeleted(false);
        DefinitionA.setInsertionDate(new Date());
        DefinitionA.setLastUpdateDate(new Date());

        DefinitionETY DefinitionB = new DefinitionETY();
        DefinitionB.setNameDefinition(DEFINITION_TEST_NAME_B);
        DefinitionB.setContentDefinition(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
        DefinitionB.setId(DEFINITION_ID_B);
        DefinitionB.setDeleted(false);
        DefinitionB.setInsertionDate(new Date());
        DefinitionB.setLastUpdateDate(new Date());

        DefinitionETY DefinitionC = new DefinitionETY();
        DefinitionC.setNameDefinition(DEFINITION_TEST_NAME_C);
        DefinitionC.setContentDefinition(new Binary(BsonBinarySubType.BINARY, FILE_TEST_STRING.getBytes()));
        DefinitionC.setId(DEFINITION_ID_C);
        DefinitionC.setDeleted(false);
        DefinitionC.setInsertionDate(new Date());
        DefinitionC.setLastUpdateDate(new Date());

        mongo.save(DefinitionA);
        mongo.save(DefinitionB);
        mongo.save(DefinitionC);

    }

    private void createXsltTestSchema() {
        mongo.createCollection(XslTransformETY.class);
    }

  

    protected void initTestRepository() throws Exception {
        createXsltTestSchema();

        populateXslTransform();
    }

    protected void dropTestSchema() {
        mongo.dropCollection(XslTransformETY.class);
        mongo.dropCollection(MapETY.class);
        mongo.dropCollection(ValuesetETY.class);
        mongo.dropCollection(DefinitionETY.class);
    }

    public static MockMultipartFile createFakeFile(String filename) {
        return new MockMultipartFile(
                "files",
                filename,
                "multipart/form-data",
                "Hello world!".getBytes());
    }

    public final String FAKE_INVALID_DTO_ID = "||----test";
    /**
     * Valid objectID for test purpose
     */
    public final String FAKE_VALID_DTO_ID = "62cd4f7f5c7e221a80e7effa";

    /**
     * This collection is going to be retrieved
     */
    public static final String MAP_TEST_EXTS_A = "POCD_MT000040UV02";

    public static final String MAP_TEST_EXTS_B = "POCD_MT000040UV03";

    public static final String MAP_TEST_EXTS_C = "POCD_MT000040UV04";

    public static final String MAP_TEST_EXTS_D = "7POCD_MT000040UV05";

    public static final String VALUESET_TEST_EXTS_A = "POCD_MT000040UV02";

    public static final String VALUESET_TEST_EXTS_B = "POCD_MT000040UV03";

    public static final String VALUESET_TEST_EXTS_C = "POCD_MT000040UV04";

    public static final String VALUESET_TEST_EXTS_D = "7POCD_MT000040UV05";

    public static final String DEFINITION_TEST_EXTS_A = "POCD_MT000040UV02";

    public static final String DEFINITION_TEST_EXTS_B = "POCD_MT000040UV03";

    public static final String DEFINITION_TEST_EXTS_C = "POCD_MT000040UV04";

    public static final String DEFINITION_TEST_EXTS_D = "7POCD_MT000040UV05";

    public static final String XSLTRANSFORM_TEST_EXTS_A = "POCD_MT000040UV02";

    public static final String XSLTRANSFORM_TEST_EXTS_B = "POCD_MT000040UV03";

    public static final String XSLTRANSFORM_TEST_EXTS_C = "POCD_MT000040UV04";

    public static final String XSLTRANSFORM_TEST_EXTS_D = "7POCD_MT000040UV05";

    public static final String SCHEMA_TEST_FAKE_EXTS = "POCD_MT000040UV06";

    public static final int SCHEMA_TEST_SIZE = 10;

    public static final String SCHEMA_TEST_ROOT = "CDA.xsd";
    /**
     * Sample parameter for multiple tests
     */
    public static final String SCHEMA_TEST_ROOT_B = "file.xsd";
    /**
     * Directory containing sample files to upload as test
     */

    public static final Path MAP_SAMPLE_FILES = Paths.get(
            "src",
            "test",
            "resources",
            "structureMap");

    public static final Path DEFINITION_SAMPLE_FILES = Paths.get(
            "src",
            "test",
            "resources",
            "structureDefinition");

    public static final Path VALUESET_SAMPLE_FILES = Paths.get(
            "src",
            "test",
            "resources",
            "valueset");
    public static final Path XSLT_SAMPLE_FILES = Paths.get(
            "src",
            "test",
            "resources",
            "xslTransform");

    /**
     * Directory containing modified files used to replace sample ones
     */
    public static final Path MAP_MOD_SAMPLE_FILES = Paths.get(
            "src",
            "test",
            "resources",
            "structureMap",
            "files",
            "modified");

    public static final Path DEFINITION_MOD_SAMPLE_FILES = Paths.get(
            "src",
            "test",
            "resources",
            "definition",
            "files",
            "modified");

    public static final Path VALUESET_MOD_SAMPLE_FILES = Paths.get(
            "src",
            "test",
            "resources",
            "valueset",
            "files",
            "modified");

    public static final Path XSLT_MOD_SAMPLE_FILES = Paths.get(
            "src",
            "test",
            "resources",
            "xslTransform",
            "files",
            "modified");

    public final MapDTO FAKE_DTO = new MapDTO(
            FAKE_VALID_DTO_ID,
            "fake_dto.xsd",
            "Q2lhbw==",
            "POCD_TEST",
            "Root Map",
            "Extension Map",
            OffsetDateTime.now());

    private final Map<String, DefinitionETY> definitionEntities;
    //private final Map<String, DefinitionETY> definitionReplacements;

    private final Map<String, MapETY> mapEntities;
    //private final Map<String, MapETY> mapReplacements;

    private final Map<String, ValuesetETY> valuesetEntities;
    //private final Map<String, ValuesetETY> valuesetReplacements;

    private final Map<String, XslTransformETY> xslTransformEntities;
    //private final Map<String, XslTransformETY> xslTransformReplacements;

    protected void clearTestEntities() {
        this.mapEntities.clear();
        this.definitionEntities.clear();
        this.valuesetEntities.clear();
        this.xslTransformEntities.clear();

    }

    protected Map<String, MapETY> getMapEntities() {
        return new HashMap<>(mapEntities);
    }

    protected Map<String, DefinitionETY> getDefinitionEntities() {
        return new HashMap<>(definitionEntities);
    }

    protected Map<String, ValuesetETY> getValuesetEntities() {
        return new HashMap<>(valuesetEntities);
    }

    protected Map<String, XslTransformETY> getXslTransformEntities() {
        return new HashMap<>(xslTransformEntities);
    }


    protected List<MapETY> getMapEntitiesToUpload() {
        return new ArrayList<>(mapEntities.values());
    }

    protected List<DefinitionETY> getDefinitionEntitiesToUpload() {
        return new ArrayList<>(definitionEntities.values());
    }

    protected List<ValuesetETY> getValuesetEntitiesToUpload() {
        return new ArrayList<>(valuesetEntities.values());
    }

    protected List<XslTransformETY> getXslTransformEntitiesToUpload() {
        return new ArrayList<>(xslTransformEntities.values());
    }

    protected void setupTestEntities() throws IOException {
        // Add entities to map instance
        addMapTestEntityToMap(MAP_TEST_EXTS_A);
        addMapTestEntityToMap(MAP_TEST_EXTS_B);
        addMapTestEntityToMap(MAP_TEST_EXTS_C);
        addMapTestEntityToMap(MAP_TEST_EXTS_D);
        // Add entities to be replaced by tests
       // setMapTestEntityToReplace();

        addDefinitionTestEntityToMap(DEFINITION_TEST_EXTS_A);
        addDefinitionTestEntityToMap(DEFINITION_TEST_EXTS_B);
        addDefinitionTestEntityToMap(DEFINITION_TEST_EXTS_C);
        addDefinitionTestEntityToMap(DEFINITION_TEST_EXTS_D);

        //setDefinitionTestEntityToReplace();

        addValuesetTestEntityToMap(VALUESET_TEST_EXTS_A);
        addValuesetTestEntityToMap(VALUESET_TEST_EXTS_B);
        addValuesetTestEntityToMap(VALUESET_TEST_EXTS_C);
        addValuesetTestEntityToMap(VALUESET_TEST_EXTS_D);

        //setValuesetTestEntityToReplace();

        addXslTransformTestEntityToMap(XSLTRANSFORM_TEST_EXTS_A);
        addXslTransformTestEntityToMap(XSLTRANSFORM_TEST_EXTS_B);
        addXslTransformTestEntityToMap(XSLTRANSFORM_TEST_EXTS_C);
        addXslTransformTestEntityToMap(XSLTRANSFORM_TEST_EXTS_D);

        //setXslTransformTestEntityToReplace();

    }

    private void addDefinitionTestEntityToMap(String extension) throws IOException {
        // List of all files inside the sample modified directory
        try (Stream<Path> files = Files.list(DEFINITION_SAMPLE_FILES)) {
            // Convert to list
            List<Path> samples = files.collect(Collectors.toList());

            MultipartFile fakeFile = createFakeMultipart(DEFINITION_ID_A);

            // Add to each map and convert
            for (Path path : samples) {
                this.definitionEntities
                        .put(
                                path.getFileName().toString(),
                                DefinitionETY.fromPath(
                                        DEFINITION_TEST_NAME_C,
                                        DEFINITION_TEST_VERSION_C,
                                        fakeFile));
            }
        }
    }

    private void addMapTestEntityToMap(String extension) throws IOException {
        // List of all files inside the sample modified directory
        try (Stream<Path> files = Files.list(MAP_SAMPLE_FILES)) {
            // Convert to list
            List<Path> samples = files.collect(Collectors.toList());

            MultipartFile fakeFile = createFakeMultipart(MAP_ID_A);

            // Add to each map and convert
            for (Path path : samples) {
                this.mapEntities
                        .put(
                                path.getFileName().toString(),
                                MapETY.fromPath(
                                        path,
                                        extension,
                                        SCHEMA_TEST_ROOT,
                                        fakeFile));
            }
        }
    }

    private void addValuesetTestEntityToMap(String extension) throws IOException {
        // List of all files inside the sample modified directory
        try (Stream<Path> files = Files.list(VALUESET_SAMPLE_FILES)) {
            // Convert to list
            List<Path> samples = files.collect(Collectors.toList());

            MultipartFile fakeFile = createFakeMultipart(DEFINITION_ID_A);

            // Add to each map and convert
            for (Path path : samples) {
                this.valuesetEntities

                        .put(
                                path.getFileName().toString(),
                                ValuesetETY.fromPath(
                                        VALUESET_TEST_NAME_C,
                                        fakeFile));
            }
        }
    }

    private void addXslTransformTestEntityToMap(String extension) throws IOException {
        // List of all files inside the sample modified directory
        try (Stream<Path> files = Files.list(XSLT_SAMPLE_FILES)) {
            // Convert to list
            List<Path> samples = files.collect(Collectors.toList());

            MultipartFile fakeFile = createFakeMultipart(XSLT_TEST_ID_A);

            // Add to each map and convert
            for (Path path : samples) {
                this.xslTransformEntities

                        .put(
                                path.getFileName().toString(),
                                XslTransformETY.fromPath(
                                        XSLT_TEST_NAME_A,
                                        XSLT_TEST_ID_A,
                                        XSLT_TEST_VERSION_A,
                                        fakeFile));
            }
        }
    }

    public static MockMultipartFile createFakeMultipart(String filename) {
        return new MockMultipartFile(
                "files",
                filename,
                APPLICATION_XML_VALUE,
                "Hello world!".getBytes());
    }
}
