package com.bilbo.platform.service.orchestrator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties("camel.openapi")
public class OpenApiRouteProperties {

    private Map<String, Route> routes;
    @Data
    public static class Route {

        private String pathToSpec;
        private Map<String, String> params;
        private List<String> operationsIds;

    }
}
