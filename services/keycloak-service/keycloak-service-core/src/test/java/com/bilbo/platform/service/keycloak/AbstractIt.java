package com.bilbo.platform.service.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.SpringDataJacksonConfiguration;
import org.springframework.data.web.config.SpringDataWebSettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractIt {

    @Autowired
    protected MockMvc mvc;
    protected ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    public <RQ> MockHttpServletResponse perform(HttpMethod method, RQ request,
                                                HttpHeaders headers, String uri,
                                                Object... uriVars) throws Exception {
        final var content = mapper.writeValueAsString(request);
        final var mockRq = MockMvcRequestBuilders.request(method, uri, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .headers(headers);

        return perform(mockRq);
    }

    public MockHttpServletResponse perform(HttpMethod method, HttpHeaders headers,
                                           String uri, Object... uriVars) throws Exception {
        final var mockRq = MockMvcRequestBuilders.request(method, uri, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers);

        return perform(mockRq);
    }

    public MockHttpServletResponse perform(RequestBuilder mockRq) throws Exception {
        return mvc.perform(mockRq)
                .andReturn()
                .getResponse();
    }

    public MockHttpServletResponse get(HttpHeaders headers, String uri,
                                            Object... uriVars) throws Exception {
        return perform(HttpMethod.GET, headers, uri, uriVars);
    }

    public <RQ> MockHttpServletResponse post(RQ request, HttpHeaders headers,
                                             String uri, Object... uriVars) throws Exception {
        return perform(HttpMethod.POST, request, headers, uri, uriVars);
    }

    public <RQ> MockHttpServletResponse put(RQ request, HttpHeaders headers,
                                            String uri, Object... uriVars) throws Exception {
        return perform(HttpMethod.PUT, request, headers, uri, uriVars);
    }

    public <RQ> MockHttpServletResponse delete(HttpHeaders headers,
                                               String uri, Object... uriVars) throws Exception {
        return perform(HttpMethod.DELETE, headers, uri, uriVars);
    }
}
