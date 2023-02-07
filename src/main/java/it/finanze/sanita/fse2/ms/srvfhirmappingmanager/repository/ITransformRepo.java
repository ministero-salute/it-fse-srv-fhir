/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.FhirETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;

import java.util.List;


/**
 *	Transform interface repository.
 */
public interface ITransformRepo extends IChangeSetRepo<TransformETY> {

	/**
	 * Insert a Transform on database.
	 * 
	 * @param ety transform to insert.
	 * @return transform inserted.
	 */
	FhirETY insert(FhirETY ety) throws OperationException;

	/**
	 * Mark a transform as deleted on database
	 *
	 * @param templateIdRoot
	 * @return
	 * @throws OperationException
	 */
	List<FhirETY> remove(String templateIdRoot) throws OperationException;

	/**
	 * Find a transform by templateIdRoot
	 * @param templateIdRoot
	 * @return
	 * @throws OperationException
	 */
	FhirETY findByTemplateIdRoot(String templateIdRoot) throws OperationException;

	/**
	 * Find a transform by templateIdRoot
	 * @param templateIdRoot
	 * @param deleted
	 * @return
	 * @throws OperationException
	 */
	List<FhirETY> findByTemplateIdRootAndDeleted(String templateIdRoot, boolean deleted) throws OperationException;

	/**
	 * Find all active transforms (not marked deleted)
	 * @return
	 * @throws OperationException
	 */
	List<FhirETY> findAll() throws OperationException;

	/**
	 * Find a transform on database by id
	 * @param id
	 * @return
	 */
    FhirETY findById(String id) throws OperationException;
}
