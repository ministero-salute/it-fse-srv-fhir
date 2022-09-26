package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.impl.definition.base.DefinitionDTO;
import lombok.Getter;

@Getter
public class GetDefinitionResDTO extends ResponseDTO {

    private final DefinitionDTO document;

    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo the trace info
     */
    public GetDefinitionResDTO(LogTraceInfoDTO traceInfo, DefinitionDTO document) {
        super(traceInfo);
        this.document = document;
    }
}
