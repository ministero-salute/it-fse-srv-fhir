package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.ValuesetETY;

public interface IValuesetRepo extends IChangeSetRepo<ValuesetETY> {
    boolean isDocumentInserted(String name) throws OperationException;
    ValuesetETY findDocByName(String name) throws OperationException;
    ValuesetETY insertDocByName(ValuesetETY entity) throws OperationException;
    ValuesetETY updateDocByName(ValuesetETY current, ValuesetETY newest) throws OperationException;
    ValuesetETY deleteDocByName(String name) throws OperationException;
    ValuesetETY findDocById(String id) throws OperationException;
}
