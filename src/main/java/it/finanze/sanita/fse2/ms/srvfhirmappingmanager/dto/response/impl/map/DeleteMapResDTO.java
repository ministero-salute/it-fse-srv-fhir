package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.map;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class DeleteMapResDTO extends ResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class Payload {
        private String name;
    }

    private final Payload deleted;

    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo the trace info
     */
    public DeleteMapResDTO(LogTraceInfoDTO traceInfo, Payload deleted) {
        super(traceInfo);
        this.deleted = deleted;
    }
}
