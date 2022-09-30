// package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.mongodb.core.MongoTemplate;

// import com.mongodb.MongoException;

// import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
// import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY;



// public abstract class AbstractDatabaseHandler extends AbstractTest {

//     /**
//      * Represent the initialisation process status.
//      * It's used to handle inconsistency which could be generated
//      * if test methods are not called correctly (call-order).
//      */
//     private enum InitState {
//         BOOTING,
//         NONE,
//         READY
//     }
//     /**
//      * Test collection name
//      */

//     /**
//      * The number of schemas available into the database after {@code setupTestRepository()}
//      * is called
//      */
//     public static final int SCHEMA_INTO_DB = 30;

//     @Autowired
//     protected MongoTemplate mongo;

//     private InitState state;

//     protected AbstractDatabaseHandler() {
//         this.state = InitState.NONE;
//     }

//     protected void setupTestRepository() throws IOException, OperationException {
//         if(this.state == InitState.NONE) {
//             // Mark as booting
//             this.state = InitState.BOOTING;
//             // Verify if previous test database exists
//             if (isTestSchemaAvailable()) {
//                 // Drop it, we are going to create a new one
//                 dropTestSchema();
//             }
//             // Make test collection
//             createTestSchema();
//             // Add entities to map instance
//             setupTestEntities();
//             // Add sample documents
//             addMapTestEntityToSchema(MAP_TEST_EXTS_A);
//             addMapTestEntityToSchema(MAP_TEST_EXTS_B);
//             addMapTestEntityToSchema(MAP_TEST_EXTS_C);
//             addDefinitionTestEntityToSchema(DEFINITION_TEST_EXTS_A);
//             addDefinitionTestEntityToSchema(DEFINITION_TEST_EXTS_B);
//             addDefinitionTestEntityToSchema(DEFINITION_TEST_EXTS_C);
//             addValuesetTestEntityToSchema(VALUESET_TEST_EXTS_A);
//             addValuesetTestEntityToSchema(VALUESET_TEST_EXTS_B);
//             addValuesetTestEntityToSchema(VALUESET_TEST_EXTS_C);
//             // addXsltTestEntityToSchema(XSLT_);



//             // Check as init
//             this.state = InitState.READY;
//         }
//     }

//     protected void clearTestRepository() {
//         this.dropTestSchema();
//         this.clearTestEntities();
//     }

//     protected void dropTestSchema() {
//         switch (this.state) {
//             case BOOTING:
//                 mongo.dropCollection(MapETY.class);
//                 break;
//             case READY:
//                 mongo.dropCollection(MapETY.class);
//                 this.state = InitState.NONE;
//                 break;
//             case NONE:
//                 throw new IllegalStateException("You cannot drop the schema without initialisation");
//         }
//     }

//     private boolean isTestSchemaAvailable() {
//         return mongo.getCollectionNames().contains(MapETY.class);
//     }

//     private void createTestSchema() {
//         if(state == InitState.READY) {
//             throw new IllegalStateException("You cannot create the schema after the initialisation");
//         }
//         mongo.createCollection(MapETY.class);
//     }

//     private void addMapTestEntityToSchema(String extension) throws OperationException {
//         try {
//             // Insert
//             mongo.insert(getMapEntities().values(), MapETY.class);
//         }catch(MongoException e) {
//             // Catch data-layer runtime exceptions and turn into a checked exception
//             throw new OperationException("Unable to insert test documents for the given extension" , e);
//         }
//     }

//     private void addDefinitionTestEntityToSchema(String extension) throws OperationException {
//         try {
//             // Insert
//             mongo.insert(getDefinitionEntities().values(), DEFINITION_TEST_COLLECTION);
//         }catch(MongoException e) {
//             // Catch data-layer runtime exceptions and turn into a checked exception
//             throw new OperationException("Unable to insert test documents for the given extension" , e);
//         }
//     }

//     private void addValuesetTestEntityToSchema(String extension) throws OperationException {
//         try {
//             // Insert
//             mongo.insert(getValuesetEntities().values(), VALUESET_TEST_COLLECTION);
//         }catch(MongoException e) {
//             // Catch data-layer runtime exceptions and turn into a checked exception
//             throw new OperationException("Unable to insert test documents for the given extension" , e);
//         }
//     }
// }
