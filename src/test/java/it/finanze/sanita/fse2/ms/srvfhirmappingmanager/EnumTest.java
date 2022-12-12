/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.ResultLogEnum;

@SpringBootTest
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class EnumTest extends AbstractTest {
	
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
