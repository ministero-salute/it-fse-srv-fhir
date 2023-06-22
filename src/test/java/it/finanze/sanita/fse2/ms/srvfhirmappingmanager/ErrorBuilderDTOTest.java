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

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.mongodb.MongoException;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.info.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorBuilderDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorInstance.Resource;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorInstance.Server;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorType;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
@TestMethodOrder(MethodOrderer.MethodName.class)
class ErrorBuilderDTOTest extends AbstractTest {
	
    @Test
    void createGenericErrorTest() {

        LogTraceInfoDTO trace = new LogTraceInfoDTO("spanID", "traceID");

        ErrorResponseDTO error = ErrorBuilderDTO.createGenericError(trace, new Exception());
        assertEquals(ErrorType.SERVER.toInstance(Server.INTERNAL),error.getInstance());
        assertEquals(ErrorType.SERVER.getType(), error.getType());
        assertEquals(ErrorType.SERVER.getTitle(), error.getTitle());

    }



    @Test
    void createOperationErrorTest() {
        LogTraceInfoDTO trace = new LogTraceInfoDTO("spanID", "traceID");

        ErrorResponseDTO error = ErrorBuilderDTO.createOperationError(trace, new OperationException(FAKE_INVALID_DTO_ID, new MongoException(FAKE_INVALID_DTO_ID)));
        assertEquals(ErrorType.SERVER.toInstance(Server.INTERNAL), error.getInstance());
        assertEquals(ErrorType.SERVER.getType(), error.getType());
        assertEquals(ErrorType.SERVER.getTitle(), error.getTitle());

    }


    @Test
    void createDocumentNotFoundErrorTest() {
        LogTraceInfoDTO trace = new LogTraceInfoDTO("spanID", "traceID");

        ErrorResponseDTO error =ErrorBuilderDTO.createDocumentNotFoundError(trace, new DocumentNotFoundException(FAKE_INVALID_DTO_ID));
        assertEquals(ErrorType.RESOURCE.toInstance(Resource.NOT_FOUND), error.getInstance());
        assertEquals(ErrorType.RESOURCE.getType(), error.getType());
        assertEquals(ErrorType.RESOURCE.getTitle(), error.getTitle());
        

    }

    @Test
    void createDocumentAlreadyPresentErrorTest() {
        LogTraceInfoDTO trace = new LogTraceInfoDTO("spanID", "traceID");

        ErrorResponseDTO error = ErrorBuilderDTO.createDocumentAlreadyPresentError(trace,new DocumentAlreadyPresentException(FAKE_INVALID_DTO_ID));
        assertEquals(ErrorType.RESOURCE.toInstance(Resource.CONFLICT), error.getInstance());
        assertEquals(ErrorType.RESOURCE.getType(), error.getType());
        assertEquals(ErrorType.RESOURCE.getTitle(), error.getTitle());

    }

}