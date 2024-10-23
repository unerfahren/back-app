package com.bilbo.platfrom.service.company.controller;

import com.bilbo.platfrom.service.company.api.CompaniesApi;
import com.bilbo.platfrom.service.company.model.CompanyDto;
import com.bilbo.platfrom.service.company.model.CompanyDtoFilter;
import com.bilbo.platfrom.service.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Random;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = "*")
@RequiredArgsConstructor
public class CompanyController implements CompaniesApi {

    private final CompanyService companyService;

    @Override
    public ResponseEntity<Void> createCompany(CompanyDto companyDto) {
        final var company = companyService.createCompany(companyDto);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(company.getId())
                .toUri();

        return ResponseEntity.created(location)
                .build();
    }

    @Override
    public ResponseEntity<Void> deleteCompany(Long companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok()
                .build();
    }

    @Override
    public ResponseEntity<PagedModel<CompanyDto>> getCompanies(CompanyDtoFilter filter, Pageable pageable) {
        final var companies = companyService.getCompanies(filter, pageable);
        return ResponseEntity.ok(new PagedModel<>(companies));
    }

    @Override
    public ResponseEntity<CompanyDto> getCompany(Long companyId) {
        final var company = companyService.getCompany(companyId);
        return ResponseEntity.ok(company);
    }

    @Override
    public ResponseEntity<CompanyDto> updateCompany(Long companyId, CompanyDto companyDto) {
        final var company = companyService.updateCompany(companyId, companyDto);
        return ResponseEntity.ok(company);
    }
}
