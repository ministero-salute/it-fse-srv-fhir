/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.ITransformCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.TransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.PutDocsResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.base.CrudInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.GetDocumentsResDTO;
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
	public PostDocsResDTO uploadTransform(String rootMapIdentifier, String templateIdRoot, String version, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException, InvalidContentException {
		if (!validateFiles(maps)) throw new InvalidContentException("One or more files appear to be empty");
		TransformBodyDTO transformBodyDTO = TransformBodyDTO.builder()
						.templateIdRoot(templateIdRoot)
						.version(version)
						.rootMapIdentifier(rootMapIdentifier).build();
		CrudInfoDTO output = transformSRV.insertTransformByComponents(transformBodyDTO, structureDefinitions, maps, valueSets);
		return new PostDocsResDTO(getLogTraceInfo(), output);
	}

	@Override
	public PutDocsResDTO updateTransform(String rootMapIdentifier, String templateIdRoot, String version, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException, InvalidVersionException, InvalidContentException {
		if (!validateFiles(maps)) throw new InvalidContentException("One or more files appear to be empty");
		TransformBodyDTO transformBodyDTO = TransformBodyDTO.builder()
				.templateIdRoot(templateIdRoot)
				.version(version)
				.rootMapIdentifier(rootMapIdentifier).build();
		CrudInfoDTO output = transformSRV.updateTransformByComponents(transformBodyDTO, structureDefinitions, maps, valueSets);
		return new PutDocsResDTO(getLogTraceInfo(), output);
	}

	@Override
	public DelDocsResDTO deleteTransform(String templateIdRoot) throws DocumentNotFoundException, OperationException {
		return new DelDocsResDTO(getLogTraceInfo(), transformSRV.delete(templateIdRoot));
	}

	@Override
	public TransformDTO getTransformByTemplateIdRootAndVersion(String templateIdRoot) throws DocumentNotFoundException, OperationException {
		return transformSRV.findByTemplateIdRoot(templateIdRoot);
	}

	@Override
	public TransformDTO getTransformById(String id) throws OperationException, DocumentNotFoundException {
		return transformSRV.findById(id);
	}

	@Override
	public GetDocumentsResDTO getTransform(boolean binary, boolean deleted) throws OperationException {
		// Create options
		TransformDTO.Options opts = new TransformDTO.Options(binary);
		// Retrieve data
		List<TransformDTO> items = deleted ? transformSRV.findAll(opts) : transformSRV.findAllActive(opts);
		// Transform and return
		return new GetDocumentsResDTO(getLogTraceInfo(), items);
	}
	
}
