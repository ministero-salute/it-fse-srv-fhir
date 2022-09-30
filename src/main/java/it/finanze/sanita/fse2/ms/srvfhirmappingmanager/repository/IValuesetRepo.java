package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository;

import java.util.List;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.ValuesetETY;

public interface IValuesetRepo extends IChangeSetRepo<ValuesetETY> {
	
    boolean isDocumentInserted(String name) throws OperationException;
    
    ValuesetETY findDocByName(String name) throws OperationException;
    
    ValuesetETY insertDocByName(ValuesetETY entity) throws OperationException;
    
    ValuesetETY updateDocByName(ValuesetETY current, ValuesetETY newest) throws OperationException;
    
    ValuesetETY deleteDocByName(String name) throws OperationException;
    
    ValuesetETY findDocById(String id) throws OperationException;
    
    List<String> isDocumentsInserted(List<String> name) throws OperationException;
    
    void insertAll(List<ValuesetETY> entities) throws OperationException;
}
