/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base;

import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.function.Supplier;

import static it.finanze.sanita.fse2.ms.srvfhirmappingmanager.utility.RouteUtility.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

public final class MockRequests {

    /**
     * Private constructor to disallow to access from other classes
     */
    private MockRequests() {}

    /** TRANSFORM **/
    public static MockHttpServletRequestBuilder getTransformByIdMockRequest(String id) {
        return get(API_GET_ONE_BY_ID_FULL, id).contentType(MediaType.APPLICATION_JSON_VALUE);
    }
    
    public static MockHttpServletRequestBuilder getAllTransformMockRequest() {
        return get(API_GET_ALL_FULL).contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static MockHttpServletRequestBuilder getLiveness() {
        return get(API_STATUS_FULL).contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static MockHttpServletRequestBuilder getTransformByUri(String uri) {
        return get(API_TRANSFORM_MAPPER).queryParam(
            API_PATH_URI_VAR, uri
        ).contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static MockHttpServletRequestBuilder createTransform(TransformETY e) {
        return multipart(API_TRANSFORM_MAPPER)
            .part(new MockPart(
                API_PATH_URI_VAR, emptyOrContent(e.getUri(), () -> e.getUri().getBytes()))
            )
            .part(new MockPart(
                API_PATH_VERSION_VAR, emptyOrContent(e.getVersion(), () -> e.getVersion().getBytes()))
            )
            .part(new MockPart(
                API_PATH_TYPE_VAR, emptyOrContent(e.getType(), () -> e.getType().getName().getBytes()))
            )
            .part(new MockPart(
                API_PATH_ROOTS_VAR, emptyOrContent(e.getTemplateIdRoot(), ()-> e.getTemplateIdRoot().toString().getBytes()))
            )
            .file(new MockMultipartFile(
                API_PATH_FILE_VAR, emptyOrContent(e.getContent(), () -> e.getContent().getData()))
            )
            .contentType(MediaType.MULTIPART_FORM_DATA);
    }

    private static <T> byte[] emptyOrContent(T obj, Supplier<byte[]> supplier) {
        return obj == null ? null : supplier.get();
    }

}
