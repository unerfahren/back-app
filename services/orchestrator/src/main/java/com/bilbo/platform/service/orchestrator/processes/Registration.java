package com.bilbo.platform.service.orchestrator.processes;

import com.bilbo.platform.service.customer.model.ClientDto;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.springframework.stereotype.Component;

@Component
public class Registration extends RouteBuilder {

    private static final String CLIENT_DIRECT = "direct:client-service"
            .concat("-");

//    @formatter:off
    @Override
    public void configure() {
        from("direct:registerClient")
                .routeId("registerClient")
                .log("started register process with ${body}")
                .saga()
                    .marshal().json()
                    .multicast()
                    .to("direct:addClientToKeycloak")
                    .to("direct:addClient");

        from("direct:addClientToKeycloak")
                .routeId("addClientToKeycloak")
                .saga()
                    .propagation(SagaPropagation.MANDATORY)
                    .log("add client to keycloak: ${body}")
                    .to(CLIENT_DIRECT.concat("createKeycloakClient"))
                    .log("response: ${body}, location: ${headers.Location}");

        from("direct:addClient")
                .routeId("addClient")
                .saga()
                    .log("add client to client service: ${body}")
                    .propagation(SagaPropagation.MANDATORY)
                    .option("client", body())
                    .compensation("direct:deleteClientFromKeycloak")
                    .to(CLIENT_DIRECT.concat("createClient"))
                    .log("response: ${body}, location: ${headers.Location}")
                    .unmarshal().json(ClientDto.class);

        from("direct:deleteClientFromKeycloak")
                .routeId("deleteClientFromKeycloak")
                .transform(header("client"))
                .log("delete keycloak body: ${body}")
                .log("email: ${jsonpath($.email)}")
                .setHeader("email", jsonpath("$.email", String.class))
                .to(CLIENT_DIRECT.concat("deleteKeycloakClient"));
    }

}
