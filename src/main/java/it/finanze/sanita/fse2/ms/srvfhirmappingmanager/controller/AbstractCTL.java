package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;

import java.nio.charset.StandardCharsets;
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
				final String content = new String(file.getBytes(), StandardCharsets.UTF_8);

				isValid = MimeTypeUtils.TEXT_XML_VALUE.equals(mimeType) && content.startsWith("<?xml") && content.contains("xsl:stylesheet");
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
