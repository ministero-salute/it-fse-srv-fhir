/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public final class MockRequests {

    /**
     * Private constructor to disallow to access from other classes
     */
    private MockRequests() {
    }

    protected static String getBaseUrl() {
		return "/v1";
	}

    // Changeset

    public final String FAKE_INVALID_DTO_ID = "||----test";
    public final String FAKE_INVALID_DTO_TEMPLATE_ID_ROOT = "||----test";
    public final String FAKE_INVALID_DTO_VERSION = "||----test";

    /**
     * Valid objectID for test purpose
     */
    public final String FAKE_VALID_DTO_ID = "62cd4f7f5c7e221a80e7effa";

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
