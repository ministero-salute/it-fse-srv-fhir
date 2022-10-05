package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import java.util.Date;
import java.util.List;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.*;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.IChangeSetCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;

/** 
 * 
 * @author Riccardo Bonesi
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
        int totalNumberOfElements = insertions.size() + deletions.size();
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
        response.setTotalNumberOfElements(totalNumberOfElements);
        // Have a nice day
        return response;
    }

    @Override
    public ChangeSetResDTO<TransformCS> getTransformChangeSet(@Nullable Date lastUpdate) throws OperationException {
        // Retrieve changes
        List<ChangeSetDTO<TransformCS>> insertions = transformSRV.getInsertions(lastUpdate);
        List<ChangeSetDTO<TransformCS>> deletions = transformSRV.getDeletions(lastUpdate);
        int totalNumberOfElements = insertions.size() + deletions.size();
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
        response.setTotalNumberOfElements(totalNumberOfElements);
        return response;
    }
}
