package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.handler;


import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorBuilderDTO.createConstraintError;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorBuilderDTO.createDocumentAlreadyPresentError;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorBuilderDTO.createDocumentNotFoundError;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorBuilderDTO.createGenericError;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorBuilderDTO.createOperationError;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.ErrorBuilderDTO.createRootNotValidError;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import lombok.extern.slf4j.Slf4j;

/**
 *	Exceptions handler
 *  @author G. Baittiner
 */
@ControllerAdvice
@Slf4j
public class ExceptionCTL extends ResponseEntityExceptionHandler {

    /**
     * Tracker log.
     */
    @Autowired
    private Tracer tracer;



    /**
     * Handle document not found exception.
     *
     * @param ex		exception
     */
    @ExceptionHandler(DocumentNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleDocumentNotFoundException(DocumentNotFoundException ex) {
        // Log me
        log.warn("HANDLER handleDocumentNotFoundException()");
        log.error("HANDLER handleDocumentNotFoundException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createDocumentNotFoundError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    }

    /**
     * Handles exceptions thrown by a root value that does not conform to the accepted ones.
     *
     * @param ex exception
     */
    @ExceptionHandler(RootNotValidException.class)
    protected ResponseEntity<ErrorResponseDTO> handleRootNotValidException(RootNotValidException ex) {
        // Log me
        log.warn("HANDLER handleRootNotValidException()");
        log.error("HANDLER handleRootNotValidException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createRootNotValidError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    }

    /**
     * Handles exceptions thrown by the validation check performed on the request submitted by the user.
     *
     * @param ex exception
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        // Log me
        log.warn("HANDLER handleConstraintViolationException()");
        log.error("HANDLER handleConstraintViolationException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createConstraintError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    }

     /**
     * Handle document already present exception.
     *
     * @param ex		exception
     */
    @ExceptionHandler(DocumentAlreadyPresentException.class)
    protected ResponseEntity<ErrorResponseDTO> handleDocumentAlreadyPresentException(DocumentAlreadyPresentException ex) {
        // Log me
        log.warn("HANDLER handleDocumentAlreadyPresentException()");
        log.error("HANDLER handleDocumentAlreadyPresentException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createDocumentAlreadyPresentError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    } 
    

    /**
     * Handle operation exception.
     *
     * @param ex		exception
     */
    @ExceptionHandler(OperationException.class)
    protected ResponseEntity<ErrorResponseDTO> handleOperationException(OperationException ex) {
        // Log me
        log.warn("HANDLER handleOperationException()");
        log.error("HANDLER handleOperationException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createOperationError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    }

    /**
     * Handle generic exception.
     *
     * @param ex		exception
     */
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        // Log me
        log.warn("HANDLER handleGenericException()");
        log.error("HANDLER handleGenericException()", ex);
        // Create error DTO
        ErrorResponseDTO out = createGenericError(getLogTraceInfo(), ex);
        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        // Bye bye
        return new ResponseEntity<>(out, headers, out.getStatus());
    }

    /**
     * Generate a new {@link LogTraceInfoDTO} instance
     * @return The new instance
     */
    private LogTraceInfoDTO getLogTraceInfo() {
        // Create instance
        LogTraceInfoDTO out = new LogTraceInfoDTO(null, null);
        // Verify if context is available
        if (tracer.currentSpan() != null) {
            out = new LogTraceInfoDTO(
                tracer.currentSpan().context().spanIdString(),
                tracer.currentSpan().context().traceIdString());
        }
        // Return the log trace
        return out;
    }
}