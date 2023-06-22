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
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;

import java.util.List;


/**
 *	Transform interface repository.
 */
public interface ITransformRepo extends IChangeSetRepo<TransformETY> {

	TransformETY insert(TransformETY ety) throws OperationException;

	List<TransformETY> remove(String uri) throws OperationException;

	TransformETY findByUri(String uri) throws OperationException;

	TransformETY findByTemplateIdRoot(String templateIdRoot) throws OperationException;

	List<TransformETY> findByUriAndDeleted(String templateIdRoot, boolean deleted) throws OperationException;

	List<TransformETY> findAll() throws OperationException;

    TransformETY findById(String id) throws OperationException;
}
