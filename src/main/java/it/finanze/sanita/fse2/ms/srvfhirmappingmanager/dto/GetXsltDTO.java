package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MAX_SIZE;
import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ValidationUtility.DEFAULT_STRING_MIN_SIZE;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.UtilsMisc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetXsltDTO
 {

     @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
     private String id;

     @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
     private String templateIdRoot;

     @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
     private String nameXslTransform;

     @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE, type = "string", format = "binary", description = "XSLT content")
     private String contentXslTransform;

     @Schema(minLength = DEFAULT_STRING_MIN_SIZE, maxLength = DEFAULT_STRING_MAX_SIZE)
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
