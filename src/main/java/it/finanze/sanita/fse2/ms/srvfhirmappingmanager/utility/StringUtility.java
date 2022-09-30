package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility;

import java.util.UUID;


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
	    boolean out = false;
		if (str == null || str.isEmpty()) {
			out = true;
		}
		return out;
	} 
	
	public static String generateTransactionUID() {
	
		return UUID.randomUUID().toString();
	}

}
