package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.ITransformCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.TransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.request.TransformBodyDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.TransformResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentNotFoundException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class TransformCTL extends AbstractCTL implements ITransformCTL {

	@Autowired
	private transient ITransformSRV transformSRV;

	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = -3766806530281454403L;


	@Override
	public ResponseEntity<TransformResponseDTO> uploadTransform(HttpServletRequest request, String rootMapIdentifier, String templateIdRoot, String version, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {
		log.debug(Constants.Logs.CALLED_API_UPLOAD_TRANSFORM);
		TransformBodyDTO transformBodyDTO = TransformBodyDTO.builder()
						.templateIdRoot(templateIdRoot)
						.version(version)
						.rootMapIdentifier(rootMapIdentifier).build();
		transformSRV.insertTransformByComponents(transformBodyDTO, structureDefinitions, maps, valueSets);
		return new ResponseEntity<>(new TransformResponseDTO(getLogTraceInfo()), HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<TransformResponseDTO> updateTransform(HttpServletRequest request, String rootMapIdentifier, String templateIdRoot, String version, MultipartFile[] structureDefinitions, MultipartFile[] maps, MultipartFile[] valueSets) throws IOException, OperationException, DocumentAlreadyPresentException, DocumentNotFoundException {
		log.debug(Constants.Logs.CALLED_API_UPDATE_TRANSFORM);
		TransformBodyDTO transformBodyDTO = TransformBodyDTO.builder()
				.templateIdRoot(templateIdRoot)
				.version(version)
				.rootMapIdentifier(rootMapIdentifier).build();
		transformSRV.updateTransformByComponents(transformBodyDTO, structureDefinitions, maps, valueSets);
		return new ResponseEntity<>(new TransformResponseDTO(getLogTraceInfo()), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<TransformResponseDTO> deleteTransform(HttpServletRequest request, String templateIdRoot, String version) throws DocumentNotFoundException, OperationException {
		log.debug(Constants.Logs.CALLED_API_DELETE_TRANSFORM);
		boolean existsTransform = transformSRV.delete(templateIdRoot, version);
		if (existsTransform) {
			return new ResponseEntity<>(new TransformResponseDTO(getLogTraceInfo()), HttpStatus.OK);
		}
		else {
			throw new DocumentNotFoundException(Constants.Logs.ERROR_REQUESTED_DOCUMENT_DOES_NOT_EXIST);
		}
	}

	@Override
	public ResponseEntity<TransformDTO> getTransformByTemplateIdRootAndVersion(HttpServletRequest request, String templateIdRoot, String version) throws DocumentNotFoundException, OperationException {
		log.info(Constants.Logs.CALLED_API_QUERY_TRANSFORM_ROOT_EXTENSION);
		TransformDTO response =  transformSRV.findByTemplateIdRootAndVersion(templateIdRoot, version);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<TransformDTO> getTransformById(HttpServletRequest request, String id) throws OperationException, DocumentNotFoundException {
		log.info(Constants.Logs.CALLED_API_GET_TRANSFORM);
		TransformDTO response = transformSRV.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<List<TransformDTO>> getTransform(HttpServletRequest request) throws OperationException {
		log.info(Constants.Logs.CALLED_API_GET_ALL_TRANSFORM);
		List<TransformDTO> response = transformSRV.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@Override
	public ResponseEntity<List<TransformDTO>> getActiveTransform(HttpServletRequest request) throws OperationException {
		log.info(Constants.Logs.CALLED_API_GET_ACTIVE_TRANSFORM);
		List<TransformDTO> response = transformSRV.findAllActive();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}