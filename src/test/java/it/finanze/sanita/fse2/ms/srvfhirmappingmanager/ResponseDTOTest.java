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

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.info.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.base.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseDTOTest extends AbstractTest {

	public HttpStatus RESPONSE_CODE_TEST = HttpStatus.OK; 
	public String RESPONSE_MESSAGE_TEST = "Test Message"; 
	
	public String SPAN_ID_TEST = "d9fg5hkaq8"; 
	public String TRACE_ID_TEST = "d9fgd8aasd"; 
	
	public String IN_TYPE_TEST = "type"; 
	public String IN_TITLE_TEST = "title"; 
	public String IN_DETAIL_TEST = "detail"; 
	public Integer IN_STATUS_TEST = 1; 
	public String IN_INSTANCE_TEST = "instance"; 
	public String IN_TX_ID_TEST = "txId"; 

	
	@Test
	void logTraceInfoDtoTest() {
		LogTraceInfoDTO dto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 
		
		assertEquals(LogTraceInfoDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getSpanID().getClass()); 
		assertEquals(String.class, dto.getTraceID().getClass()); 
		
		assertEquals(SPAN_ID_TEST, dto.getSpanID()); 
		assertEquals(TRACE_ID_TEST, dto.getTraceID()); 
		
	} 
	
	@Test
	void responseDtoTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		ResponseDTO dto = new ResponseDTO(logTraceInfoDto); 
		
		assertEquals(ResponseDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getSpanID().getClass()); 
		assertEquals(String.class, dto.getTraceID().getClass()); 
		
		assertEquals(SPAN_ID_TEST, dto.getSpanID()); 
		assertEquals(TRACE_ID_TEST, dto.getTraceID()); 
	} 
	
	@Test
	void errorResponseDtoTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		ErrorResponseDTO dto = new ErrorResponseDTO(logTraceInfoDto, IN_TYPE_TEST, IN_TITLE_TEST,
				IN_DETAIL_TEST, IN_STATUS_TEST, IN_INSTANCE_TEST); 
		
		
		assertEquals(ErrorResponseDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getType().getClass()); 
		assertEquals(String.class, dto.getTitle().getClass()); 
		assertEquals(String.class, dto.getDetail().getClass()); 
		assertEquals(Integer.class, dto.getStatus().getClass()); 
		assertEquals(String.class, dto.getInstance().getClass()); 
		
		assertEquals(IN_TYPE_TEST, dto.getType()); 
		assertEquals(IN_TITLE_TEST, dto.getTitle()); 
		assertEquals(IN_DETAIL_TEST, dto.getDetail()); 
		assertEquals(IN_STATUS_TEST, dto.getStatus()); 
		assertEquals(IN_INSTANCE_TEST, dto.getInstance()); 
		
	} 
	
	@Test
	void errorResponseDtoLogInfoOnlyTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		ErrorResponseDTO dto = new ErrorResponseDTO(logTraceInfoDto); 
		
		
		assertEquals(ErrorResponseDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getSpanID().getClass()); 
		assertEquals(String.class, dto.getTraceID().getClass()); 
		
		assertEquals(SPAN_ID_TEST, dto.getSpanID()); 
		assertEquals(TRACE_ID_TEST, dto.getTraceID()); 
	} 
	
	@Test
	void changesetResponseDtoTest() {
		ChangeSetResDTO changesetResponse = new ChangeSetResDTO();
		
		Date date = new Date(); 
		
		changesetResponse.setInsertions(new ArrayList<>()); 
		changesetResponse.setDeletions(new ArrayList<>()); 
		changesetResponse.setTimestamp(date); 
		changesetResponse.setLastUpdate(date); 
		changesetResponse.setTotalNumberOfElements(1); 
		
		assertEquals(ArrayList.class, changesetResponse.getInsertions().getClass()); 
		assertEquals(ArrayList.class, changesetResponse.getDeletions().getClass()); 
		assertEquals(Date.class, changesetResponse.getTimestamp().getClass()); 
		assertEquals(Date.class, changesetResponse.getLastUpdate().getClass()); 

		assertEquals(date, changesetResponse.getTimestamp()); 
		assertEquals(date, changesetResponse.getLastUpdate()); 
		assertEquals(1, changesetResponse.getTotalNumberOfElements()); 

	} 
	
	@Test
	void changesetResponseDtoInitTest() {
		ChangeSetResDTO changesetResponse = new ChangeSetResDTO(
			TRACE_ID_TEST,
			SPAN_ID_TEST,
			new Date(),
			new Date(),
			new ArrayList<>(),
			new ArrayList<>(),
			1,
			1);
		
		assertEquals(ArrayList.class, changesetResponse.getInsertions().getClass()); 
		assertEquals(ArrayList.class, changesetResponse.getDeletions().getClass()); 
		assertEquals(Date.class, changesetResponse.getTimestamp().getClass()); 
		assertEquals(Date.class, changesetResponse.getLastUpdate().getClass()); 

		assertEquals(1, changesetResponse.getTotalNumberOfElements()); 

	}
	
}
