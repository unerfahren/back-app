package com.bilbo.platfrom.service.company.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "company")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String ownerEmail;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String shortDescription;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company")
    private List<BranchEntity> branches;
    @Version
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

}
