/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility;


import lombok.extern.slf4j.Slf4j;

import javax.validation.Path;
import java.util.Base64;

@Slf4j
public final class UtilsMisc {

    /**
     * Private constructor to disallow to access from other classes
     */
    private UtilsMisc() {}

    /**
     * Encode in Base64 the byte array passed as parameter.
     *
     * @param input	The byte array to encode.
     * @return		The encoded byte array to String.
     */
    public static String encodeBase64(final byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public static String extractKeyFromPath(Path path) {
        String field = "";
        for(Path.Node node: path) field = node.getName();
        return field;
    }
}
