package com.bilbo.platform.service.orchestrator.processes;

import com.bilbo.platform.service.orchestrator.config.OpenApiRouteProperties;
import lombok.Getter;
import org.apache.camel.builder.RouteBuilder;

import java.util.List;

public class OpenApiRoute extends RouteBuilder {

    @Getter
    protected final String name;
    protected final String TEMPLATE = "rest-openapi:";
    protected final List<String> operationsIds;
    protected final String SERVICE_DSL;

    public OpenApiRoute(OpenApiRouteProperties.Route route, String name) {
        final var builder = new StringBuilder(TEMPLATE)
                .append(route.getPathToSpec())
                .append("#%s?");
        route.getParams().forEach((key, value) -> builder
                .append(key)
                .append("=")
                .append(value)
                .append("&"));
        builder.deleteCharAt(builder.length() - 1);
        SERVICE_DSL = builder.toString();
        this.operationsIds = route.getOperationsIds();
        this.name = name;
    }

    @Override
    public void configure() {
        operationsIds.forEach(operationsId -> {
            final var routeId = name.concat("-").concat(operationsId);
            from("direct:".concat(routeId))
                    .routeId(routeId)
                    .to(SERVICE_DSL.formatted(operationsId));
        });
    }
}
