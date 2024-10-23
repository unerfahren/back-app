package com.bilbo.platform.service.client.repository;

import com.bilbo.platform.service.client.entity.ClientEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long>, JpaSpecificationExecutor<ClientEntity> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<ClientEntity> findClientEntityById(Long id);

}
