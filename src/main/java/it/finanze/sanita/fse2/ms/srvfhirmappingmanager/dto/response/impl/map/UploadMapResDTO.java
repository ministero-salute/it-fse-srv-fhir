package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import lombok.Getter;

@Getter
public class UploadMapResDTO extends ResponseDTO {

 
    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo the trace info
     */
    public UploadMapResDTO(LogTraceInfoDTO traceInfo) {
        super(traceInfo);
    }
}
