package com.bilbo.platform.starter.localization.models;

import lombok.Data;

import java.util.List;

@Data
public class CodeDto {

    private String code;
    private String fieldName;
    private String provider;
    private List<Object> args;
    private String message;

}
