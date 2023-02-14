/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class StringUtility {

	/**
	 * Private constructor to avoid instantiation.
	 */
	private StringUtility() {
		// Constructor intentionally empty.
	}

	/**
	 * Returns {@code true} if the String passed as parameter is null or empty.
	 * 
	 * @param str	String to validate.
	 * @return		{@code true} if the String passed as parameter is null or empty.
	 */
	public static boolean isNullOrEmpty(final String str) {
		return str == null || str.isEmpty();
	}

	public static boolean isNullOrEmpty(final List<String> str) {
		return str == null || str.isEmpty();
	}

	public static List<String> normalize(final List<String> str) {
		return str.stream().filter(Objects::nonNull).map(String::trim).collect(Collectors.toList());
	}

	public static boolean isEmptyOrHasEmptyItems(final List<String> str) {
		return str.isEmpty() || str.stream().anyMatch(StringUtility::isNullOrEmpty);
	}
}
