package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.io.IOException;
import java.time.OffsetDateTime;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.GetXsltDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.GetXsltResponseDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDocumentDTO;

public final class MockRequests {

    /**
     * Private constructor to disallow to access from other classes
     */
    private MockRequests() {}

    public static MockHttpServletRequestBuilder deleteXslTransformMockRequest(String root, String version, String baseUrl) {
        return delete(baseUrl + "/xslt/root/" + root + "/version/" + version)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    } 
 
    public static MockHttpServletRequestBuilder getXslTransformsMockRequest(String baseUrl) {
        return get(baseUrl + "/xslt").contentType(MediaType.APPLICATION_JSON_VALUE);
    } 
    
    public static MockHttpServletRequestBuilder getXslTransformByIdMockRequest(String id, String baseUrl) {
    	MockHttpServletRequestBuilder obj = get(baseUrl + "/xslt/id/" + id).contentType(MediaType.APPLICATION_JSON_VALUE); 
        return obj; 
    } 
    
    public static MockHttpServletRequestBuilder queryXslTransformMockRequest(String root, String version, String baseUrl) {
        return get(baseUrl + "/xslt/root/" + root + "/version/" + version)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    }



    // Changeset

    public static MockHttpServletRequestBuilder getXslTransformChangeSet(String lastUpdate, String baseUrl) {
        return get(baseUrl + "/changeset/xslt/status?lastUpdate=" + lastUpdate).contentType(MediaType.APPLICATION_JSON_VALUE); 
    }
    
    public static MockHttpServletRequestBuilder getXslTransformChangeSetMockRequest(String queryDate, String baseUrl) {
        return get(baseUrl + "/changeset/xslt/status?lastUpdate="+queryDate).contentType(MediaType.APPLICATION_JSON_VALUE);
    } 
    
    public static MockHttpServletRequestBuilder getValuesetChangesetMockRequest(String queryDate, String baseUrl) {
        return get(baseUrl + "/changeset/valueset/status?lastUpdate="+queryDate).contentType(MediaType.APPLICATION_JSON_VALUE);
    } 

    public static MockHttpServletRequestBuilder getDefinitionChangesetMockRequest(String queryDate, String baseUrl) {
        return get(baseUrl + "/changeset/definition/status?lastUpdate="+queryDate).contentType(MediaType.APPLICATION_JSON_VALUE);
    } 
    


    // ------- Definition

    public static MockHttpServletRequestBuilder getDefinitionByNameMockRequest(final String name, final String baseUrl) {
        return get(baseUrl + "/definition/" + name).contentType(MediaType.APPLICATION_JSON_VALUE); 
    }

    public static MockHttpServletRequestBuilder getDefinitionByIdMockRequest(final String id, final String baseUrl) {
        return get(baseUrl + "/definition/id/" + id).contentType(MediaType.APPLICATION_JSON_VALUE);
    }
    
    public static MockHttpServletRequestBuilder deleteDefinitionByNameMockRequest(final String name, final String baseUrl) {
        return delete(baseUrl + "/definition/" + name).contentType(MediaType.APPLICATION_JSON_VALUE);
    }




    public static MockHttpServletRequestBuilder uploadDefinitionMockRequest(String name, String version, MockMultipartFile file, String baseUrl) throws IOException {

        MockMultipartHttpServletRequestBuilder req = multipart(baseUrl + "/definition");

        req.file("File", file.getBytes());
        req.param("name", name);
        req.param("version", version);


        // Add parts
        // req.part(
        //     new MockPart("name", name.getBytes()),
        //     new MockPart("version", version.getBytes())
        //     //new MockPart("File", file.getBytes())
        // );

        req.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        
        return req;
    }


    // ------- Valueset

    public static MockHttpServletRequestBuilder getValuesetByNameMockRequest(final String name, final String baseUrl) {
        return get(baseUrl + "/valueset/" + name).contentType(MediaType.APPLICATION_JSON_VALUE); 
    }

    public static MockHttpServletRequestBuilder getValuesetByIdMockRequest(final String id, final String baseUrl) {
        return get(baseUrl + "/valueset/id/" + id).contentType(MediaType.APPLICATION_JSON_VALUE);
    }
    
    public static MockHttpServletRequestBuilder deleteValuesetByNameMockRequest(final String name, final String baseUrl) {
        return delete(baseUrl + "/valueset/" + name).contentType(MediaType.APPLICATION_JSON_VALUE);
    }




