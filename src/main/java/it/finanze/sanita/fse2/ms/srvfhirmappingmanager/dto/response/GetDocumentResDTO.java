package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response; 

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDocumentDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DTO used to return a document as response to getDocument by ID request.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetDocumentResDTO extends ResponseDTO {

    @Schema(implementation = XslTransformDocumentDTO.class)
    private XslTransformDocumentDTO document;

    public GetDocumentResDTO(LogTraceInfoDTO traceInfo, XslTransformDocumentDTO data) {
        super(traceInfo);
        this.document = data;
    }
}
