package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import lombok.Getter;

@Getter
public class UploadDefinitionResDTO extends ResponseDTO {

  
    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo the trace info
     */
    public UploadDefinitionResDTO(LogTraceInfoDTO traceInfo) {
        super(traceInfo);
    }
}
