package com.bilbo.platfrom.service.company;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest
public class Stratz {

    @Test
    public void call() throws IOException, InterruptedException {
        final var httpClient = HttpClient.newHttpClient();
        final var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.stratz.com/api/v1/Hero"))
                .GET()
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJTdWJqZWN0IjoiM2Y5ZjQ0ZjgtZTIzYy00MzA5LTkyYzMtOTQxOWZkOGRiODZlIiwiU3RlYW1JZCI6IjExNTgxNzE1OSIsIm5iZiI6MTcyNjE2MzE4MCwiZXhwIjoxNzU3Njk5MTgwLCJpYXQiOjE3MjYxNjMxODAsImlzcyI6Imh0dHBzOi8vYXBpLnN0cmF0ei5jb20ifQ.bPmNusPH0_c_Z_Acsyh4639TGMBWtZmgg7EWTnmlFZw")
                .build();
        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        final var mapper = new ObjectMapper().findAndRegisterModules();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final var rs = mapper.readValue(response.body(), HeroDto.class);
        System.out.println(rs.getAntiMag().getDisplayName());
        System.out.println(rs);
    }
}
