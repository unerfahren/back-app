package com.bilbo.platform.service.employee.service;

import com.bilbo.platform.dto.PagedModeImpl;
import com.bilbo.platform.service.employee.AbstractIt;
import com.bilbo.platform.service.employee.model.EmployeeDto;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
class EmployeeServiceTest extends AbstractIt {

    private static final String RESOURCE_URI = "/employees";
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Test
    public void crudTest() throws Exception {
        final var employee = new EmployeeDto("test@mail.ru", 1L, 1L)
                .birthsDate(LocalDate.now())
                .name("test")
                .lastName("test")
                .registrationDate(LocalDateTime.now(Clock.tickMinutes(ZoneId.systemDefault())));
        final var uri = createClient(employee);
        log.info("created employee: {}", uri);
        final var createdRs = get(HttpHeaders.EMPTY, uri);
        Assertions.assertThat(createdRs.getStatus()).isEqualTo(HttpStatus.OK.value());
        final var created = mapper.readValue(createdRs.getContentAsString(), EmployeeDto.class);

        Assertions.assertThat(created.getId()).isNotNull();

        final var notFoundReadRs = get(HttpHeaders.EMPTY, RESOURCE_URI + "/2");
        Assertions.assertThat(notFoundReadRs.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

        created.setEmail("updated@mail.ru");
        final var update1 = executorService.submit(() -> {
            try {
                return post(created, HttpHeaders.EMPTY, RESOURCE_URI + "/" + created.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        final var update2 = executorService.submit(() -> {
            try {
                return post(created, HttpHeaders.EMPTY, RESOURCE_URI + "/" + created.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        MockHttpServletResponse successUpdate;
        MockHttpServletResponse conflictUpdate;
        final var firstUpdate = update1.get();
        if (firstUpdate.getStatus() == HttpStatus.OK.value()) {
            successUpdate = firstUpdate;
            conflictUpdate = update2.get();
        } else {
            successUpdate = update2.get();
            conflictUpdate = firstUpdate;
        }
        Assertions.assertThat(successUpdate.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(successUpdate.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(conflictUpdate.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        final var updated = mapper.readValue(successUpdate.getContentAsString(), EmployeeDto.class);

        Assertions.assertThat(updated.getEmail()).isEqualTo(created.getEmail());
        log.info("employee {} updated {}", created, updated);
    }

    @Test
    public void filterTest() throws Exception {
        final var firstClient = createClient(1);
        final var secondClient = createClient(2);
        final var thirdClient = createClient(3);
        final var forthClient = createClient(4);

        final var firstLocation = createClient(firstClient);
        final var firstCreatedRs = get(HttpHeaders.EMPTY, firstLocation);
        Assertions.assertThat(firstCreatedRs.getStatus()).isEqualTo(HttpStatus.OK.value());
        final var firstCreated = mapper.readValue(firstCreatedRs.getContentAsString(), EmployeeDto.class);
        createClient(secondClient);
        createClient(thirdClient);
        createClient(forthClient);

        final var response = get(HttpHeaders.EMPTY, RESOURCE_URI + "?email={email}", firstClient.getEmail());
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        final PagedModel<EmployeeDto> pagedModel = mapper.readValue(response.getContentAsString(), new TypeReference<PagedModeImpl<EmployeeDto>>() {
        });
        Assertions.assertThat(pagedModel.getMetadata()).isNotNull();
        Assertions.assertThat(pagedModel.getMetadata().totalElements()).isEqualTo(1);
        final var employees = pagedModel.getContent();
        Assertions.assertThat(employees.size()).isEqualTo(1);
        Assertions.assertThat(employees.get(0)).isEqualTo(firstCreated);
    }

    private EmployeeDto createClient(int uniqNum) {
        return new EmployeeDto(uniqNum + "test@mail.ru", (long) uniqNum, (long) uniqNum)
                .birthsDate(LocalDate.of(2000 + uniqNum, uniqNum, uniqNum))
                .name(uniqNum + "test")
                .lastName(uniqNum + "test")
                .registrationDate(LocalDateTime.now(Clock.tickMinutes(ZoneId.systemDefault())));
    }

    private String createClient(EmployeeDto employeeDto) throws Exception {
        final var response = post(employeeDto, HttpHeaders.EMPTY, RESOURCE_URI);
        Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        return response.getHeader("Location");
    }

}