package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDocumentDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.XSLTransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;

import java.util.List;

/**
    @author Riccardo Bonesi
 *
 *	XSL Transform interface service.
 */
public interface IXslTransformSRV extends IChangeSetSRV<XSLTransformCS> {

	/**
	 * Insert a XSL Transform document.
	 * @param ety
	 */
	void insert(XslTransformDTO ety) throws OperationException, DocumentAlreadyPresentException;

	/**
	 * Update the XSL Transform document.
	 * @param ety
	 * @return
	 */
	boolean update(XslTransformDTO ety) throws OperationException; 

	/**
	 * Insert all the XSLTs in the list.
	 * @param dtos
	 */
	void insertAll(List<XslTransformDTO> dtos) throws OperationException; 

	/**
	 * Delete a XSL Transform document.
	 * @param templateIdRoot
	 * @param version
	 * @return
	 */
	boolean delete(String templateIdRoot, String version) throws DocumentNotFoundException, OperationException; 
	
	/**
	 * Get the XSL Transform document by templateIdRoot and version.
	 * @param templateIdRoot
	 * @param version
	 * @return
	 */
	XslTransformDTO findByTemplateIdRootAndVersion(String templateIdRoot, String version) throws DocumentNotFoundException, OperationException; 

	/**
	 * Retrieves the XSLT by identifier
	 * @param id
	 * @return XslTransformDocumentDTO
	 * @throws OperationException
	 * @throws DocumentNotFoundException
	 */
	XslTransformDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException;

	/**
	 * Returns all XSLT
	 * @return List<XslTransformDTO>
	 */
	List<XslTransformDTO> findAll() throws OperationException; 
	
}
