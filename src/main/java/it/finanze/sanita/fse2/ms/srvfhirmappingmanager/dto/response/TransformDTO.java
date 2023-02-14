package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;


import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc.encodeBase64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransformDTO {
	
    private String id;
    private String uri;
    private String version;
    private List<String> templateIdRoot;
    private String content;
    private String filename;
	private FhirTypeEnum type;
    private Date insertionDate;
    private Date lastUpdateDate;
    private boolean deleted;

    @AllArgsConstructor
    public static class Options {
        private final boolean binary;
    }

	
    public static TransformDTO fromEntity(TransformETY e) {
        return new TransformDTO(
            e.getId(),
            e.getUri(),
            e.getVersion(),
            e.getTemplateIdRoot(),
            encodeBase64(e.getContent().getData()),
            e.getFilename(),
            e.getType(),
            e.getInsertionDate(),
            e.getLastUpdateDate(),
            e.isDeleted()
        );
    }


	public TransformDTO applyOptions(Options o) {
        if(!o.binary) content = null;
        return this;
	}
}
