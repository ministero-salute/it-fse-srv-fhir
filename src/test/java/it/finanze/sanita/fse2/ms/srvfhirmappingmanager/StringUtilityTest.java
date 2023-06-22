/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright 2023 Ministero della Salute
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
	
	@Test
	void isNullOrEmptyListTest() {
		List<String> listStr = new ArrayList<>(); 
		List<String> listNullStr = null; 
		
		assertTrue(StringUtility.isNullOrEmpty(listStr)); 
		assertTrue(StringUtility.isNullOrEmpty(listNullStr)); 
	} 
	
	@Test
	void isNullOrEmptyListFalse() {
		List<String> listStr = new ArrayList<>(); 
		listStr.add("Test");
		
		assertFalse(StringUtility.isNullOrEmpty(listStr));
		
	}
	
	@Test
	void isEmptyOrHashListTest() {
		List<String> listStr = new ArrayList<>(); 
		
		assertTrue(StringUtility.isEmptyOrHasEmptyItems(listStr)); 
	} 
	
	@Test
	void isEmptyOrHashListFalse() {
		List<String> listStr = new ArrayList<>(); 
		listStr.add("Test");
		
		assertFalse(StringUtility.isEmptyOrHasEmptyItems(listStr));
		
	}
	
	@Test
	void normalizeListString() {
		List<String> listStr = new ArrayList<>(); 
		listStr.add(" test1 ");
		listStr.add(" test2 ");
		
		List<String> normalizedStr = StringUtility.normalize(listStr);
		
		assertEquals("test1", normalizedStr.get(0));
		assertEquals("test2", normalizedStr.get(1));
	}
	
}
