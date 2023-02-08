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

	TransformETY insert(TransformETY ety) throws OperationException;

	List<TransformETY> remove(String uri) throws OperationException;

	TransformETY findByUri(String uri) throws OperationException;

	TransformETY findByTemplateIdRoot(String templateIdRoot) throws OperationException;

	List<TransformETY> findByTemplateIdRootAndDeleted(String templateIdRoot, boolean deleted) throws OperationException;

	List<TransformETY> findAll() throws OperationException;

    TransformETY findById(String id) throws OperationException;
}
