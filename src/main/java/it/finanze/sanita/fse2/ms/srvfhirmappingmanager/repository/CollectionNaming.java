package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.ProfileUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CollectionNaming {

    @Autowired
    private ProfileUtility profileUtility;

    @Bean("structureDefinitionBean")
    public String getStructureDefinitionCollection() {
        if (profileUtility.isTestProfile()) {
            return Constants.Profile.TEST_PREFIX + Constants.App.Collections.STRUCTURE_DEFINITION;
        }
        return Constants.App.Collections.STRUCTURE_DEFINITION;
    }

    @Bean("structureMapBean")
    public String getMapCollection() {
        if (profileUtility.isTestProfile()) {
            return Constants.Profile.TEST_PREFIX + Constants.App.Collections.MAP;
        }
        return Constants.App.Collections.MAP;
    }

    @Bean("valueSetBean")
    public String getValueSetCollection() {
        if (profileUtility.isTestProfile()) {
            return Constants.Profile.TEST_PREFIX + Constants.App.Collections.VALUESET;
        }
        return Constants.App.Collections.VALUESET;
    }

    @Bean("xslTransformBean")
    public String getDefinitionCollection() {
        if (profileUtility.isTestProfile()) {
            return Constants.Profile.TEST_PREFIX + Constants.App.Collections.XSL_TRANSFORM;
        }
        return Constants.App.Collections.XSL_TRANSFORM;
    }
    
    
    
}
