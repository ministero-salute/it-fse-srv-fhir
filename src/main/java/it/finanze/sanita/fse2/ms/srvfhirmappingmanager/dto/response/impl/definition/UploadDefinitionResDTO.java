package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UploadDefinitionResDTO extends ResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class Payload {
        private String name;
    }

    private final Payload uploaded;

    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo the trace info
     */
    public UploadDefinitionResDTO(LogTraceInfoDTO traceInfo, Payload uploaded) {
        super(traceInfo);
        this.uploaded = uploaded;
    }
}
