package com.bilbo.platfrom.service.company.service;

import com.bilbo.platfrom.service.company.model.CompanyDto;
import com.bilbo.platfrom.service.company.model.CompanyDtoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {

    CompanyDto createCompany(CompanyDto companyDto);

    void deleteCompany(Long id);

    CompanyDto getCompany(Long id);

    Page<CompanyDto> getCompanies(CompanyDtoFilter filter, Pageable pageable);

    CompanyDto updateCompany(Long id, CompanyDto clientDto);
}
