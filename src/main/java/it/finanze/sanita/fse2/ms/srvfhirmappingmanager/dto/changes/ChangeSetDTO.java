package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.changes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSetDTO<T> {

	@Size(max = DEFAULT_STRING_MAX_SIZE)
	private String id; 

	T description;

}
