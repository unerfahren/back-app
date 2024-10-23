package com.bilbo.platfrom.service.company.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "address")
@ToString(exclude = "branch")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String region;
    private String area;
    @Column(nullable = false)
    private String locality;
    private String street;
    @Column(nullable = false)
    private String doma;
    private String flat;
    @Column(columnDefinition = "geography(Point,4326)")
//    @Column(columnDefinition = "geography(Point,3576)")
    private Point location;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private BranchEntity branch;
}
