package com.bilbo.platfrom.service.company.repository;

import com.bilbo.platfrom.service.company.entity.CompanyEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>, JpaSpecificationExecutor<CompanyEntity> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<CompanyEntity> findLockedById(Long id);

    @EntityGraph(attributePaths = "branches")
    Optional<CompanyEntity> findWithBranchesById(Long id);

}
