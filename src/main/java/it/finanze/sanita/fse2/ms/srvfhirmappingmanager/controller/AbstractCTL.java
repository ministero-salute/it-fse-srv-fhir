package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO; 

/**
 * 
 * @author CPIERASC
 *
 *	Abstract controller.
 */
public abstract class AbstractCTL {

	@Autowired
	private Tracer tracer;

	protected boolean isValidXslt(MultipartFile file) {

		boolean isValid = false;
		if (file != null && !file.isEmpty()) {
			try {
				final Path path = Paths.get(file.getOriginalFilename());
				final String mimeType = Files.probeContentType(path);
				
				if (MimeTypeUtils.TEXT_XML_VALUE.equals(mimeType)) {
					isValid = true;
				}
			} catch (Exception e) {
				isValid = false;
			}
		}

		return isValid;
	}

	protected LogTraceInfoDTO getLogTraceInfo() {
		LogTraceInfoDTO out = new LogTraceInfoDTO(null, null);
		if (tracer.currentSpan() != null) {
			out = new LogTraceInfoDTO(
					tracer.currentSpan().context().spanIdString(), 
					tracer.currentSpan().context().traceIdString());
		}
		return out;
	}

}

