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

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class TransformCTL extends AbstractCTL implements ITransformCTL {

	@Autowired
	private ITransformSRV transformSRV;

	@Override
	public PostDocsResDTO uploadTransform(String templateIdRoot, String version, MultipartFile file, String uri, FhirTypeEnum type) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException, InvalidContentException {

		transformSRV.insertTransformByComponents(templateIdRoot, version, uri, file, type);
		return new PostDocsResDTO(getLogTraceInfo(), 1);
	}

	@Override
	public PutDocsResDTO updateTransform(String templateIdRoot, String version, MultipartFile file, String uri, FhirTypeEnum type) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException, InvalidVersionException, InvalidContentException {
		transformSRV.updateTransformByComponents(templateIdRoot, version, uri, file, type);
		return new PutDocsResDTO(getLogTraceInfo(), 1);
	}

	@Override
	public DelDocsResDTO deleteTransform(String templateIdRoot) throws DocumentNotFoundException, OperationException {
		transformSRV.delete(templateIdRoot);
		return new DelDocsResDTO(getLogTraceInfo(), 1);
	}

	@Override
	public GetDocsResDTO getTransformByTemplateIdRootAndVersion(String templateIdRoot, boolean binary, boolean deleted) throws DocumentNotFoundException, OperationException {
		return new GetDocsResDTO(getLogTraceInfo(), transformSRV.findByTemplateIdRootAndDeleted(templateIdRoot, deleted), new Options(binary));
	}

	@Override
	public GetDocByIdResDTO getTransformById(String id) throws OperationException, DocumentNotFoundException {
		return new GetDocByIdResDTO(getLogTraceInfo(), transformSRV.findById(id));
	}

	@Override
	public GetDocsResDTO getTransform(boolean binary, boolean deleted) throws OperationException {
		Options opts = new Options(binary);
		// Retrieve data
		List<TransformDTO> items = deleted ? transformSRV.findAll(opts) : transformSRV.findAllActive(opts);
		// Transform and return
		return new GetDocsResDTO(getLogTraceInfo(), items, opts);
	}
	
}
