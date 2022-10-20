/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;

import javax.validation.Path;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class UtilsMisc {

	/**
	 * Max size chunk.
	 */
	private static final int CHUNK_SIZE = 16384;

    /**
     * Private constructor to disallow to access from other classes
     */
    private UtilsMisc() {}

    public static OffsetDateTime convertToOffsetDateTime(Date dateToConvert) {
        return dateToConvert.toInstant().atOffset(ZoneOffset.UTC);
    }
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

    /**
	 * Metodo per il recupero del contenuto di un file dalla folder interna "/src/main/resources".
	 *
	 * @param filename	nome del file
	 * @return			contenuto del file
	 */
	public static byte[] getFileFromInternalResources(final String filename) {
		byte[] b = null;
		try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
			b = getByteFromInputStream(is);
		} catch (Exception e) {
			log.error("FILE UTILS getFileFromInternalResources(): Errore in fase di recupero del contenuto di un file dalla folder '/src/main/resources'. ", e);
		}
		return b;
	}

	/**
	 * Recupero contenuto file da input stream.
	 *
	 * @param is
	 *            input stream
	 * @return contenuto file
	 */
	private static byte[] getByteFromInputStream(final InputStream is) {
		byte[] b;
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[CHUNK_SIZE];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			b = buffer.toByteArray();
		} catch (Exception e) {
			log.error("Errore durante il trasform da InputStream a byte[]: ", e);
			throw new BusinessException(e);
		}
		return b;
	}
}
