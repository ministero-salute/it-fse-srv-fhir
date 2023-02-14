/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO.Options;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Transform interface service.
 */

public interface ITransformSRV extends IChangeSetSRV{

	/**
	 * Insert a FHIR transform using all components passed in the request
	 */
	void insertTransformByComponents(
		List<String> root,
		String version,
		String uri,
		MultipartFile file,
		FhirTypeEnum type
	) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, InvalidContentException;


	void updateTransformByComponents(
		String version,
		String uri,
		MultipartFile file
	) throws OperationException, DataProcessingException, DocumentNotFoundException, InvalidVersionException;

	void delete(String templateIdRoot) throws OperationException, DocumentNotFoundException;

	List<TransformDTO> findAll(Options opts) throws OperationException;

	List<TransformDTO> findAllActive(Options opts) throws OperationException;

    TransformDTO findById(String id) throws OperationException, DocumentNotFoundException;

	List<TransformDTO> findByUriAndDeleted(String uri, boolean deleted) throws DocumentNotFoundException, OperationException;
}
