package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import lombok.Getter;

@Getter
public class UploadValuesetResDTO extends ResponseDTO {


    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo the trace info
     */
    public UploadValuesetResDTO(LogTraceInfoDTO traceInfo) {
        super(traceInfo);
    }
}
