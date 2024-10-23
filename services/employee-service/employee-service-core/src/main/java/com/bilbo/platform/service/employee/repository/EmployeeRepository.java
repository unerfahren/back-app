package com.bilbo.platform.service.employee.repository;

import com.bilbo.platform.service.employee.entity.EmployeeEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>, JpaSpecificationExecutor<EmployeeEntity> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<EmployeeEntity> findEmployeeEntitiesById(Long id);
}
