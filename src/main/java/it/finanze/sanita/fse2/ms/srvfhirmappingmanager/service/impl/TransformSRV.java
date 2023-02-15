/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants.Logs.*;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum.Map;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.API_PATH_ROOTS_VAR;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.StringUtility.*;


/**
 *	Transform service.
 */
@Service
@Slf4j
public class TransformSRV implements ITransformSRV {
	@Autowired
	private ITransformRepo repository;

	@Override
	public void insertTransformByComponents(List<String> root, String version, String uri, MultipartFile file, FhirTypeEnum type) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, InvalidContentException {
		log.debug("[EDS] Insertion of transform - START");
		// Retrieve most recent one
		TransformETY exists = repository.findByUri(uri);
		// Verify it doesn't exist
		if (exists != null) throw new DocumentAlreadyPresentException(ERR_SRV_DOC_ALREADY_EXIST);
		// Check if root values are available
		if (type == Map && !isNullOrEmpty(root)) {
			// Normalize (remove whitespaces and null objects)
			root = normalize(root);
			// Check for empty items
			if(isEmptyOrHasEmptyItems(root)) throw new InvalidContentException(ERR_SRV_ROOT_ITEMS_INVALID, API_PATH_ROOTS_VAR);
			// Iterate for each root
			for (String s : root) {
				// Retrieve most recent one
				TransformETY id = repository.findByTemplateIdRoot(s);
				// It must be null otherwise is already used by some other resource
				if (id != null) throw new DocumentAlreadyPresentException(ERR_SRV_ROOT_ALREADY_EXIST);
			}
		}
		// Convert from DTO to entity
		TransformETY out = TransformETY.fromComponents(uri, version, root, type, file);
		// Save
		repository.insert(out);
	}

	@Override
	public void updateTransformByComponents(String version, String uri, MultipartFile file) throws OperationException, DataProcessingException, DocumentNotFoundException, InvalidVersionException {
		log.debug("[EDS] Update of transform - START");
		// Retrieve most recent one
		TransformETY latest = repository.findByUri(uri);
		// Verify it exists
		if (latest == null) throw new DocumentNotFoundException(ERR_SRV_DOC_NOT_EXIST);
		// Check provided version is greater than the latest one
		if (!ValidationUtility.isMajorVersion(version, latest.getVersion())) {
			throw new InvalidVersionException(String.format(ERR_SRV_VERSION_MISMATCH, version, latest.getVersion()));
		}
		// Mark as deleted the current file
		repository.remove(uri);
		// Insert the new one
		repository.insert(TransformETY.fromComponents(uri, version, latest.getTemplateIdRoot(), latest.getType(), file));
	}

	@Override
	public void delete(String uri) throws OperationException, DocumentNotFoundException {
		List<TransformETY> out = repository.remove(uri);
		if (out == null || out.isEmpty()) throw new DocumentNotFoundException(ERR_SRV_DOC_NOT_EXIST);
	}

	
	@Override
	public List<TransformDTO> findByUriAndDeleted(final String uri, boolean deleted) throws DocumentNotFoundException, OperationException {
		List<TransformETY> entities = repository.findByUriAndDeleted(uri, deleted);
		if(entities.isEmpty()) throw new DocumentNotFoundException(ERR_SRV_DOC_NOT_EXIST);
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
		if (output == null) throw new DocumentNotFoundException(ERR_SRV_DOC_NOT_EXIST);
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
