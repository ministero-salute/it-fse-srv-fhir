package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service;

import org.springframework.lang.Nullable;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;

import java.util.Date;
import java.util.List;

public interface IChangeSetSRV<T> {
    /**
     * Retrieves the latest insertions according to the given timeframe
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing insertions
     * @throws OperationException If a data-layer error occurs
     */
    List<ChangeSetDTO<T>> getInsertions(@Nullable Date lastUpdate) throws OperationException;

    /**
     * Retrieves the latest deletions according to the given timeframe
     * @param lastUpdate The timeframe to consider while calculating
     * @return The missing deletions
     * @throws OperationException If a data-layer error occurs
     */
    List<ChangeSetDTO<T>> getDeletions(@Nullable Date lastUpdate) throws OperationException;
}
