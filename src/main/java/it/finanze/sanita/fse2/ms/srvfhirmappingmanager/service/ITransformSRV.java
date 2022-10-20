/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.specs.TransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.TransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.InvalidVersionException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Transform interface service.
 */

public interface ITransformSRV extends IChangeSetSRV<TransformCS>{

	/**
	 * Insert a FHIR transform using all components passed in the request
	 * @param body
	 * @param structureDefinitions
	 * @param maps
	 * @param valueSets
	 */
	Map<String,Integer> insertTransformByComponents(TransformBodyDTO body, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, DocumentNotFoundException;

	/**
	 * Update a FHIR transform by request's components
	 * @param body
	 * @param structureDefinitions
	 * @param maps
	 * @param valueSets
	 * @throws InvalidVersionException
	 */
	Map<String,Integer> updateTransformByComponents(TransformBodyDTO body, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, DocumentNotFoundException, InvalidVersionException;

	/**
	 * Delete transform by templateIdRoot and version
	 *
	 * @param templateIdRoot
	 * @param version
	 * @return
	 */
	Map<String, Integer> delete(String templateIdRoot, String version) throws OperationException, DocumentNotFoundException;

	/**
	 * Find transform by templateIdRoot and version
	 * @param templateIdRoot
	 * @param version
	 * @return
	 */
	TransformDTO findByTemplateIdRootAndVersion(String templateIdRoot, String version) throws DocumentNotFoundException, OperationException;

	/**
	 * Find all transform saved on database
	 * @return
	 */
	List<TransformDTO> findAll() throws OperationException;

	/**
	 * Find all active transform saved on database
	 * @return
	 */
	List<TransformDTO> findAllActive() throws OperationException;

	/**
	 * Find transform by its id
	 * @param id
	 * @return
	 */
    TransformDTO findById(String id) throws OperationException, DocumentNotFoundException;
}
