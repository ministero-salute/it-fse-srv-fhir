package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.IXslTransformCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.GetXsltDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.GetXsltResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.GetXsltResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDocumentDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.GetDocumentResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.XslTransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.XslTransformResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.XslTransformUploadResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IXslTransformSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.StringUtility;
import lombok.extern.slf4j.Slf4j;


/** 
 * @author Riccardo Bonesi
 */
@RestController
@Slf4j
public class XslTransformCTL extends AbstractCTL implements IXslTransformCTL {
	
	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = -4658843113005895201L;
	
	@Autowired
	private transient IXslTransformSRV xslTransformService; 

	
	@Override
	public ResponseEntity<XslTransformUploadResponseDTO> addXslTransform(String templateIdRoot, String version, MultipartFile file, HttpServletRequest request) throws IOException, OperationException, DocumentAlreadyPresentException {
		log.debug(Constants.Logs.CALLED_API_POST_XSL_TRANSFORM); 
		Date date = new Date(); 
		
		if(!file.isEmpty()) {
			XslTransformDTO xslTransform = new XslTransformDTO();
			xslTransform.setTemplateIdRoot(templateIdRoot);
			xslTransform.setNameXslTransform(file.getOriginalFilename());
			xslTransform.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
			xslTransform.setVersion(version);
			xslTransform.setInsertionDate(date); 
			xslTransform.setLastUpdate(date); 
			xslTransformService.insert(xslTransform);
			return new ResponseEntity<>(new XslTransformUploadResponseDTO(getLogTraceInfo(),1), HttpStatus.CREATED); 
		}
		return new ResponseEntity<>(new XslTransformUploadResponseDTO(getLogTraceInfo(),0), HttpStatus.NO_CONTENT); 
	}

	@Override
	public ResponseEntity<XslTransformResponseDTO> updateXslTransform(String templateIdRoot, String version,MultipartFile file,HttpServletRequest request) throws IOException, OperationException {
		log.debug(Constants.Logs.CALLED_API_PUT_XSL_TRANSFORM); 
		
		Date date = new Date(); 
		boolean hasBeenUpdated = false; 
		
		if(!file.isEmpty()) {
			XslTransformDTO xslTransform = new XslTransformDTO();
			xslTransform.setTemplateIdRoot(templateIdRoot);
			xslTransform.setNameXslTransform(file.getOriginalFilename());
			xslTransform.setContentXslTransform(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
			xslTransform.setVersion(version);
			xslTransform.setInsertionDate(date); 
			xslTransform.setLastUpdate(date); 
			hasBeenUpdated = xslTransformService.update(xslTransform);
		}
		
		if(hasBeenUpdated) {
			return new ResponseEntity<>(new XslTransformResponseDTO(getLogTraceInfo()), HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(new XslTransformResponseDTO(getLogTraceInfo()), HttpStatus.NO_CONTENT); 
		} 
		
	}
	

	@Override
	public ResponseEntity<XslTransformResponseDTO> deleteXslTransform(HttpServletRequest request, String templateIdRoot, String version) throws DocumentNotFoundException, OperationException {
		log.info(Constants.Logs.CALLED_API_DELETE_XSL_TRANSFORM); 
		boolean existsXslTransform = xslTransformService.delete(templateIdRoot, version); 	
		if(existsXslTransform) {
			return new ResponseEntity<>(new XslTransformResponseDTO(getLogTraceInfo()), HttpStatus.OK); 
		} else {
			return new ResponseEntity<>(new XslTransformResponseDTO(getLogTraceInfo()), HttpStatus.NOT_FOUND); 
		}
	} 
	
	@Override
	public ResponseEntity<XslTransformDTO> getXslTransformByTemplateIdRootAndVersion(HttpServletRequest request, 
			String templateIdRoot, String version) throws DocumentNotFoundException, OperationException {
		
		log.debug(Constants.Logs.CALLED_API_QUERY_ROOT_EXTENSION); 
		XslTransformDTO response =  xslTransformService.findByTemplateIdRootAndVersion(templateIdRoot, version); 		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	} 
	
	@Override
	public ResponseEntity<GetXsltResponseDTO> getXslTransform(HttpServletRequest request) throws OperationException {
		
		log.debug(Constants.Logs.CALLED_API_GET_XSL_TRANSFORM);  
		
		GetXsltResponseDTO response = new GetXsltResponseDTO(); 
		List<XslTransformDTO> responseArray = xslTransformService.findAll();
		List<GetXsltDTO> body = responseArray.stream().map(GetXsltDTO::fromDTO).collect(Collectors.toList());
		response.setBody(body);

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}  
	
	
	public static XslTransformBodyDTO getXsltJSONObject(String jsonREQ) {
		XslTransformBodyDTO out = null;
		if(!StringUtility.isNullOrEmpty(jsonREQ)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				out = mapper.readValue(jsonREQ, XslTransformBodyDTO.class);
			} catch (Exception ex) {
				log.error(Constants.Logs.ERROR_JSON_HANDLING, ex); 
			}
		}
		return out;
	
	}

	@Override
	public ResponseEntity<GetDocumentResDTO> getXslTransformById(HttpServletRequest request, String id) throws OperationException, DocumentNotFoundException {
		log.debug(Constants.Logs.CALLED_API_QUERY_ID); 
		XslTransformDocumentDTO doc =  xslTransformService.findById(id); 
		return new ResponseEntity<>(new GetDocumentResDTO(getLogTraceInfo(), doc), HttpStatus.OK);
	}
	
	
}
