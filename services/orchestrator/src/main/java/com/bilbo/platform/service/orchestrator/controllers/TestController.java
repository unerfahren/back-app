package com.bilbo.platform.service.orchestrator.controllers;

import com.bilbo.platform.service.customer.model.ClientDto;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/process")
public class TestController {

    @Autowired
    private ProducerTemplate template;

    @PostMapping("/test")
    public void test() {
        final var clientDto = new ClientDto("test@mail.ru")
                .firstName("firstName")
                .lastName("lastName");
//        final var response = template.request("direct:registerClient", exchange -> exchange.getIn().setBody(clientDto));
        final var response = template.request("direct:test", exchange -> {});
        System.out.println(response.getMessage().getBody());
    }

    @PostMapping("/register")
    public void registration() {
        final var clientDto = new ClientDto("test@mail.ru")
                .firstName("firstName")
                .lastName("lastName");
        final var response = template.request("direct:registerClient", exchange -> exchange.getIn().setBody(clientDto));
//        final var response = template.request("direct:registerClient", exchange -> {});
        System.out.println(response.getMessage().getBody());
    }

    @GetMapping("/gpt")
    public void gpt() {
        final var response = template.request("direct:startSaga", exchange -> {});
    }
}
