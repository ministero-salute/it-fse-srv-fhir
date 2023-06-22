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
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility;

public final class RouteUtility {

    /**
     * Private constructor to disallow to access from other classes
     */
    private RouteUtility() {}

    public static final String API_VERSION = "v1";
    public static final String API_CHANGESET = "changeset";
    public static final String API_TRANSFORM = "transform";
    public static final String API_STATUS = "status";

    public static final String API_ID = "id";
    public static final String API_TRANSFORM_MAPPER = "/" + API_VERSION + "/" + API_TRANSFORM;

    public static final String API_CHANGESET_TAG = "ChangeSet";
    public static final String API_TRANSFORM_TAG = "Transform Controller";

    public static final String API_CHANGESET_STATUS = "/" + API_VERSION + "/" + API_CHANGESET + "/" + API_TRANSFORM + "/" + API_STATUS ;

    public static final String API_QP_INCLUDE_DELETED = "includeDeleted";
    public static final String API_QP_BINARY = "includeBinary";
    public static final String API_QP_LAST_UPDATE = "lastUpdate";

    public static final String API_PATH_ROOTS_VAR = "roots";
    public static final String API_PATH_ID_VAR = "id";
    public static final String API_PATH_FILE_VAR = "file";
    public static final String API_PATH_URI_VAR = "uri";
    public static final String API_PATH_ALL_VAR = "all";
    public static final String API_PATH_TYPE_VAR = "type";
    public static final String API_PATH_VERSION_VAR = "version";

    public static final String API_ID_EXTS = "/{" + API_PATH_ID_VAR + "}";
    public static final String API_GET_ONE_BY_ID = API_ID + API_ID_EXTS;

    public static final String API_GET_ONE_BY_ID_FULL = "/" + API_VERSION + "/" + API_TRANSFORM + "/" + API_GET_ONE_BY_ID;
    public static final String API_GET_ALL_FULL = API_TRANSFORM_MAPPER + "/" + API_PATH_ALL_VAR;

    public static final String API_STATUS_FULL = "/" + API_STATUS;
}
