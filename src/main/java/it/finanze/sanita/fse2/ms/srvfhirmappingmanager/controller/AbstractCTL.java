/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO; 

/**
 *	Abstract controller.
 */
public abstract class AbstractCTL {

	@Autowired
	private Tracer tracer;

	protected LogTraceInfoDTO getLogTraceInfo() {
		LogTraceInfoDTO out = new LogTraceInfoDTO(null, null);
		if (tracer.currentSpan() != null) {
			out = new LogTraceInfoDTO(
					tracer.currentSpan().context().spanIdString(), 
					tracer.currentSpan().context().traceIdString());
		}
		return out;
	}

	protected boolean validateFiles(MultipartFile[] files) {
		boolean isValid = true;
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				if (file == null || file.isEmpty()) {
					isValid = false;
					break;
				}
			}
		} else {
			isValid = false;
		}

		return isValid;
	}

}
