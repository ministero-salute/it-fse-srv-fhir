package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DataProcessingException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.DocumentAlreadyPresentException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.model.Definition;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDefinitionSRV {
    List<Definition> insertDocsByName(String version, MultipartFile[] files) throws DocumentAlreadyPresentException, OperationException, DataProcessingException;
}
