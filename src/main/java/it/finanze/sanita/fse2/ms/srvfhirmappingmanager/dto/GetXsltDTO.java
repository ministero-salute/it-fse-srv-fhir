package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Date;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.*;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetXsltDTO
 {

     @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
     private String id;

     @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
     private String templateIdRoot;

     @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
     private String nameXslTransform;

     @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_BINARY_MAX_SIZE)
     private String contentXslTransform;

     @Size(min = DEFAULT_STRING_MIN_SIZE, max = DEFAULT_STRING_MAX_SIZE)
     private String version;

     private Date lastUpdate;

     public static GetXsltDTO fromDTO(XslTransformDTO dto) {
         return new GetXsltDTO(
             dto.getId(),
             dto.getTemplateIdRoot(),
             dto.getNameXslTransform(),
             UtilsMisc.encodeBase64(dto.getContentXslTransform().getData()),
             dto.getVersion(),
             dto.getLastUpdate()
         );
     }
 }
