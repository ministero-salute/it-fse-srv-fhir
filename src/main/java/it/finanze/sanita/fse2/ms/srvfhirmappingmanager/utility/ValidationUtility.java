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

public class ValidationUtility {
	
	private ValidationUtility() {
		
	} 
	
	public static final int DEFAULT_STRING_MIN_SIZE = 0;
	public static final int DEFAULT_STRING_MAX_SIZE = 100;
	public static final int DEFAULT_NUMBER_MIN_SIZE = 0;
	public static final int DEFAULT_NUMBER_MAX_SIZE = 10000;
	public static final int DEFAULT_ARRAY_MIN_SIZE = 0;
	public static final int DEFAULT_ARRAY_MAX_SIZE = 10000;
	public static final int DEFAULT_BINARY_MIN_SIZE = 0;
	public static final int DEFAULT_BINARY_MAX_SIZE = 10000;
	
	public static boolean isMajorVersion(final String newVersion, final String lastVersion) {
		boolean isMajor = false;
		String[] lastVersionChunks = lastVersion.split("\\.");
		String[] newVersionChunks = newVersion.split("\\.");
	
		if (Integer.parseInt(lastVersionChunks[0]) < Integer.parseInt(newVersionChunks[0])) {
			isMajor = true;
		} else if (Integer.parseInt(lastVersionChunks[0]) == Integer.parseInt(newVersionChunks[0])) {
			isMajor = Integer.parseInt(lastVersionChunks[1]) < Integer.parseInt(newVersionChunks[1]);
		}
		
		return isMajor;
	}

}
