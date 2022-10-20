/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository;

import java.util.List;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;

/**
	@author Riccardo Bonesi
 *
 *	XSLT interface repository.
 */
public interface IXslTransformRepo extends IChangeSetRepo<XslTransformETY> {

	/**
	 * Insert a XSLT on database.
	 * 
	 * @param ety xslTransform to insert.
	 * @return xslTransform inserted.
	 */
	XslTransformETY insert(XslTransformETY ety) throws OperationException;
	
	/**
	 * Returns a xslTransform identified by its {@code templateIdRoot} and {@code version}.
	 * 
	 * @param templateIdRoot, version Primary key of the xslTransform to return.
	 * @return xslTransform identified by its {@code templateIdRoot} and {@code version}.
	 */
	XslTransformETY findByTemplateIdRootAndVersion(String templateIdRoot, String version) throws OperationException;

	/**
	 * Returns a xslTransform identified by its {@code id}.
	 * 
	 * @param id, Mongo ID, key of the xslTransform to return.
	 * @return xslTransform identified by its {@code id}.
	 */
	XslTransformETY findById(String id) throws OperationException;
	
	/**
	 * Deletes a xslTransform identified by its {@code template_id_root} and its {@code version}.
	 * 
	 * @param templateIdRoot, version Primary key of the xslTransform to return.
	 */
	boolean remove(String templateIdRoot, String version) throws OperationException;
	
	/**
	 * Insert all XSLTs on database.
	 * 
	 * @param etys List of XSLTs to insert.
	 */
	void insertAll(List<XslTransformETY> etys) throws OperationException;

	/**
	 * Returns all XSLTs.
	 * 
	 * @return List of all XSLTs.
	 */
	List<XslTransformETY> findAll() throws OperationException;

	/**
	 * Verify if a XSLT exists.
	 * @param templateIdRoot
	 * @return
	 * @throws OperationException
	 */
	boolean existByTemplateIdRoot(String templateIdRoot) throws OperationException;

}
