package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.MongoException;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.TransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.DefinitionDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.MapDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ValuesetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.specs.TransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.ITransformRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureDefinition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureMap;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.StructureValueset;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IDefinitionSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IMapSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IValuesetSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ChangeSetUtility;
import lombok.extern.slf4j.Slf4j;


/**
 *	Transform service.
 */
@Service
@Slf4j
public class TransformSRV implements ITransformSRV {

	@Autowired
	private IDefinitionSRV definitionSRV;

	@Autowired
	private IValuesetSRV valuesetSRV;

	@Autowired
	private IMapSRV mapSRV;

	@Autowired
	private ITransformRepo transformRepo;

	@Override
	public Map<String,Integer> insertTransformByComponents(TransformBodyDTO body, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, DocumentNotFoundException {
		log.debug("[EDS] Insertion of transform - START");
		Map<String,Integer> output = new LinkedHashMap<>();
		try {
			TransformETY existingDocument = transformRepo.findByTemplateIdRootAndVersion(body.getTemplateIdRoot(), body.getVersion());
			if (existingDocument != null) {
				throw new DocumentAlreadyPresentException(Constants.Logs.ERROR_DOCUMENT_ALREADY_EXIST);
			}

			String rootMapFileName = FilenameUtils.removeExtension(body.getRootMapIdentifier());
			Map<String, StructureMap> mapsToInsert = mapSRV.createMaps(rootMapFileName, maps);
			Map<String, StructureDefinition> definitionsToInsert = definitionSRV.createDefinitions(null, structureDefinitions);
			Map<String, StructureValueset> valuesetsToInsert = valuesetSRV.createValuesets(valueSets);

			// Insert rootMapName at root level of ety
			TransformETY transformETY = TransformETY.fromComponents(body.getTemplateIdRoot(), body.getVersion(), rootMapFileName,
					new ArrayList<>(mapsToInsert.values()), new ArrayList<>(definitionsToInsert.values()), new ArrayList<>(valuesetsToInsert.values()));
			transformRepo.insert(transformETY);
			
			output.put("insertedMaps", mapsToInsert.values().size());
			output.put("insertedDefinitions", definitionsToInsert.values().size());
			output.put("insertedValuesets", valuesetsToInsert.values().size());

		} catch (MongoException ex) {
			log.error(Constants.Logs.ERROR_INSERT_TRANSFORM , ex);
			throw new OperationException(Constants.Logs.ERROR_INSERT_TRANSFORM , ex);
		}
		return output;
	}

	@Override
	public Map<String, Integer> updateTransformByComponents(TransformBodyDTO body, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws DocumentAlreadyPresentException, OperationException, DataProcessingException, DocumentNotFoundException {
		log.debug("[EDS] Update of transform - START");
		Map<String,Integer> output = new LinkedHashMap<>();
		try {
			TransformETY documentToUpdate = transformRepo.findByTemplateIdRoot(body.getTemplateIdRoot());

			if (ObjectUtils.anyNull(documentToUpdate) || ObjectUtils.isEmpty(documentToUpdate)) {
				throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
			}

			if (documentToUpdate.getVersion().equals(body.getVersion())) {
				throw new DocumentAlreadyPresentException(Constants.Logs.ERROR_DOCUMENT_ALREADY_EXIST);
			}

			String currentRoot = documentToUpdate.getRootStructureMap();
			Map<String, StructureMap> mapsToUpdate = mapSRV.updateMaps(documentToUpdate.getStructureMaps(), maps);
			Map<String, StructureDefinition> definitionsToUpdate = definitionSRV.updateValuesets(null, documentToUpdate.getStructureDefinitions(), structureDefinitions);
			Map<String, StructureValueset> valuesetsToUpdate = valuesetSRV.updateValuesets(documentToUpdate.getStructureValuesets(), valueSets);

			// Check if root map is to be replaced
			String rootMapFileName = FilenameUtils.removeExtension(body.getRootMapIdentifier());
			if (!StringUtils.isEmpty(rootMapFileName) && mapsToUpdate.containsKey(rootMapFileName)) {
				currentRoot = rootMapFileName;
			}

			TransformETY transformETY = TransformETY.fromComponents(body.getTemplateIdRoot(), body.getVersion(),
					currentRoot, new ArrayList<>(mapsToUpdate.values()), new ArrayList<>(definitionsToUpdate.values()), new ArrayList<>(valuesetsToUpdate.values()));
			transformRepo.insert(transformETY);

			output.put("updatedMaps", structureDefinitions.length);
			output.put("updatedDefinitions", maps.length);
			output.put("updatedValuesets", valueSets.length);

			return output;
		} catch (MongoException ex) {
			log.error(Constants.Logs.ERROR_UPDATE_TRANSFORM , ex);
			throw new OperationException(Constants.Logs.ERROR_UPDATE_TRANSFORM , ex);
		}
	}

	@Override
	public boolean delete(String templateIdRoot, String version) throws OperationException {
		try {
			return transformRepo.remove(templateIdRoot, version);
		} catch(MongoException e) {
			throw new OperationException(e.getMessage(), e);
		}
	}

	@Override
	public TransformDTO findByTemplateIdRootAndVersion(final String templateIdRoot, final String version) throws DocumentNotFoundException, OperationException {
		TransformETY output = transformRepo.findByTemplateIdRootAndVersion(templateIdRoot, version);

		if (ObjectUtils.anyNull(output) || ObjectUtils.isEmpty(output)) {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		}

		return parseEtyToDto(output);
	}

	@Override
	public List<TransformDTO> findAll() throws OperationException {
		java.util.Map<String, TransformDTO> transforms = new HashMap<>();
		List<TransformETY> entities = transformRepo.findAll();

		for (TransformETY transformETY : entities) {
			transforms.put(transformETY.getId(), this.parseEtyToDto(transformETY));
		}

		return new ArrayList<>(transforms.values());
	}

	@Override
	public List<TransformDTO> findAllActive() throws OperationException {
		java.util.Map<String, TransformDTO> transforms = new HashMap<>();
		List<TransformETY> entities = transformRepo.getEveryActiveDocument();

		for (TransformETY transformETY : entities) {
			transforms.put(transformETY.getId(), this.parseEtyToDto(transformETY));
		}

		return new ArrayList<>(transforms.values());
	}

	@Override
	public TransformDTO findById(String id) throws OperationException, DocumentNotFoundException {
		TransformETY output = transformRepo.findById(id);

		if (ObjectUtils.anyNull(output) || ObjectUtils.isEmpty(output)) {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		}

		return parseEtyToDto(output);
	}

	private TransformDTO parseEtyToDto(TransformETY output) {
		Date startDate = new Date();
		TransformDTO transformDTO = new TransformDTO();

		java.util.Map<String, DefinitionDTO> definitions = new HashMap<>();
		java.util.Map<String, MapDTO> maps = new HashMap<>();
		java.util.Map<String, ValuesetDTO> valuesets = new HashMap<>();

		for (StructureDefinition structureDefinition : output.getStructureDefinitions()) {
			definitions.put(structureDefinition.getFilenameDefinition(), DefinitionDTO.fromEntity(structureDefinition));
		}
		for (StructureMap structureMap : output.getStructureMaps()) {
			maps.put(structureMap.getFilenameMap(), MapDTO.fromEntity(structureMap));
		}
		for (StructureValueset structureValueset : output.getStructureValuesets()) {
			valuesets.put(structureValueset.getFilenameValueset(), ValuesetDTO.fromEntity(structureValueset));
		}

		transformDTO.setId(output.getId());
		transformDTO.setRootMap(output.getRootStructureMap());
		transformDTO.setInsertionDate(output.getInsertionDate());
		transformDTO.setLastUpdateDate(output.getLastUpdateDate());
		transformDTO.setVersion(output.getVersion());
		transformDTO.setTemplateIdRoot(output.getTemplateIdRoot());
		transformDTO.setDeleted(output.isDeleted());
		transformDTO.setDefinitions(new ArrayList<>(definitions.values()));
		transformDTO.setMaps(new ArrayList<>(maps.values()));
		transformDTO.setValuesets(new ArrayList<>(valuesets.values()));

		Date endDate = new Date();
		log.debug("diff: {}", endDate.getTime() - startDate.getTime());

		return transformDTO;
	}

	@Override
	public List<ChangeSetDTO<TransformCS>> getInsertions(Date lastUpdate) throws OperationException {
		List<TransformETY> insertions;
		if (lastUpdate != null) {
			insertions = transformRepo.getInsertions(lastUpdate);
		} else {
			insertions = transformRepo.getEveryActiveDocument();
		}
		return insertions.stream().map(ChangeSetUtility::transformToChangeset).collect(Collectors.toList());
	}

	@Override
	public List<ChangeSetDTO<TransformCS>> getDeletions(Date lastUpdate) throws OperationException {
		try {
			List<ChangeSetDTO<TransformCS>> deletions = new ArrayList<>();

			if (lastUpdate != null) {
				List<TransformETY> deletionsETY = transformRepo.getDeletions(lastUpdate);
				deletions = deletionsETY.stream().map(ChangeSetUtility::transformToChangeset)
						.collect(Collectors.toList());
			}
			return deletions;
		} catch (Exception e) {
			log.error("Error retrieving modifications", e);
			throw new BusinessException("Error retrieving modifications", e);
		}
	}
}
