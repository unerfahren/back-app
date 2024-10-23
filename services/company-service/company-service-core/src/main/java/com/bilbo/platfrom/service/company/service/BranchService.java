package com.bilbo.platfrom.service.company.service;

import com.bilbo.platfrom.service.company.model.BranchDto;
import com.bilbo.platfrom.service.company.model.BranchDtoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BranchService {

    BranchDto createBranch(Long companyId, BranchDto branchDto);

    void deleteBranch(Long companyId, Long branchId);

    BranchDto getBranch(Long companyId, Long branchId);

    Page<BranchDto> getBranches(Long companyId, BranchDtoFilter filter, Pageable pageable);

    BranchDto updateBranch(Long companyId, Long branchId, BranchDto branchDto);

}