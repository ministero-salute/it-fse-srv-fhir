package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.mongo;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *
 *	Mongo properties configuration.
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)  
public class MongoPropertiesCFG implements Serializable {
  
	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 8112027984940371008L; 
 
	@Value("${data.mongodb.uri}")
	private String uri;
	
}
