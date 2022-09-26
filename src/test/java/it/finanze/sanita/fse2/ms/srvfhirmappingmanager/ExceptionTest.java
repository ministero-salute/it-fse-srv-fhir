package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.ResultLogEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.ConnectionRefusedException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.XslTransformErrorException;

class ExceptionTest extends AbstractTest {

	@Test
	void businessExceptionTest() {
		BusinessException exc = new BusinessException("Error"); 
		
		assertEquals(BusinessException.class, exc.getClass()); 
		assertEquals("Error", exc.getMessage()); 
		
	} 
	
	@Test
	void businessExceptionTestWithoutMsg() {
		BusinessException exc = new BusinessException(new RuntimeException()); 
		
		assertEquals(BusinessException.class, exc.getClass()); 
		
	} 
	
	@Test
	void connectionRefusedExceptionTest() {
		ConnectionRefusedException exc = new ConnectionRefusedException("http://testurl.io", "Error"); 
		
		assertEquals(ConnectionRefusedException.class, exc.getClass()); 
		assertEquals("http://testurl.io", exc.getUrl()); 
		assertEquals("Error", exc.getMessage()); 
		
	} 
	 
	
	@Test
	void xslTransformErrorExceptionTest() {
		XslTransformErrorException exc = new XslTransformErrorException(ResultLogEnum.KO, "Error", "id"); 
		
		assertEquals(XslTransformErrorException.class, exc.getClass()); 
		assertEquals(ResultLogEnum.KO, exc.getResult()); 
		assertEquals("Error", exc.getMessage()); 
		assertEquals("id", exc.getTransactionId()); 
	} 
	
} 
