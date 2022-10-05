package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.impl;

import com.mongodb.MongoException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.TransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.base.DefinitionDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.base.MapDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.base.ValuesetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.ITransformRepo;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.*;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Definition;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Map;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Valueset;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IDefinitionSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IMapSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IValuesetSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ChangeSetUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


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
	public void insertTransformByComponents(String rootMapIdentifier, String rootMapExtension, TransformBodyDTO body, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws DocumentAlreadyPresentException, OperationException, DataProcessingException {
		log.debug("Start insertion of transform");
		TransformETY existingDocument = transformRepo.findByTemplateIdRootAndVersion(body.getTemplateIdRoot(), body.getVersion());
		try {
			if (existingDocument != null) {
				throw new DocumentAlreadyPresentException("Cannot insert the given document, it already exists");
			}
			List<Map> insertedMapsETY = mapSRV.insertDocsByName(rootMapIdentifier, rootMapExtension, maps);
			List<Definition> insertedDefinitionsETY = definitionSRV.insertDocsByName(null, structureDefinitions);
			List<Valueset> insertedValuesetsETY = valuesetSRV.insertDocsByName(valueSets);

			Date insertionDate = new Date();
			TransformETY transformETY = TransformETY.fromComponents(body.getTemplateIdRoot(), body.getVersion(), insertionDate, insertionDate, insertedMapsETY, insertedValuesetsETY, insertedDefinitionsETY);
			transformRepo.insert(transformETY);
		} catch (MongoException ex) {
			log.error(Constants.Logs.ERROR_INSERT_TRANSFORM , ex);
			throw new OperationException(Constants.Logs.ERROR_INSERT_TRANSFORM , ex);
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

		for (Definition definition : output.getDefinitions()) {
			definitions.put(definition.getFilenameDefinition(), DefinitionDTO.fromEntity(definition));
		}

		for (Map map : output.getMaps()) {
			maps.put(map.getFilenameMap(), MapDTO.fromEntity(map));
		}

		for (Valueset valueset : output.getValuesets()) {
			valuesets.put(valueset.getFilenameValueset(), ValuesetDTO.fromEntity(valueset));
		}

		transformDTO.setId(output.getId());
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
