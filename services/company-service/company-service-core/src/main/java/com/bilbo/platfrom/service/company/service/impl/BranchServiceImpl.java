package com.bilbo.platfrom.service.company.service.impl;

import com.bilbo.platform.repository.SpecBuilder;
import com.bilbo.platfrom.service.company.entity.BranchEntity;
import com.bilbo.platfrom.service.company.model.BranchDto;
import com.bilbo.platfrom.service.company.model.BranchDtoFilter;
import com.bilbo.platfrom.service.company.model.GeoLocationDto;
import com.bilbo.platfrom.service.company.repository.BranchRepository;
import com.bilbo.platfrom.service.company.repository.CompanyRepository;
import com.bilbo.platfrom.service.company.service.BranchMapper;
import com.bilbo.platfrom.service.company.service.BranchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final CompanyRepository companyRepository;
    private final BranchRepository branchRepository;
    private final BranchMapper mapper;

    @Override
    @Transactional
    public BranchDto createBranch(Long companyId, BranchDto branchDto) {
        final var company = companyRepository.findWithBranchesById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("company with id %s not found".formatted(companyId)));
        final var branch = mapper.mapToEntity(branchDto);
        branch.getAddress().setBranch(branch);
        branch.setCompany(company);
        company.getBranches().add(branch);
        final var saved = branchRepository.save(branch);
        return mapper.mapToDto(saved);
    }

    @Override
    @Transactional
    public void deleteBranch(Long companyId, Long branchId) {
        branchRepository.removeByIdAndCompanyId(branchId, companyId);
    }

    @Override
    public BranchDto getBranch(Long companyId, Long branchId) {
        return branchRepository.findWithAddressByIdAndCompanyId(branchId, companyId)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "branch with companyId %s and branchId %s not found".formatted(companyId, branchId)));
    }

    @Override
    public Page<BranchDto> getBranches(Long companyId, BranchDtoFilter filter, Pageable pageable) {
        final var pageRequest = Optional.ofNullable(pageable)
                .orElseGet(this::createDefaultPageable);

        return Optional.ofNullable(filter)
                .flatMap(this::createSpec)
                .map(spec -> branchRepository.findAllWithAddressByCompanyId(companyId, spec, pageRequest))
                .orElseGet(() -> branchRepository.findAllWithAddressByCompanyId(companyId, pageRequest))
                .map(mapper::mapToDto);
    }

    private Optional<Specification<BranchEntity>> createSpec(BranchDtoFilter filter) {
        return new SpecBuilder<BranchEntity>()
                .with(SpecBuilder::stringIs, "name", filter.getName())
                .build(Specification::and);
    }

    private Pageable createDefaultPageable() {
        final var sort = Sort.sort(BranchEntity.class)
                .by(BranchEntity::getName)
                .ascending();

        return PageRequest.of(0, 10, sort);
    }

    @Override
    @Transactional
    public BranchDto updateBranch(Long companyId, Long branchId, BranchDto branchDto) {
        return branchRepository.findWithAddressByIdAndCompanyId(branchId, companyId)
                .map(entity -> mapper.updateEntity(entity, branchDto))
                .map(branchRepository::save)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "branch with companyId %s and branchId %s not found".formatted(companyId, branchId)));
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page<BranchDto> searchBranches(BranchDtoFilter filter) {
//        if (Objects.nonNull(filter.getLongitude()) && Objects.nonNull(filter.getLatitude())) {
//            final var point = mapper.locationDtoToLocationEntity(new GeoLocationDto(filter.getLongitude(), filter.getLatitude()));
//            final var rs = branchRepository.findNearestBranches(point, filter.getDistance())
//                    .filter(branch -> isEqual(filter::getArea, branch.getAddress()::getArea))
//                    .filter(branch -> isEqual(filter::getDoma, branch.getAddress()::getDoma))
//                    .filter(branch -> isEqual(filter::getFlat, branch.getAddress()::getFlat))
//                    .filter(branch -> isEqual(filter::getLocality, branch.getAddress()::getLocality))
//                    .filter(branch -> isEqual(filter::getStreet, branch.getAddress()::getStreet))
//                    .filter(branch -> isEqual(filter::getRegion, branch.getAddress()::getRegion))
//                    .map(mapper::mapToDto)
//                    .toList();
//            return new PageImpl<>(rs);
//        } else {
//            return this.createSpec(filter)
//                    .map(spec -> branchRepository.findAll(spec, createDefaultPageable()))
//                    .orElseGet(() -> branchRepository.findAll(createDefaultPageable()))
//                    .map(mapper::mapToDto);
//        }
//    }
//
//    private <T, V, U> boolean isEqual(Supplier<U> target, Supplier<U> source) {
//        return target.get().equals(source.get());
//    }
}
