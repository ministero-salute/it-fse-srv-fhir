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
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error;


import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorInstance.Resource;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorInstance.Server;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorInstance.Validation;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.info.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs.ERR_VAL_UNABLE_CONVERT;
import static org.apache.http.HttpStatus.*;


public final class ErrorBuilderDTO {

    private ErrorBuilderDTO() {}

 public static ErrorResponseDTO createConstraintError(LogTraceInfoDTO trace, ConstraintViolationException ex) {
        // Retrieve the first constraint error
        ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
        String field = UtilsMisc.extractKeyFromPath(violation.getPropertyPath());
        // Return associated information
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            violation.getMessage(),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, field)
        );
    }

    public static ErrorResponseDTO createArgumentMismatchError(LogTraceInfoDTO trace, MethodArgumentTypeMismatchException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            String.format(
                ERR_VAL_UNABLE_CONVERT,
                ex.getName(),
                ex.getParameter().getParameter().getType().getSimpleName()
            ),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, ex.getName())
        );
    }


    public static ErrorResponseDTO createMissingPartError(LogTraceInfoDTO trace, MissingServletRequestPartException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            ex.getMessage(),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, ex.getRequestPartName())
        );
    }

    public static ErrorResponseDTO createGenericError(LogTraceInfoDTO trace, Exception ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.SERVER.getType(),
            ErrorType.SERVER.getTitle(),
            ex.getMessage(),
            SC_INTERNAL_SERVER_ERROR,
            ErrorType.SERVER.toInstance(Server.INTERNAL)
        );
    }

    public static ErrorResponseDTO createOperationError(LogTraceInfoDTO trace, OperationException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.SERVER.getType(),
            ErrorType.SERVER.getTitle(),
            ex.getMessage(),
            SC_INTERNAL_SERVER_ERROR,
            ErrorType.SERVER.toInstance(Server.INTERNAL)
        );
    }

    public static ErrorResponseDTO createDocumentNotFoundError(LogTraceInfoDTO trace, DocumentNotFoundException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.RESOURCE.getType(),
            ErrorType.RESOURCE.getTitle(),
            ex.getMessage(),
            SC_NOT_FOUND,
            ErrorType.RESOURCE.toInstance(Resource.NOT_FOUND)
        );
    }

    public static ErrorResponseDTO createInvalidVersionError(LogTraceInfoDTO trace, InvalidVersionException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            ex.getMessage(),
            SC_CONFLICT,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, "version")
        );

    }

    public static ErrorResponseDTO createInvalidContentError(LogTraceInfoDTO trace, InvalidContentException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            ex.getMessage(),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, ex.getField())
        );

    }

    public static ErrorResponseDTO createDocumentAlreadyPresentError(LogTraceInfoDTO trace, DocumentAlreadyPresentException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.RESOURCE.getType(),
            ErrorType.RESOURCE.getTitle(),
            ex.getMessage(),
            SC_CONFLICT,
            ErrorType.RESOURCE.toInstance(Resource.CONFLICT)
        );
    } 

}
