package com.bilbo.platform.starter.localization.components;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class MessageArgsContainer {

    @Getter
    private final Map<String, List<Object>> argsMap = new HashMap<>();
}
