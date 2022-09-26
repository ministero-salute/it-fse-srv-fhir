package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import java.util.Date;
import java.util.List;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.DefinitionCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.MapCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.ValuesetCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.specs.XSLTransformCS;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IDefinitionSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IMapSRV;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IValuesetSRV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.IChangeSetCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.IXslTransformSRV;

/** 
 * 
 * @author Riccardo Bonesi
 */
@RestController
public class ChangeSetCTL extends AbstractCTL implements IChangeSetCTL{

    @Autowired
    private IXslTransformSRV serviceXSL;

    @Autowired
    private IValuesetSRV serviceVS;

    @Autowired
    private IDefinitionSRV serviceDF;

    @Autowired
    private IMapSRV serviceMP;

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
    public ChangeSetResDTO<ValuesetCS> getValuesetChangeset(@Nullable Date lastUpdate) throws OperationException {
        // Retrieve changes
        List<ChangeSetDTO<ValuesetCS>> insertions = serviceVS.getInsertions(lastUpdate);
        List<ChangeSetDTO<ValuesetCS>> deletions = serviceVS.getDeletions(lastUpdate);
        int totalNumberOfElements = insertions.size() + deletions.size();
        // Retrieve log trace
        LogTraceInfoDTO trace = getLogTraceInfo();
        // Build response
        ChangeSetResDTO<ValuesetCS> response = new ChangeSetResDTO<>();
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
    public ChangeSetResDTO<DefinitionCS> getDefinitionChangeset(Date lastUpdate) throws OperationException {
        // Retrieve changes
        List<ChangeSetDTO<DefinitionCS>> insertions = serviceDF.getInsertions(lastUpdate);
        List<ChangeSetDTO<DefinitionCS>> deletions = serviceDF.getDeletions(lastUpdate);
        int totalNumberOfElements = insertions.size() + deletions.size();
        // Retrieve log trace
        LogTraceInfoDTO trace = getLogTraceInfo();
        // Build response
        ChangeSetResDTO<DefinitionCS> response = new ChangeSetResDTO<>();
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
    public ChangeSetResDTO<MapCS> getMapChangeset(Date lastUpdate) throws OperationException {
        // Retrieve changes
        List<ChangeSetDTO<MapCS>> insertions = serviceMP.getInsertions(lastUpdate);
        List<ChangeSetDTO<MapCS>> deletions = serviceMP.getDeletions(lastUpdate);
        int totalNumberOfElements = insertions.size() + deletions.size();
        // Retrieve log trace
        LogTraceInfoDTO trace = getLogTraceInfo();
        // Build response
        ChangeSetResDTO<MapCS> response = new ChangeSetResDTO<>();
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

}
