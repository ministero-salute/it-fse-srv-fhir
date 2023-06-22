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
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.error;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorInstance {

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Validation {
                public static final String CONSTRAINT_FIELD = "/constraint/field";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Server {
                public static final String INTERNAL = "/internal";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Resource {
                public static final String CONFLICT = "/conflict";
                public static final String NOT_FOUND = "/not-found";
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class Fields {
                public static final String ID = "id";
                public static final String LAST_UPDATE = "lastUpdate";
        }

}
