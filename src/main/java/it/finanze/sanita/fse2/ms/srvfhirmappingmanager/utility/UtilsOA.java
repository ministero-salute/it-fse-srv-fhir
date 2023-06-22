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

public final class UtilsOA {

    /**
     * Private constructor to disallow to access from other classes
     */
    private UtilsOA() {}

    /**
     * Min size extension string
     */
    public static final int OA_EXTS_STRING_MIN = 1;

    /**
     * Max size extension string
     */
    public static final int OA_EXTS_STRING_MAX = 50;

    /**
     * Max size given string ids
     */
    public static final int OA_IDS_SIZE_MAX = 255;

    /**
     * Min size given string
     */
    public static final int OA_ANY_STRING_MIN = 1;

    /**
     * Max size given string
     */
    public static final int OA_ANY_STRING_MAX = 255;

    /**
     * Min array size files
     */
    public static final int OA_ARRAY_FILES_MIN = 1;
    /**
     * Max array size files
     */
    public static final int OA_ARRAY_FILES_MAX = 25;

    /**
     * Min change set array files
     */
    public static final int OA_ARRAY_CHANGESET_MIN = 0;
    /**
     * Max change set array files
     */
    public static final int OA_ARRAY_CHANGESET_MAX = 100;

    /**
     * Max file size
     */
    public static final int OA_FILE_CONTENT_MAX = Integer.MAX_VALUE / 2;

}
