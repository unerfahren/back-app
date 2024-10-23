package com.bilbo.platfrom.service.company.repository;

import com.bilbo.platfrom.service.company.entity.BranchEntity;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<BranchEntity, Long>, JpaSpecificationExecutor<BranchEntity> {

    @EntityGraph(attributePaths = {"address", "company"})
    Optional<BranchEntity> findWithAddressByIdAndCompanyId(Long branchId, Long companyId);

    @EntityGraph(attributePaths = {"address"})
    Page<BranchEntity> findAllWithAddressByCompanyId(Long companyId, Specification<BranchEntity> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"address"})
    Page<BranchEntity> findAllWithAddressByCompanyId(Long companyId, Pageable pageable);

    void removeByIdAndCompanyId(Long branchId, Long companyId);

    @Query(value = """
            select b from BranchEntity b
            join fetch b.address a
            join fetch b.company c
            where function('ST_DWithin', a.location, :point, :distance, false) = true
            """)
    List<BranchEntity> findNearestBranches(Point point, double distance);
}
