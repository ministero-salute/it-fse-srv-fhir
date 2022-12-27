/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureDefinition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureMap;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureValueset;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.StringUtility;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.TransformGenUtility;

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
	void updateDefinitionsTest() throws DataProcessingException {
		List<StructureDefinition> structureDefinition = new ArrayList<StructureDefinition>(); 
		StructureDefinition sd = new StructureDefinition(); 
		structureDefinition.add(sd); 
		
		byte[] content = "Hello World".getBytes(); 
		MultipartFile file1 = new MockMultipartFile("file1", "file1.txt", "id", content); 
		MultipartFile file2 = new MockMultipartFile("file2", "file2.txt", "id", content); 
		MultipartFile file3 = new MockMultipartFile("file3", "file3.txt", "id", content); 
		
		Map<String, StructureDefinition> result = TransformGenUtility
				.updateDefinitions("test", structureDefinition, new MultipartFile[] {file1, file2, file3}); 
		
		assertEquals(4, result.size()); 

	} 
	
	@Test
	void updateMapsTest() throws DataProcessingException {
		List<StructureMap> structureMap = new ArrayList<StructureMap>(); 
		StructureMap sm = new StructureMap(); 
		structureMap.add(sm); 
		
		byte[] content = "Hello World".getBytes(); 
		MultipartFile file1 = new MockMultipartFile("file1", "file1.txt", "id", content); 
		MultipartFile file2 = new MockMultipartFile("file2", "file2.txt", "id", content); 
		MultipartFile file3 = new MockMultipartFile("file3", "file3.txt", "id", content); 
		
		Map<String, StructureMap> result = TransformGenUtility
				.updateMaps(structureMap, new MultipartFile[] {file1, file2, file3}); 
		
		assertEquals(4, result.size()); 

	} 
	
	@Test
	void updateValuesetsTest() throws DataProcessingException {
		List<StructureValueset> structureMap = new ArrayList<StructureValueset>(); 
		StructureValueset vl = new StructureValueset(); 
		structureMap.add(vl); 
		
		byte[] content = "Hello World".getBytes(); 
		MultipartFile file1 = new MockMultipartFile("file1", "file1.txt", "id", content); 
		MultipartFile file2 = new MockMultipartFile("file2", "file2.txt", "id", content); 
		MultipartFile file3 = new MockMultipartFile("file3", "file3.txt", "id", content); 
		
		Map<String, StructureValueset> result = TransformGenUtility
				.updateValuesets(structureMap, new MultipartFile[] {file1, file2, file3}); 
		
		assertEquals(4, result.size()); 

	} 
	
}
