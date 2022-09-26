package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map.base.MapDTO;
import lombok.Getter;

@Getter
public class GetMapResDTO extends ResponseDTO {

    private final MapDTO document;

    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo the trace info
     */
    public GetMapResDTO(LogTraceInfoDTO traceInfo, MapDTO document) {
        super(traceInfo);
        this.document = document;
    }
}
