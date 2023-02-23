/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvfhirmappingmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.dto.response.changes.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvfhirmappingmanager.repository.entity.TransformETY;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ComponentScan
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class ChangeSetControllerTest extends AbstractTest {

	@Autowired
	private MockMvc mvc;

	@BeforeEach
	void setup() {
		mongo.dropCollection(TransformETY.class);
	}

	@Test
	void getTransformChangeSetEmptyTest() throws Exception {

		final String queryDate = "2022-06-04T12:08:56.000-00:00";
		final MvcResult result = mvc
				.perform(get("/v1/changeset/transform/status?lastUpdate=" + queryDate)
					.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
				.andReturn();

		final ChangeSetResDTO changeSet = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ChangeSetResDTO.class);
		assertEquals(0, changeSet.getDeletions().size(), "With empty database should be returned 0 deletion");
		assertEquals(0, changeSet.getInsertions().size(), "With empty database should be returned 0 insertions");
		assertEquals(0, changeSet.getTotalNumberOfElements(), "With empty database should be returned 0 elements");
		assertNotNull(changeSet.getTraceID(), "TraceID should be not null");
		assertNotNull(changeSet.getSpanID(), "SpanID should be not null");
	}
}
