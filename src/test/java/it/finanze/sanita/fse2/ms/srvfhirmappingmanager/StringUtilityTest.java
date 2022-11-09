/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.StringUtility;

class StringUtilityTest extends AbstractTest {

	@Test
	void isNullOrEmptyTest() {
		String str = ""; 
		String nullStr = null; 
		
		assertTrue(StringUtility.isNullOrEmpty(str)); 
		assertTrue(StringUtility.isNullOrEmpty(nullStr)); 
	} 
	
	@Test
	void isNullOrEmptyFalse() {
		String str = "Hello World!"; 
		
		assertFalse(StringUtility.isNullOrEmpty(str)); 
		
	} 
	
}
