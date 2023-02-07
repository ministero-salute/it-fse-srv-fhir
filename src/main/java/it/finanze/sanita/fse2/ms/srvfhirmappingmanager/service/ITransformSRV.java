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
	void insertTransformByComponents(String templateIdRoot, String version, String uri, MultipartFile file, FhirTypeEnum type) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, DocumentNotFoundException;

	/**
	 * Update a FHIR transform by request's components
	 * @throws InvalidVersionException
	 */
	void updateTransformByComponents(String templateIdRoot, String version, String uri, MultipartFile file, FhirTypeEnum type) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, DocumentNotFoundException, InvalidVersionException;

	/**
	 * Delete transform by templateIdRoot and version
	 *
	 * @param templateIdRoot
	 * @return
	 */
	void delete(String templateIdRoot) throws OperationException, DocumentNotFoundException;

	/**
	 * Find transform by templateIdRoot and version
	 * @param templateIdRoot
	 * @return
	 */
	TransformDTO findByTemplateIdRoot(String templateIdRoot) throws DocumentNotFoundException, OperationException;

	/**
	 * Find all transform saved on database
	 * @return
	 */
	List<TransformDTO> findAll(Options opts) throws OperationException;

	/**
	 * Find all active transform saved on database
	 * @return
	 */
	List<TransformDTO> findAllActive(Options opts) throws OperationException;

	/**
	 * Find transform by its id
	 * @param id
	 * @return
	 */
    TransformDTO findById(String id) throws OperationException, DocumentNotFoundException;

	List<TransformDTO> findByTemplateIdRootAndDeleted(String templateIdRoot, boolean deleted)
			throws DocumentNotFoundException, OperationException;
}
