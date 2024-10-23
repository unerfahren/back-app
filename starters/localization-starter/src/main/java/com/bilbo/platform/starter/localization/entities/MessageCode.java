package com.bilbo.platform.starter.localization.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(schema = "locale")
public class MessageCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String locale;
    private String message;

}
