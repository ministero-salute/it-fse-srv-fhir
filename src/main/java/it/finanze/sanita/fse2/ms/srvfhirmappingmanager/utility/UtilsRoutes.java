package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility;

public class UtilsRoutes {
    /**
     * Private constructor to disallow to access from other classes
     */
    private UtilsRoutes() {}

    public static final String API_VERSION = "v1";
    public static final String API_VALUESET = "valueset";
    public static final String API_MAP= "map";
    public static final String API_DEFINITION = "definition";
    public static final String API_VALUESET_MAPPER = "/" + API_VERSION + "/" + API_VALUESET;
    public static final String API_DEFINITION_MAPPER = "/" + API_VERSION + "/" + API_DEFINITION;
    public static final String API_MAP_MAPPER = "/" + API_VERSION + "/" + API_MAP;
    public static final String API_VALUESET_TAG = "Valueset";
    public static final String API_MAP_TAG = "Map";
    public static final String API_DEFINITION_TAG = "Definition";
    public static final String API_PATH_ID_VAR = "id";
    public static final String API_PATH_NAME_VAR = "name";
    public static final String API_ID_EXTS = "/{" + API_PATH_ID_VAR + "}";

    public static final String API_GET_ONE_BY_ID = API_PATH_ID_VAR + API_ID_EXTS;
    public static final String API_PATH_EXTS = "/{" + API_PATH_NAME_VAR + "}";
}
