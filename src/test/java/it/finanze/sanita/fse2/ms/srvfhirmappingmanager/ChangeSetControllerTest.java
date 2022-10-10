package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.MockRequests.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ComponentScan
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class ChangeSetControllerTest extends AbstractTest {


	@Autowired
	private MockMvc mvc; 
	
	@MockBean
	private Tracer tracer;

	@Test
	void getXslTransformChangeSetTest() throws Exception{

		String queryDate = "2022-06-04T12:08:56.000-00:00";

		mvc.perform(getXslTransformChangeSetMockRequest(queryDate, getBaseUrl())).andExpectAll(
				status().is2xxSuccessful()
		);
	}

	@Test
	void getTransformChangeSetTest() throws Exception{

		String queryDate = "2022-06-04T12:08:56.000-00:00";

		mvc.perform(getTransformChangeSetMockRequest(queryDate, getBaseUrl())).andExpectAll(
				status().is2xxSuccessful()
		);
	}
}
