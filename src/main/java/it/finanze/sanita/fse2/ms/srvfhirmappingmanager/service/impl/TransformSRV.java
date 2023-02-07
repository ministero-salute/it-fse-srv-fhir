/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import com.mongodb.MongoException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformDTO.Options;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.ITransformRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
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

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs.*;


/**
 *	Transform service.
 */
@Service
@Slf4j
public class TransformSRV implements ITransformSRV {
	@Autowired
	private ITransformRepo repository;

	@Override
	public void insertTransformByComponents(String root, String version, String uri, MultipartFile file, FhirTypeEnum type) throws DocumentAlreadyPresentException, OperationException, DataProcessingException {
		log.debug("[EDS] Insertion of transform - START");
		// Retrieve most recent one
		TransformETY exists = repository.findByUri(uri);
		// Verify it doesn't exist
		if (exists != null) throw new DocumentAlreadyPresentException(ERR_SRV_DOC_ALREADY_EXIST);
		// Check root id is available if provided
		if (type == FhirTypeEnum.Map && root != null && !root.isEmpty()) {
			// Retrieve most recent one
			TransformETY id = repository.findByTemplateIdRoot(root);
			// It must be null otherwise is already used by some other resource
			if (id != null) throw new DocumentAlreadyPresentException(ERR_SRV_ROOT_ALREADY_EXIST);
		}
		// Convert from DTO to entity
		TransformETY out = TransformETY.fromComponents(uri, version, root, type, file);
		// Save
		repository.insert(out);
	}

	@Override
	public void updateTransformByComponents(String templateIdRoot, String version, String uri, MultipartFile file, FhirTypeEnum type) throws OperationException, DataProcessingException, DocumentNotFoundException, InvalidVersionException {
		log.debug("[EDS] Update of transform - START");
		TransformETY lastDocument = repository.findByUri(templateIdRoot);

		if (ObjectUtils.anyNull(lastDocument) || ObjectUtils.isEmpty(lastDocument)) {
			throw new DocumentNotFoundException(ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		}

		if (!ValidationUtility.isMajorVersion(version, lastDocument.getVersion())) {
			throw new InvalidVersionException(String.format("Invalid version: %s. The version must be greater than %s", version, lastDocument.getVersion()));
		}

		delete(lastDocument.getTemplateIdRoot());
		// Insert rootMapName at root level of ety
		TransformETY transformETY = TransformETY.fromComponents(templateIdRoot, version, uri, type, file);
		repository.insert(transformETY);
		
	}

	@Override
	public void delete(String templateIdRoot) throws OperationException, DocumentNotFoundException {
		try {
			List<TransformETY> deletedTransform = repository.remove(templateIdRoot);
			if (CollectionUtils.isEmpty(deletedTransform)) {
				throw new DocumentNotFoundException(ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
			}
		} catch(MongoException e) {
			throw new OperationException(e.getMessage(), e);
			}
	}

	
	@Override
	public List<TransformDTO> findByTemplateIdRootAndDeleted(final String templateIdRoot, boolean deleted) throws DocumentNotFoundException, OperationException {
		List<TransformETY> entities = repository.findByTemplateIdRootAndDeleted(templateIdRoot, deleted);
		if(entities.isEmpty()) throw new DocumentNotFoundException(ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		return entities.stream().map(TransformDTO::fromEntity).collect(Collectors.toList());
	}

	@Override
	public List<TransformDTO> findAll(Options opts) throws OperationException {
		return repository.findAll()
			.stream()
			.map(TransformDTO::fromEntity)
			.map(i -> i.applyOptions(opts))
			.collect(Collectors.toList());
	}

	@Override
	public List<TransformDTO> findAllActive(Options opts) throws OperationException {
		return repository.getEveryActiveDocument()
			.stream()
			.map(TransformDTO::fromEntity)
			.map(i -> i.applyOptions(opts))
			.collect(Collectors.toList());
	}

	@Override
	public TransformDTO findById(String id) throws OperationException, DocumentNotFoundException {
		TransformETY output = repository.findById(id);

		if (ObjectUtils.anyNull(output) || ObjectUtils.isEmpty(output)) {
			throw new DocumentNotFoundException(ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		}

		return TransformDTO.fromEntity(output);
	}

	@Override
	public List<ChangeSetDTO> getInsertions(Date lastUpdate) throws OperationException {
		List<TransformETY> insertions;
		if (lastUpdate != null) {
			insertions = repository.getInsertions(lastUpdate);
		} else {
			insertions = repository.getEveryActiveDocument();
		}
		return insertions.stream().map(ChangeSetUtility::transformToChangeset).collect(Collectors.toList());
	}

	@Override
	public List<ChangeSetDTO> getDeletions(Date lastUpdate) throws OperationException {
		List<ChangeSetDTO> deletions = new ArrayList<>();
		if (lastUpdate != null) {
			List<TransformETY> deletionsETY = repository.getDeletions(lastUpdate);
			deletions = deletionsETY.stream().map(ChangeSetUtility::transformToChangeset)
					.collect(Collectors.toList());
		}
		return deletions;
	}

	@Override
	public long getCollectionSize() throws OperationException {
		return repository.getActiveDocumentCount();
	}
}
