package com.bilbo.platform.starter.localization.components;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FieldArgumentsResolver {

    public MessageArgsContainer getArgsContainer() {
        return messageArgsContainer();
    }

    public void purArgs(String fieldName, Object... args) {
        messageArgsContainer().getArgsMap().put(fieldName, List.of(args));
    }

    public void purArgs(Map<String, List<Object>> args) {
        messageArgsContainer().getArgsMap().putAll(args);
    }

    public List<Object> getArgs(String fieldName) {
        return messageArgsContainer().getArgsMap().get(fieldName);
    }

    public Map<String, List<Object>> getArgsMap() {
        return messageArgsContainer().getArgsMap();
    }

    @Lookup
    public MessageArgsContainer messageArgsContainer() {
        return null;
    }
}
