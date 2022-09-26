package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.valueset;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UpdateValuesetResDTO extends ResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class Payload {
        private String name;
    }

    private final Payload updated;

    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo the trace info
     */
    public UpdateValuesetResDTO(LogTraceInfoDTO traceInfo, Payload updated) {
        super(traceInfo);
        this.updated = updated;
    }
}
