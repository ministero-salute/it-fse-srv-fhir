/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
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
	TransformETY insert(TransformETY ety) throws OperationException;

	/**
	 * Mark a transform as deleted on database
	 *
	 * @param templateIdRoot
	 * @return
	 * @throws OperationException
	 */
	List<TransformETY> remove(String templateIdRoot) throws OperationException;

	/**
	 * Find a transform by templateIdRoot
	 * @param templateIdRoot
	 * @return
	 * @throws OperationException
	 */
	TransformETY findByTemplateIdRoot(String templateIdRoot) throws OperationException;

	/**
	 * Find a transform by templateIdRoot
	 * @param templateIdRoot
	 * @param deleted
	 * @return
	 * @throws OperationException
	 */
	List<TransformETY> findByTemplateIdRootAndDeleted(String templateIdRoot, boolean deleted) throws OperationException;

	/**
	 * Find all active transforms (not marked deleted)
	 * @return
	 * @throws OperationException
	 */
	List<TransformETY> findAll() throws OperationException;

	/**
	 * Find a transform on database by id
	 * @param id
	 * @return
	 */
    TransformETY findById(String id) throws OperationException;
}
