package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

/**
 * XSLT Body for insert and update endpoints
 * 
 * @author Riccardo Bonesi
 */
@Data
@NoArgsConstructor
public class TransformBodyDTO {
    @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
    private String templateIdRoot;

    @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
    private String version;
}
