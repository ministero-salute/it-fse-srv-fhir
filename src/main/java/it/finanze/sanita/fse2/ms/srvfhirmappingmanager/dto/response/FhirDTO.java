package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;


import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.enums.FhirTypeEnum;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.FhirETY;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FhirDTO {
	
    private String id;

    private String templateIdRoot;

    private String version;

    private String content;
    
    private String uri;
    
    private String filename;
    
	private FhirTypeEnum type;
	
    @AllArgsConstructor
    public static class Options {
        private final boolean binary;
    }

	
    public static FhirDTO fromEntity(FhirETY e) {
        return new FhirDTO(
            e.getId(),
            e.getTemplateIdRoot(),
            e.getVersion(),
            UtilsMisc.encodeBase64(e.getContent().getData()),
            e.getUri(),
            e.getFilename(),
            e.getType()
        	);
    }


	public FhirDTO applyOptions(Options o) {
        if(!o.binary) content = null;
        return this;
	}
}
