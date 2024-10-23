package com.bilbo.platfrom.service.company.service;

import com.bilbo.platfrom.service.company.AbstractIt;
import com.bilbo.platfrom.service.company.model.AddressDto;
import com.bilbo.platfrom.service.company.model.BranchDto;
import com.bilbo.platfrom.service.company.model.CompanyDto;
import com.bilbo.platfrom.service.company.model.GeoLocationDto;
import com.bilbo.platfrom.service.company.repository.BranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@Slf4j
class BranchServiceTest extends AbstractIt {

    private final String COMPANY_RESOURCE = "/companies";
    private final String BRANCH_RESOURCE = "/branches";
    @Autowired
    private BranchRepository branchRepository;

    @Test
    public void crudTest() throws Exception {
        final var company1 = createCompany(1);
        final var createCompany1Rs = post(company1, HttpHeaders.EMPTY, COMPANY_RESOURCE);
        Assertions.assertThat(createCompany1Rs.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        final var location = createCompany1Rs.getHeader(HttpHeaders.LOCATION);
        Assertions.assertThat(location).isNotEmpty();

        log.info("location of company 1: {}", location);
        final var branchResource = location.concat("/" + BRANCH_RESOURCE);

        final var branch1 = createBranch(1);
        final var createBranch1Rs = post(branch1, HttpHeaders.EMPTY, branchResource);
        Assertions.assertThat(createBranch1Rs.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        final var branch1Location = createBranch1Rs.getHeader(HttpHeaders.LOCATION);
        Assertions.assertThat(branch1Location).isNotEmpty();

        log.info("location of branch 1: {}", branch1Location);

        final var getBranch1Rs = get(HttpHeaders.EMPTY, branch1Location);
        Assertions.assertThat(getBranch1Rs.getStatus()).isEqualTo(HttpStatus.OK.value());

        final var createdBranch1 = mapper.readValue(getBranch1Rs.getContentAsString(), BranchDto.class);

        Assertions.assertThat(createdBranch1.getId()).isPositive();
        Assertions.assertThat(branch1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(createdBranch1);
    }

    @Test
    public void pointTest() throws Exception {
        final var factory = new GeometryFactory(new PrecisionModel(), 4326);
//        final var factory = new GeometryFactory(new PrecisionModel(), 3576);
        final var company1 = createCompany(1);
        final var createCompany1Rs = post(company1, HttpHeaders.EMPTY, COMPANY_RESOURCE);
        Assertions.assertThat(createCompany1Rs.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        final var location = createCompany1Rs.getHeader(HttpHeaders.LOCATION);
        Assertions.assertThat(location).isNotEmpty();
        final var branchResource = location.concat("/" + BRANCH_RESOURCE);

        final var branch1 = createBranch(1);
        final var createBranch1Rs = post(branch1, HttpHeaders.EMPTY, branchResource);
        Assertions.assertThat(createBranch1Rs.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        final var branch1Location = createBranch1Rs.getHeader(HttpHeaders.LOCATION);
        Assertions.assertThat(branch1Location).isNotEmpty();

        final var point = factory.createPoint(new Coordinate(53.222042498228625, 50.19594153563107));
        System.out.println(branchRepository.findNearestBranches(point, 90));
    }

    private CompanyDto createCompany(int unique) {
        return new CompanyDto(unique + "test@mail.ru", unique + "test");
    }

    private BranchDto createBranch(int unique) {
        final var location = new GeoLocationDto(53.222062, 50.194640);
        final var address = new AddressDto("Samara", "Gastello", "49", location).flat("17");
        return new BranchDto(unique + "test", address);
    }
}