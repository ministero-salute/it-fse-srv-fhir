/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import com.mongodb.MongoException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.FhirDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.FhirDTO.Options;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.ITransformRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.FhirETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ChangeSetUtility;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 *	Transform service.
 */
@Service
@Slf4j
public class TransformSRV implements ITransformSRV {
	@Autowired
	private ITransformRepo repository;

	@Override
	public void insertTransformByComponents(String templateIdRoot, String version, String uri, MultipartFile file, FhirTypeEnum type) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, DocumentNotFoundException {
		log.debug("[EDS] Insertion of transform - START");
		try {
			FhirETY existingDocument = repository.findByTemplateIdRoot(templateIdRoot);
			if (existingDocument != null) {
				throw new DocumentAlreadyPresentException(Constants.Logs.ERROR_DOCUMENT_ALREADY_EXIST);
			}

			// Insert rootMapName at root level of ety
			FhirETY transformETY = FhirETY.fromComponents(templateIdRoot, version, uri, file, type);
			repository.insert(transformETY);
			
		} catch (MongoException ex) {
			log.error(Constants.Logs.ERROR_INSERT_TRANSFORM , ex);
			throw new OperationException(Constants.Logs.ERROR_INSERT_TRANSFORM , ex);
		}
	}

	@Override
	public void updateTransformByComponents(String templateIdRoot, String version, String uri, MultipartFile file, FhirTypeEnum type) throws OperationException, DataProcessingException, DocumentNotFoundException, InvalidVersionException {
		log.debug("[EDS] Update of transform - START");
		FhirETY lastDocument = repository.findByTemplateIdRoot(templateIdRoot);

		if (ObjectUtils.anyNull(lastDocument) || ObjectUtils.isEmpty(lastDocument)) {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		}

		if (!ValidationUtility.isMajorVersion(version, lastDocument.getVersion())) {
			throw new InvalidVersionException(String.format("Invalid version: %s. The version must be greater than %s", version, lastDocument.getVersion()));
		}

		delete(lastDocument.getTemplateIdRoot());
		// Insert rootMapName at root level of ety
		FhirETY transformETY = FhirETY.fromComponents(templateIdRoot, version, uri, file, type);
		repository.insert(transformETY);
		
	}

	@Override
	public void delete(String templateIdRoot) throws OperationException, DocumentNotFoundException {
		try {
			List<FhirETY> deletedTransform = repository.remove(templateIdRoot);
			if (CollectionUtils.isEmpty(deletedTransform)) {
				throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
			}
		} catch(MongoException e) {
			throw new OperationException(e.getMessage(), e);
			}
	}

	
	@Override
	public List<FhirDTO> findByTemplateIdRootAndDeleted(final String templateIdRoot, boolean deleted) throws DocumentNotFoundException, OperationException {
		List<FhirETY> entities = repository.findByTemplateIdRootAndDeleted(templateIdRoot, deleted);
		if(entities.isEmpty()) throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		return entities.stream().map(FhirDTO::fromEntity).collect(Collectors.toList());
	}
	
	@Override
	public FhirDTO findByTemplateIdRoot(final String templateIdRoot) throws DocumentNotFoundException, OperationException {
		FhirETY output = repository.findByTemplateIdRoot(templateIdRoot);

		if (ObjectUtils.anyNull(output) || ObjectUtils.isEmpty(output)) {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		}

		return FhirDTO.fromEntity(output);

	}


	@Override
	public List<FhirDTO> findAll(Options opts) throws OperationException {
		return repository.findAll()
			.stream()
			.map(FhirDTO::fromEntity)
			.map(i -> i.applyOptions(opts))
			.collect(Collectors.toList());
	}

	@Override
	public List<FhirDTO> findAllActive(Options opts) throws OperationException {
		return repository.getEveryActiveDocument()
			.stream()
			.map(FhirDTO::fromEntity)
			.map(i -> i.applyOptions(opts))
			.collect(Collectors.toList());
	}

	@Override
	public FhirDTO findById(String id) throws OperationException, DocumentNotFoundException {
		FhirETY output = repository.findById(id);

		if (ObjectUtils.anyNull(output) || ObjectUtils.isEmpty(output)) {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		}

		return FhirDTO.fromEntity(output);
	}

	@Override
	public List<ChangeSetDTO> getInsertions(Date lastUpdate) throws OperationException {
		List<FhirETY> insertions;
		if (lastUpdate != null) {
			insertions = repository.getInsertions(lastUpdate);
		} else {
			insertions = repository.getEveryActiveDocument();
		}
		return insertions.stream().map(ChangeSetUtility::transformToChangeset).collect(Collectors.toList());
	}

	@Override
	public List<ChangeSetDTO> getDeletions(Date lastUpdate) throws OperationException {
		try {
			List<ChangeSetDTO> deletions = new ArrayList<>();

			if (lastUpdate != null) {
				List<FhirETY> deletionsETY = repository.getDeletions(lastUpdate);
				deletions = deletionsETY.stream().map(ChangeSetUtility::transformToChangeset)
						.collect(Collectors.toList());
			}
			return deletions;
		} catch (Exception e) {
			log.error("Error retrieving modifications", e);
			throw new BusinessException("Error retrieving modifications", e);
		}
	}

	@Override
	public long getCollectionSize() throws OperationException {
		return repository.getActiveDocumentCount();
	}
}
