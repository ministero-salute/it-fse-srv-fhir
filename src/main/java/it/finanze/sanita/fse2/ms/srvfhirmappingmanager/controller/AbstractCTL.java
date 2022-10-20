package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
@Slf4j
public abstract class AbstractCTL {

	@Autowired
	private Tracer tracer;

	protected boolean isValidXslt(MultipartFile file) {

		boolean isValid = false;
		if (file != null && !file.isEmpty()) {
			try {
				final String extension = Optional.ofNullable(FilenameUtils.getExtension(file.getOriginalFilename())).orElse("");
				return extension.equals("xsl");
			} catch (Exception e) {
				log.warn("Error, file not valid", e);
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
