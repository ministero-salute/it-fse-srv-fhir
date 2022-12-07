/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.IChangeSetCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.specs.TransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.specs.XSLTransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IXslTransformSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/** 
 * 
 */
@RestController
public class ChangeSetCTL extends AbstractCTL implements IChangeSetCTL {

    @Autowired
    private IXslTransformSRV serviceXSL;

    @Autowired
    private ITransformSRV transformSRV;

    @Override
    public ChangeSetResDTO<XSLTransformCS> getXslTransformChangeSet(@Nullable Date lastUpdate) throws OperationException {
        // Retrieve changes
        List<ChangeSetDTO<XSLTransformCS>> insertions = serviceXSL.getInsertions(lastUpdate);
        List<ChangeSetDTO<XSLTransformCS>> deletions = serviceXSL.getDeletions(lastUpdate);
        long collectionSize = serviceXSL.getCollectionSize();

        // Retrieve log trace
        LogTraceInfoDTO trace = getLogTraceInfo();
        // Build response
        ChangeSetResDTO<XSLTransformCS> response = new ChangeSetResDTO<>();
        response.setTraceID(trace.getTraceID());
        response.setSpanID(trace.getSpanID());
        response.setLastUpdate(lastUpdate);
        response.setTimestamp(new Date());
        response.setInsertions(insertions);
        response.setDeletions(deletions);
        response.setTotalNumberOfElements(insertions.size() + deletions.size());
        response.setCollectionSize(collectionSize);

        return response;
    }

    @Override
    public ChangeSetResDTO<TransformCS> getTransformChangeSet(@Nullable Date lastUpdate) throws OperationException {
        // Retrieve changes
        List<ChangeSetDTO<TransformCS>> insertions = transformSRV.getInsertions(lastUpdate);
        List<ChangeSetDTO<TransformCS>> deletions = transformSRV.getDeletions(lastUpdate);
        long collectionSize = transformSRV.getCollectionSize();
        // Retrieve log trace
        LogTraceInfoDTO trace = getLogTraceInfo();
        // Build response
        ChangeSetResDTO<TransformCS> response = new ChangeSetResDTO<>();
        response.setTraceID(trace.getTraceID());
        response.setSpanID(trace.getSpanID());
        response.setLastUpdate(lastUpdate);
        response.setTimestamp(new Date());
        response.setInsertions(insertions);
        response.setDeletions(deletions);
        response.setTotalNumberOfElements(insertions.size() + deletions.size());
        response.setCollectionSize(collectionSize);

        return response;
    }
}
