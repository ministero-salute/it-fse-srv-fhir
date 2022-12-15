/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
    public static final String API_ID = "id";
    public static final String API_TRANSFORM_MAPPER = "/" + API_VERSION + "/" + API_TRANSFORM;

    public static final String API_TRANSFORM_TAG = "Transform Controller";

    public static final String API_QP_INCLUDE_DELETED = "includeDeleted";
    public static final String API_QP_BINARY = "includeBinary";
    public static final String API_PATH_TEMPLATE_ID_ROOT_VAR = "templateIdRoot";
    public static final String API_PATH_ROOT_MAP_IDENTIFIER_VAR = "rootMapIdentifier";
    public static final String API_PATH_VALUESETS_VAR = "valueSets";
    public static final String API_PATH_MAPS_VAR = "maps";
    public static final String API_PATH_STRUCTURE_DEFINITIONS_VAR = "structureDefinitions";
    public static final String API_PATH_ID_VAR = "id";
    public static final String API_PATH_VERSION_VAR = "version";
    public static final String API_PATH_TEMPLATE_ID_ROOT = "/{" + API_PATH_TEMPLATE_ID_ROOT_VAR + "}";
    public static final String API_DELETE_BY_TEMPLATE_ID_ROOT = API_PATH_TEMPLATE_ID_ROOT;
    public static final String API_GET_BY_TEMPLATE_ID_ROOT = API_PATH_TEMPLATE_ID_ROOT;
    public static final String API_ID_EXTS = "/{" + API_PATH_ID_VAR + "}";
    public static final String API_GET_ONE_BY_ID = API_ID + API_ID_EXTS;
}
