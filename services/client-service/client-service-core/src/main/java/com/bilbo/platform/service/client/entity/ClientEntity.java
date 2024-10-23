package com.bilbo.platform.service.client.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    private String lastName;
    private LocalDate birthsDate;
    private LocalDateTime registrationDate;
    @Version
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

}
