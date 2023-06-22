/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright 2023 Ministero della Salute
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.AbstractCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.IChangeSetCTL;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.info.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.service.ITransformSRV;
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
    private ITransformSRV transformSRV;

    @Override
    public ChangeSetResDTO getTransformChangeSet(@Nullable Date lastUpdate) throws OperationException {
        // Retrieve changes
        List<ChangeSetDTO> insertions = transformSRV.getInsertions(lastUpdate);
        List<ChangeSetDTO> deletions = transformSRV.getDeletions(lastUpdate);
        long collectionSize = transformSRV.getCollectionSize();
        // Retrieve log trace
        LogTraceInfoDTO trace = getLogTraceInfo();
        // Build response
        ChangeSetResDTO response = new ChangeSetResDTO();
        response.setTraceID(trace.getTraceID());
        response.setSpanID(trace.getSpanID());
        response.setLastUpdate(lastUpdate);
        response.setTimestamp(new Date());
        response.setInsertions(insertions);
        response.setDeletions(deletions);
        response.setTotalNumberOfElements((long) insertions.size() + deletions.size());
        response.setCollectionSize(collectionSize);

        return response;
    }
}
