/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDocumentDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.specs.XSLTransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.InvalidVersionException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.XslTransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.mongo.impl.XslTransformRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IXslTransformSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ChangeSetUtility;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility;
import lombok.extern.slf4j.Slf4j;


/**
 *
 *	XSL Transform service.
 */
@Service
@Slf4j
public class XsltransformSRV implements IXslTransformSRV {

	@Autowired
	private XslTransformRepo xslTransformRepo;
	
	@Override
	public void insert(final XslTransformDTO dto) throws OperationException, DocumentAlreadyPresentException {
		try {
			XslTransformETY ety = parseDtoToEty(dto); 
			
			XslTransformETY xslTransformIfPresent = xslTransformRepo.findByTemplateIdRootAndVersion(
						ety.getTemplateIdRoot(), ety.getVersion()); 
			
			if(xslTransformIfPresent != null) {
				log.error(Constants.Logs.ERROR_XSL_TRANSFORM_ALREADY_PRESENT);
				throw new DocumentAlreadyPresentException(Constants.Logs.ERROR_XSL_TRANSFORM_ALREADY_PRESENT); 
			}
			
			xslTransformRepo.insert(ety);
		} catch(MongoException ex) {
			log.error(Constants.Logs.ERROR_INSERT_XSL_TRANSFORM , ex);
			throw new OperationException(Constants.Logs.ERROR_INSERT_XSL_TRANSFORM , ex);
		}
		
	}
	
	@Override
	public void update(XslTransformDTO dto) throws OperationException, DocumentNotFoundException, InvalidVersionException {
		XslTransformETY ety = parseDtoToEty(dto);

		XslTransformETY lastXslt = xslTransformRepo.findByTemplateIdRoot(ety.getTemplateIdRoot());
		if (lastXslt != null) {
			if (ValidationUtility.isMajorVersion(dto.getVersion(), lastXslt.getVersion())) {
				xslTransformRepo.remove(ety.getTemplateIdRoot(), lastXslt.getVersion());
				xslTransformRepo.insert(ety);
			} else {
				throw new InvalidVersionException(String.format("Invalid version: %s. The version must be greater than %s", dto.getVersion(), lastXslt.getVersion()));
			}
		} else {
			throw new DocumentNotFoundException(String.format("Document with templateIdRoot: %s not found", dto.getTemplateIdRoot()));
		}
	}

	@Override
	public void insertAll(List<XslTransformDTO> dtos) throws OperationException {
			List<XslTransformETY> etyList;
			etyList = buildEtyFromDto(dtos);			
			xslTransformRepo.insertAll(etyList);
	}
	
	@Override
	public XslTransformDTO findByTemplateIdRootAndVersion(final String templateIdRoot, 
			final String version) throws DocumentNotFoundException, OperationException {
		
		XslTransformETY output = xslTransformRepo.findByTemplateIdRootAndVersion(templateIdRoot, version);

		if (output == null) {
            throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
        } 
		
		return parseEtyToDto(output);
	}

	@Override
	public XslTransformDocumentDTO findById(String id) throws OperationException, DocumentNotFoundException {

        XslTransformETY output = xslTransformRepo.findById(id);

        if (output == null) {
            throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
        }
		
		return XslTransformDocumentDTO.fromEntity(output);
	}

	@Override
	public boolean delete(String templateIdRoot, String version) throws DocumentNotFoundException, OperationException {
		try {
			return xslTransformRepo.remove(templateIdRoot, version); 
		} catch(MongoException e) {
			throw new OperationException(e.getMessage(), e);  
		}

	}
	
	@Override
	public List<XslTransformDTO> findAll() throws OperationException {
			List<XslTransformETY> etyList = xslTransformRepo.findAll(); 
			return buildDtoFromEty(etyList); 
	}
	
	
	public List<XslTransformDTO> buildDtoFromEty(List<XslTransformETY> xslTransformEtyList) {
		List<XslTransformDTO> output = new ArrayList<>();
		
		for(XslTransformETY xslTransform : xslTransformEtyList) {
			output.add(parseEtyToDto(xslTransform));
		}
		
		return output;
	} 
	
	private List<XslTransformETY> buildEtyFromDto(List<XslTransformDTO> xslTransformDtoList) {
		List<XslTransformETY> output = new ArrayList<>();
		
		for(XslTransformDTO xslTransform : xslTransformDtoList) {
			output.add(parseDtoToEty(xslTransform)); 
		}
		
		return output;
	}
	
	public XslTransformDTO parseEtyToDto(XslTransformETY xslTransformEty) {
		XslTransformDTO output = new XslTransformDTO();
		output.setId(xslTransformEty.getId());
		output.setNameXslTransform(xslTransformEty.getNameXslTransform()); 
		output.setContentXslTransform(xslTransformEty.getContentXslTransform()); 
		output.setTemplateIdRoot(xslTransformEty.getTemplateIdRoot());
		output.setVersion(xslTransformEty.getVersion());
		output.setInsertionDate(xslTransformEty.getInsertionDate()); 
		output.setDeleted(xslTransformEty.isDeleted()); 
		
		if(xslTransformEty.getLastUpdateDate() != null) {
			output.setLastUpdate(xslTransformEty.getLastUpdateDate()); 
		} 

		return output;
	}
	
	public XslTransformETY parseDtoToEty(XslTransformDTO xslTransformDto) {
		XslTransformETY output = new XslTransformETY();
		output.setId(xslTransformDto.getId());
		output.setNameXslTransform(xslTransformDto.getNameXslTransform()); 
		output.setContentXslTransform(xslTransformDto.getContentXslTransform()); 
		output.setVersion(xslTransformDto.getVersion());
		output.setTemplateIdRoot(xslTransformDto.getTemplateIdRoot());
		output.setInsertionDate(xslTransformDto.getInsertionDate()); 
		output.setDeleted(xslTransformDto.isDeleted()); 
		
		if(xslTransformDto.getLastUpdate() != null) {
			output.setLastUpdateDate(xslTransformDto.getLastUpdate()); 
		} 

		return output;
	}

	@Override
	public List<ChangeSetDTO<XSLTransformCS>> getInsertions(Date lastUpdate) throws OperationException {

		List<XslTransformETY> insertions;

		if (lastUpdate != null) {
			insertions = xslTransformRepo.getInsertions(lastUpdate);
		} else {
			insertions = xslTransformRepo.getEveryActiveDocument();
		}

		return insertions.stream().map(ChangeSetUtility::xslTransformToChangeset).collect(Collectors.toList());

	}

    @Override
	public List<ChangeSetDTO<XSLTransformCS>> getDeletions(Date lastUpdate) throws OperationException {
		try {

			List<ChangeSetDTO<XSLTransformCS>> deletions = new ArrayList<>();

			if (lastUpdate != null) {
				List<XslTransformETY> deletionsETY = xslTransformRepo.getDeletions(lastUpdate);
				deletions = deletionsETY.stream().map(ChangeSetUtility::xslTransformToChangeset)
						.collect(Collectors.toList());

			}

			return deletions;

		} catch (Exception e) {
			log.error("Error retrieving modifications", e);
			throw new BusinessException("Error retrieving modifications", e);
		}
	}



	
}
