package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository;

import java.util.List;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.DefinitionETY;

public interface IDefinitionRepo extends IChangeSetRepo<DefinitionETY> {

	boolean isDocumentInserted(String name) throws OperationException;

	DefinitionETY findDocByName(String name) throws OperationException;

	DefinitionETY insertDocByName(DefinitionETY entity) throws OperationException;

	DefinitionETY updateDocByName(DefinitionETY current, DefinitionETY newest) throws OperationException;

	DefinitionETY deleteDocByName(String name) throws OperationException;

	DefinitionETY findDocById(String id) throws OperationException;

	void insertAll(List<DefinitionETY> entities) throws OperationException;

	List<String> isDocumentsInserted(List<String> names) throws OperationException;
	
}
