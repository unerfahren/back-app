package com.bilbo.platform.starter.localization.repositories;

import com.bilbo.platform.starter.localization.entities.MessageCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MessageCodeRepository extends JpaRepository<MessageCode, String> {

    Optional<MessageCode> findByCodeAndLocale(String code, String locale);
    List<MessageCode> findAllByCodeInAndLocale(Set<String> codes, String locale);

}
