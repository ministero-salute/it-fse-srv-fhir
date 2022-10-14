package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.GetXsltDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetXsltResponseDTO
 {

	@ArraySchema(minItems = 0, maxItems = 1000)
	private List<GetXsltDTO> body;
	
}
