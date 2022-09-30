package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = {Constants.ComponentScan.BASE})
@ActiveProfiles(Constants.Profile.TEST)
public class MapCTLTest {


    
    @Autowired
    private static ServletWebServerApplicationContext webServerAppCtxt;
    protected static String getBaseUrl() {
        
		return "http://localhost:" + webServerAppCtxt.getWebServer().getPort() + webServerAppCtxt.getServletContext().getContextPath();
    }
    

}
