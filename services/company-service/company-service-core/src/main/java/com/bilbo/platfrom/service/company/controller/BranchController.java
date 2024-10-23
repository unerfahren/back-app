package com.bilbo.platfrom.service.company.controller;

import com.bilbo.platfrom.service.company.api.BranchesApi;
import com.bilbo.platfrom.service.company.model.BranchDto;
import com.bilbo.platfrom.service.company.model.BranchDtoFilter;
import com.bilbo.platfrom.service.company.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class BranchController implements BranchesApi {

    private final BranchService branchService;

    @Override
    public ResponseEntity<Void> createBranch(Long companyId, BranchDto branchDto) {
        final var branch = branchService.createBranch(companyId, branchDto);
        final var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(branch.getId())
                .toUri();


        return ResponseEntity.created(location)
                .build();
    }

    @Override
    public ResponseEntity<Void> deleteBranch(Long companyId, Long branchId) {
        branchService.deleteBranch(companyId, branchId);
        return ResponseEntity.ok()
                .build();
    }

    @Override
    public ResponseEntity<BranchDto> getBranch(Long companyId, Long branchId) {
        final var branch = branchService.getBranch(companyId, branchId);
        return ResponseEntity.ok(branch);
    }

    @Override
    public ResponseEntity<PagedModel<BranchDto>> getBranches(Long companyId, BranchDtoFilter filter, Pageable pageable) {
        final var branches = branchService.getBranches(companyId, filter, pageable);
        return ResponseEntity.ok(new PagedModel<>(branches));
    }

//    @Override
//    public ResponseEntity<PagedModel<BranchDto>> searchBranches(BranchDtoFilter filter, Pageable pageable) {
//        final var branches = branchService.searchBranches(filter);
//        return ResponseEntity.ok(new PagedModel<>(branches));
//    }

    @Override
    public ResponseEntity<BranchDto> updateBranch(Long companyId, Long branchId, BranchDto branchDto) {
        final var branch = branchService.updateBranch(companyId, branchId, branchDto);
        return ResponseEntity.ok(branch);
    }
}
