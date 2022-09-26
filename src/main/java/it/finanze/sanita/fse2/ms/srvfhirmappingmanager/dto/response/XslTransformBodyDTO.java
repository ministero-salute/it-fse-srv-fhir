package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * XSLT Body for insert and update endpoints
 * 
 * @author Riccardo Bonesi
 */
@Data
@NoArgsConstructor
public class XslTransformBodyDTO {
    @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
    private String templateIdRoot;

    @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
    private String nameXslTransform;

    @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
    private String version;

}
