package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.MapETY;

public interface IMapRepo extends IChangeSetRepo<MapETY> {
    boolean isDocumentInserted(String name) throws OperationException;
    MapETY findDocByName(String name) throws OperationException;
    MapETY insertDocByName(MapETY entity) throws OperationException;
    MapETY updateDocByName(MapETY current, MapETY newest) throws OperationException;
    MapETY deleteDocByName(String name) throws OperationException;
    MapETY findDocById(String id) throws OperationException;
}
