package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import java.util.List;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.GetXsltDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetXsltResponseDTO
 {

	private List<GetXsltDTO> body;
	
}
