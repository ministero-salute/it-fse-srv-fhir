/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility;

import com.google.gson.Gson;

public class JsonUtility {
    private static final Gson gson = new Gson();

    private JsonUtility() {}

    public static <T> T clone(Object object, Class<T> outputClass) {
        return gson.fromJson(gson.toJson(object), outputClass);
    }

    public static <T> T toJsonObject(String json, Class<T> outputClass) {
        return gson.fromJson(json, outputClass);
    }
}
