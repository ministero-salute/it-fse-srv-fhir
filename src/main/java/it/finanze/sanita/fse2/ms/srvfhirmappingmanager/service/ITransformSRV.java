/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.TransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.crud.base.CrudInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Transform interface service.
 */

public interface ITransformSRV extends IChangeSetSRV{

	/**
	 * Insert a FHIR transform using all components passed in the request
	 * @param body
	 * @param structureDefinitions
	 * @param maps
	 * @param valueSets
	 */
	CrudInfoDTO insertTransformByComponents(TransformBodyDTO body, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, DocumentNotFoundException;

	/**
	 * Update a FHIR transform by request's components
	 * @param body
	 * @param structureDefinitions
	 * @param maps
	 * @param valueSets
	 * @throws InvalidVersionException
	 */
	CrudInfoDTO updateTransformByComponents(TransformBodyDTO body, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, DocumentNotFoundException, InvalidVersionException;

	/**
	 * Delete transform by templateIdRoot and version
	 *
	 * @param templateIdRoot
	 * @return
	 */
	CrudInfoDTO delete(String templateIdRoot) throws OperationException, DocumentNotFoundException;

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
	List<TransformDTO> findAll(TransformDTO.Options opts) throws OperationException;

	/**
	 * Find all active transform saved on database
	 * @return
	 */
	List<TransformDTO> findAllActive(TransformDTO.Options opts) throws OperationException;

	/**
	 * Find transform by its id
	 * @param id
	 * @return
	 */
    TransformDTO findById(String id) throws OperationException, DocumentNotFoundException;

	List<TransformDTO> findByTemplateIdRootAndDeleted(String templateIdRoot, boolean deleted)
			throws DocumentNotFoundException, OperationException;
}
