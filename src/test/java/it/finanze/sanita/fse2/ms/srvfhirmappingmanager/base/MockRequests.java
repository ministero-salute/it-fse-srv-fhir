package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;

public final class MockRequests {

    /**
     * Private constructor to disallow to access from other classes
     */
    private MockRequests() {}

    public static MockHttpServletRequestBuilder getXslTransformChangeSet(String lastUpdate) {
        return get("http://127.0.0.1:9085/v1/changeset/xslt/status?lastUpdate=" + lastUpdate)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    }
    
    public static MockHttpServletRequestBuilder insertXslTransformMockRequest(XslTransformDTO dto) {
        return post("http://127.0.0.1:9085/v1/xslt/", dto)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    } 
    
    public static MockHttpServletRequestBuilder deleteXslTransformMockRequest(String root, String version) {
        return delete("http://127.0.0.1:9085/v1/xslt/root/" + root + "/version/" + version)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    } 
 
    public static MockHttpServletRequestBuilder getXslTransformsMockRequest() {
        return get("http://127.0.0.1:9085/v1/xslt").contentType(MediaType.APPLICATION_JSON_VALUE);
    } 
    
    public static MockHttpServletRequestBuilder getXslTransformByIdMockRequest(String id) {
    	MockHttpServletRequestBuilder obj = get("http://127.0.0.1:9085/v1/xslt/id/" + id).contentType(MediaType.APPLICATION_JSON_VALUE); 
        return obj; 
    } 
    
    public static MockHttpServletRequestBuilder getXslTransformsErrorMockRequest() {
        return get("http://127.0.0.1:9085/v1/xslt").contentType(MediaType.MULTIPART_FORM_DATA);
    } 
    
    public static MockHttpServletRequestBuilder queryXslTransformMockRequest(String root, String version) {
        return get("http://127.0.0.1:9085/v1/xslt/root/" + root + "/version/" + version)
        			.contentType(MediaType.APPLICATION_JSON_VALUE); 
    }

    public static String createBlankString() {
        return "  ";
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

}
