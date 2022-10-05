package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Map;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMapSRV {
    List<Map> insertDocsByName(String root, String extension, MultipartFile[] files) throws OperationException, DocumentAlreadyPresentException, DataProcessingException;
}