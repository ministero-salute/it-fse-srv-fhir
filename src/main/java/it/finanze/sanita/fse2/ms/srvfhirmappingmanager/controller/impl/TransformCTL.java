/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.ITransformCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.TransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformUploadResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.InvalidContentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.InvalidVersionException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TransformCTL extends AbstractCTL implements ITransformCTL {

	@Autowired
	private transient ITransformSRV transformSRV;

	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = -3766806530281454403L;


	@Override
	public ResponseEntity<TransformUploadResponseDTO> uploadTransform(HttpServletRequest request, String rootMapIdentifier, String templateIdRoot, String version, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException, InvalidContentException {
		log.debug(Constants.Logs.CALLED_API_UPLOAD_TRANSFORM);
		if (validateFiles(structureDefinitions) && validateFiles(maps) && validateFiles(valueSets)) {
			TransformBodyDTO transformBodyDTO = TransformBodyDTO.builder()
							.templateIdRoot(templateIdRoot)
							.version(version)
							.rootMapIdentifier(rootMapIdentifier).build();
			Map<String,Integer> output = transformSRV.insertTransformByComponents(transformBodyDTO, structureDefinitions, maps, valueSets);
			return new ResponseEntity<>(new TransformUploadResponseDTO(getLogTraceInfo(), output), HttpStatus.CREATED);
		} else {
			throw new InvalidContentException("One or more files appear to be empty");
		}
	}

	@Override
	public ResponseEntity<TransformUploadResponseDTO> updateTransform(HttpServletRequest request, String rootMapIdentifier, String templateIdRoot, String version, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException, InvalidVersionException, InvalidContentException {
		log.debug(Constants.Logs.CALLED_API_UPDATE_TRANSFORM);
		if (validateFiles(structureDefinitions) && validateFiles(maps) && validateFiles(valueSets)) {
			TransformBodyDTO transformBodyDTO = TransformBodyDTO.builder()
					.templateIdRoot(templateIdRoot)
					.version(version)
					.rootMapIdentifier(rootMapIdentifier).build();
			Map<String,Integer> output = transformSRV.updateTransformByComponents(transformBodyDTO, structureDefinitions, maps, valueSets);
			return new ResponseEntity<>(new TransformUploadResponseDTO(getLogTraceInfo(), output), HttpStatus.OK);
		} else {
			throw new InvalidContentException("One or more files appear to be empty");
		}
	}

	@Override
	public ResponseEntity<TransformUploadResponseDTO> deleteTransform(HttpServletRequest request, String templateIdRoot) throws DocumentNotFoundException, OperationException {
		log.debug(Constants.Logs.CALLED_API_DELETE_TRANSFORM);
		Map<String, Integer> existsTransform = transformSRV.delete(templateIdRoot);
		return new ResponseEntity<>(new TransformUploadResponseDTO(getLogTraceInfo(), existsTransform), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<TransformDTO> getTransformByTemplateIdRootAndVersion(HttpServletRequest request, String templateIdRoot, String version) throws DocumentNotFoundException, OperationException {
		log.info(Constants.Logs.CALLED_API_QUERY_TRANSFORM_ROOT_EXTENSION);
		TransformDTO response =  transformSRV.findByTemplateIdRootAndVersion(templateIdRoot, version);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<TransformDTO> getTransformById(HttpServletRequest request, String id) throws OperationException, DocumentNotFoundException {
		log.info(Constants.Logs.CALLED_API_GET_TRANSFORM);
		TransformDTO response = transformSRV.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<List<TransformDTO>> getTransform(HttpServletRequest request) throws OperationException {
		log.info(Constants.Logs.CALLED_API_GET_ALL_TRANSFORM);
		List<TransformDTO> response = transformSRV.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<List<TransformDTO>> getActiveTransform(HttpServletRequest request) throws OperationException {
		log.info(Constants.Logs.CALLED_API_GET_ACTIVE_TRANSFORM);
		List<TransformDTO> response = transformSRV.findAllActive();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
