package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset.base.ValuesetDTO;
import lombok.Getter;

@Getter
public class GetValuesetResDTO extends ResponseDTO {

    private final ValuesetDTO document;

    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo the trace info
     */
    public GetValuesetResDTO(LogTraceInfoDTO traceInfo, ValuesetDTO document) {
        super(traceInfo);
        this.document = document;
    }
}
