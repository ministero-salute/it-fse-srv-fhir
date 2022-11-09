/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.OffsetDateTime;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.GetXsltDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.XslTransformDocumentDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.GetXsltResponseDTO;

public final class MockRequests {

    /**
     * Private constructor to disallow to access from other classes
     */
    private MockRequests() {
    }

    protected static String getBaseUrl() {
		return "/v1";
	}

    public static MockHttpServletRequestBuilder deleteXslTransformMockRequest(String root, String version) {
        return delete(getBaseUrl() + "/xslt/root/" + root + "/version/" + version)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static MockHttpServletRequestBuilder getXslTransformsMockRequest() {
        return get(getBaseUrl() + "/xslt").contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static MockHttpServletRequestBuilder getXslTransformByIdMockRequest(String id) {
        MockHttpServletRequestBuilder obj = get(getBaseUrl() + "/xslt/id/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        return obj;
    }

    public static MockHttpServletRequestBuilder queryXslTransformMockRequest(String root, String version) {
        return get(getBaseUrl() + "/xslt/root/" + root + "/version/" + version)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    // Changeset

    public static MockHttpServletRequestBuilder getXslTransformChangeSet(String lastUpdate) {
        return get(getBaseUrl() + "/changeset/xslt/status?lastUpdate=" + lastUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static MockHttpServletRequestBuilder getXslTransformChangeSetMockRequest(String queryDate) {
        return get(getBaseUrl() + "/changeset/xslt/status?lastUpdate=" + queryDate)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }


    public final String FAKE_INVALID_DTO_ID = "||----test";
    public final String FAKE_INVALID_DTO_NAME_XSLTRANSFORM = "||----test";
    public final String FAKE_INVALID_DTO_TEMPLATE_ID_ROOT = "||----test";
    public final String FAKE_INVALID_DTO_CONTENT_XSLTRANSFORM = "||----test";
    public final String FAKE_INVALID_DTO_VERSION = "||----test";

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

    public final GetXsltDTO FAKE_GET_XSLT_DTO = new GetXsltDTO();

    public final GetXsltResponseDTO FAKE_GET_XSLT_RESPONSE_DTO = new GetXsltResponseDTO();

    /** TRANSFORM **/

    public static MockHttpServletRequestBuilder deleteTransformMockRequest(String root, String version,
                                                                              String baseUrl) {
        return delete(getBaseUrl() + "/transform/root/" + root + "/version/" + version)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static MockHttpServletRequestBuilder getTransformsMockRequest(String baseUrl) {
        return get(getBaseUrl() + "/transform").contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static MockHttpServletRequestBuilder getTransformByIdMockRequest(String id) {
        MockHttpServletRequestBuilder obj = get(getBaseUrl() + "/transform/id/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        return obj;
    }

    public static MockHttpServletRequestBuilder queryTransformMockRequest(String root, String version,
                                                                             String baseUrl) {
        return get(getBaseUrl() + "/transform/root/" + root + "/version/" + version)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static MockHttpServletRequestBuilder queryActiveTransformMockRequest(String baseUrl) {
        return get(getBaseUrl() + "/transform/active")
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

}
