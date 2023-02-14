/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.ITransformCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO.Options;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.data.GetDocByIdResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.GetDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.PutDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs.ERR_VAL_FILES_INVALID;

@RestController
@Slf4j
public class TransformCTL extends AbstractCTL implements ITransformCTL {

	@Autowired
	private ITransformSRV service;

	@Override
	public PostDocsResDTO uploadTransform(String uri, String version, FhirTypeEnum type, List<String> root, MultipartFile file) throws OperationException, DocumentAlreadyPresentException, InvalidContentException, DataProcessingException {
		if (!isValidFile(file)) throw new InvalidContentException(ERR_VAL_FILES_INVALID);
		service.insertTransformByComponents(root, version, uri, file, type);
		return new PostDocsResDTO(getLogTraceInfo(), 1);
	}

	@Override
	public PutDocsResDTO updateTransform(String uri, String version, MultipartFile file) throws OperationException, DocumentNotFoundException, InvalidVersionException, InvalidContentException, DataProcessingException {
		if (!isValidFile(file)) throw new InvalidContentException(ERR_VAL_FILES_INVALID);
		service.updateTransformByComponents(version, uri, file);
		return new PutDocsResDTO(getLogTraceInfo(), 1);
	}

	@Override
	public DelDocsResDTO deleteTransform(String uri) throws DocumentNotFoundException, OperationException {
		service.delete(uri);
		return new DelDocsResDTO(getLogTraceInfo(), 1);
	}

	@Override
	public GetDocsResDTO getTransformByUri(String uri, boolean binary, boolean deleted) throws DocumentNotFoundException, OperationException {
		return new GetDocsResDTO(getLogTraceInfo(), service.findByUriAndDeleted(uri, deleted), new Options(binary));
	}

	@Override
	public GetDocByIdResDTO getTransformById(String id) throws OperationException, DocumentNotFoundException {
		return new GetDocByIdResDTO(getLogTraceInfo(), service.findById(id));
	}

	@Override
	public GetDocsResDTO getTransform(boolean binary, boolean deleted) throws OperationException {
		Options opts = new Options(binary);
		// Retrieve data
		List<TransformDTO> items = deleted ? service.findAll(opts) : service.findAllActive(opts);
		// Transform and return
		return new GetDocsResDTO(getLogTraceInfo(), items, opts);
	}
	
}
