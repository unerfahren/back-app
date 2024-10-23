package com.bilbo.platform.service.employee.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "empoyee")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String lastName;
    private LocalDate birthsDate;
    private LocalDateTime registrationDate;
    @Version
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;
    private Long companyId;
    private Long branchId;

}