    public static MockHttpServletRequestBuilder uploadValuesetMockRequest(String name, MockMultipartFile file, String baseUrl) throws IOException {

        MockMultipartHttpServletRequestBuilder req = multipart(baseUrl + "/valueset");

        req.file("File", file.getBytes());
        req.param("name", name);


        // Add parts
        // req.part(
        //     new MockPart("name", name.getBytes()),
        //     new MockPart("version", version.getBytes())
        //     //new MockPart("File", file.getBytes())
        // );

        req.contentType(MediaType.MULTIPART_FORM_DATA_VALUE);
        
        return req;
    }

    
   // -------  Map
    
    public static MockHttpServletRequestBuilder getMapByNameMockRequest(String name, String baseUrl) {
        return get(baseUrl, name).contentType(MediaType.APPLICATION_JSON_VALUE);
    } 
    
    public static MockHttpServletRequestBuilder getMapChangesetMockRequest(String queryDate, String baseUrl) {
        return get(baseUrl + "/changeset/map/status?lastUpdate="+queryDate).contentType(MediaType.APPLICATION_JSON_VALUE);
    } 
    
//    public static MockHttpServletRequestBuilder getMapByIdMockRequest(String name) {
//        return get(API_GET_ONE_BY_ID_FULL,name).contentType(MediaType.APPLICATION_JSON_VALUE);
//    } 

    public static MockHttpServletRequestBuilder deleteMapMockRequest(String name, String baseUrl) {
        return delete(baseUrl + "/map/"+name)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    } 
    
    public static MockHttpServletRequestBuilder uploadMapMockRequest(String name,String root, String version, MockMultipartFile[] files, String baseUrl) {
    	// Create request
        MockMultipartHttpServletRequestBuilder req = multipart(baseUrl + "/map");
        // Iterate file
        for (MockMultipartFile f : files) {
            req.file(f);
        }

        // Add parts

        req.part(
            new MockPart("name", name.getBytes()),
            new MockPart("root",root.getBytes()),
            new MockPart("version", version.getBytes())
        );
        return req;

    }
    
    
    public static MockHttpServletRequestBuilder updateMapMockRequest(String name, MockMultipartFile[] files,String baseUrl) {
        // create request
    	MockMultipartHttpServletRequestBuilder req = multipart(baseUrl+"/map", name);
    	
    	for (MockMultipartFile f : files) {
    		req.file(f);
    	}
        // Modify output method
        req.with(request ->{
        	request.setMethod(HttpMethod.PUT.name());
        	return request;
        });
        
        return req;
        
    	
    	
    }
    
    
    
    
    public static MockMultipartFile createFakeMultipartEmpty(String filename) {
        return new MockMultipartFile(
            "files",
            filename,
            APPLICATION_XML_VALUE,
            new byte[0]
        );
    }
    
    public static MockMultipartFile createFakeMultipart(String filename) {
        return new MockMultipartFile(
            "files",
            filename,
            APPLICATION_XML_VALUE,
            "Hello world!".getBytes()
        );
    }
    
    public final String FAKE_INVALID_DTO_ID = "||----test";
    public final String FAKE_INVALID_DTO_NAME_XSLTRANSFORM = "||----test";
    public final String FAKE_INVALID_DTO_TEMPLATE_ID_ROOT ="||----test";
    public final String FAKE_INVALID_DTO_CONTENT_XSLTRANSFORM = "||----test";
    public final String	FAKE_INVALID_DTO_VERSION = "||----test";

    /**
     * Valid objectID for test purpose
     */
    public final String FAKE_VALID_DTO_ID = "62cd4f7f5c7e221a80e7effa";
    /**
     * XslTransform DTO for test purpose
     */
    public final XslTransformDTO FAKE_XSLT_DTO = new XslTransformDTO();
    		
    public final XslTransformDocumentDTO FAKE_XSLT_DOCUMENT_DTO = new XslTransformDocumentDTO(
    		FAKE_INVALID_DTO_ID, 
    		FAKE_INVALID_DTO_NAME_XSLTRANSFORM, 
    		FAKE_INVALID_DTO_TEMPLATE_ID_ROOT, 
    		FAKE_INVALID_DTO_CONTENT_XSLTRANSFORM, 
    		FAKE_INVALID_DTO_VERSION, 
    		OffsetDateTime.now());
    		
    public final GetXsltDTO FAKE_GET_XSLT_DTO =new GetXsltDTO();
    
    public final GetXsltResponseDTO FAKE_GET_XSLT_RESPONSE_DTO = new GetXsltResponseDTO();
    
}
