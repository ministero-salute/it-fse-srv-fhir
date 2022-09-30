	package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

	import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
	import static org.junit.jupiter.api.Assertions.assertThrows;
	import static org.mockito.ArgumentMatchers.any;
	import static org.mockito.ArgumentMatchers.anyString;
	import static org.mockito.Mockito.when;

	import java.io.IOException;

	import org.junit.jupiter.api.AfterAll;
	import org.junit.jupiter.api.BeforeAll;
	import org.junit.jupiter.api.DisplayName;
	import org.junit.jupiter.api.MethodOrderer;
	import org.junit.jupiter.api.Test;
	import org.junit.jupiter.api.TestInstance;
	import org.junit.jupiter.api.TestMethodOrder;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.test.context.SpringBootTest;
	import org.springframework.boot.test.mock.mockito.MockBean;
	import org.springframework.context.annotation.ComponentScan;
	import org.springframework.test.context.ActiveProfiles;

	import brave.Tracer;
	import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
	import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
	import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
	import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
	import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.IMapRepo;
	import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY;
	import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IMapSRV;
	import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	@ComponentScan
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@ActiveProfiles(Constants.Profile.TEST)
	@TestMethodOrder(MethodOrderer.MethodName.class)
	class MapServiceTest extends AbstractTest{


		@MockBean
		private Tracer tracer;
		@MockBean
		private IMapRepo repository;
		@Autowired
		private IMapSRV service;




		@BeforeAll
		public void  setup() throws IOException{
			this.setupTestEntities();
		}



		@AfterAll 
		public void teardown() {
			this.clearTestEntities();
		}



		@Test
		@DisplayName(" Find Doc By Id Test")
		void findMapbyIdTest() throws OperationException {
			when(repository.findDocById(anyString()))
			.thenReturn(getMapEntitiesToUpload().get(0));
			// Verify Exception is not thrown
			assertDoesNotThrow(()->{
				service.findDocById(FAKE_VALID_DTO_ID);
			});

		}

		@Test
		void findMapWithInvalidIdTest() {
			// Verify exception is thrown
			assertThrows(DocumentNotFoundException.class, () -> {
				service.findDocById(FAKE_INVALID_DTO_ID);
			});
		}

		@Test
		void findMapWithNotFoundDocument() throws OperationException {
			// Providing mock knowledge
			when(repository.findDocById(anyString())).thenReturn(null);
			// Verify exception is thrown
			assertThrows(DocumentNotFoundException.class, () -> {
				service.findDocById(FAKE_VALID_DTO_ID);
			});
		}




		@Test
		@DisplayName("Find Doc By Name Test")
		void findMapByNameTest() throws OperationException {
			when(repository.findDocByName(anyString()))
			.thenReturn(getMapEntitiesToUpload().get(0));

			assertDoesNotThrow(()->{
				service.findDocByName(FAKE_VALID_DTO_ID);
			});
		}


		@Test
		@DisplayName("Insert Doc By Name Test")
		void insertMapByNameTest() throws OperationException {
			when(repository.isDocumentInserted(MAP_TEST_EXTS_A)).thenReturn(false);
			when(repository.insertDocByName(any())).thenReturn(new MapETY());

			assertDoesNotThrow(()->{
				service.insertDocByName(
						MAP_TEST_NAME_A,
						MAP_TEST_ROOT_A,
						MAP_TEMPLATE_EXTENSIONS_ID_A,
						createFakeFile("Fake"));
			});

		}




		@Test
		@DisplayName("Update Doc By Name Test")
		void updateMapByNameTest() throws OperationException {

			MapETY mappa = new MapETY();
			mappa.setFilenameMap("filenamemap");
			when(repository.findDocByName(anyString())).thenReturn(mappa);
			when(repository.updateDocByName(any(),any())).thenReturn(mappa);
			assertDoesNotThrow(()->{
				service.updateDocByName(
						MAP_TEST_NAME_B,
						createFakeFile(MAP_TEST_FILENAME_A));
			}

					);
		}

		@Test
		@DisplayName("Delete Doc By Name Test")
		void deleteMapByNameTest() throws OperationException {
			when(repository.isDocumentInserted(MAP_TEST_FILENAME_A))
			.thenReturn(true);
			when(repository.deleteDocByName(MAP_TEST_FILENAME_A))
			.thenReturn(new MapETY());
			
			assertDoesNotThrow(()->{
				service.deleteDocByName(MAP_TEST_FILENAME_A);
			});
			

		}
	}
