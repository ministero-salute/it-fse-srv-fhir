package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.ErrorLogEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.OperationLogEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.ResultLogEnum;

@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class EnumTest extends AbstractTest {
	
	@Test
	void errorLogEnumTest() {
		ErrorLogEnum errorLogEnum = ErrorLogEnum.KO_XSLT_CREATE; 
		
		assertEquals(ErrorLogEnum.class, errorLogEnum.getClass()); 
		assertEquals(String.class, errorLogEnum.getCode().getClass()); 
		assertEquals(String.class, errorLogEnum.getDescription().getClass()); 

		assertEquals("KO_XSLT_CREATE", errorLogEnum.getCode()); 
		assertEquals("Error while creating XSLT", errorLogEnum.getDescription()); 

	} 
	
	@Test
	void operationLogEnumTest() {
		OperationLogEnum operationLogEnum = OperationLogEnum.POST_XSL_TRANSFORM; 
		
		assertEquals(OperationLogEnum.class, operationLogEnum.getClass()); 
		assertEquals(String.class, operationLogEnum.getCode().getClass()); 
		assertEquals(String.class, operationLogEnum.getDescription().getClass()); 

		assertEquals("POST_XSL_TRANSFORM", operationLogEnum.getCode()); 
		assertEquals("Aggiunta XSLT", operationLogEnum.getDescription()); 

	} 
	
	@Test
	void resultLogEnumTest() {
		ResultLogEnum resultLogEnum = ResultLogEnum.OK; 
		
		assertEquals(ResultLogEnum.class, resultLogEnum.getClass()); 
		assertEquals(String.class, resultLogEnum.getCode().getClass()); 
		assertEquals(String.class, resultLogEnum.getDescription().getClass()); 

		assertEquals("OK", resultLogEnum.getCode()); 
		assertEquals("Operazione eseguita con successo", resultLogEnum.getDescription()); 

	} 
	
}
