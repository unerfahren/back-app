package com.bilbo.platform.starter.localization.components;

import com.bilbo.platform.starter.localization.models.CodeDto;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MessageResolver {

    private final Map<String, PlatformMessageSource> messageSourceMap;

    public String resolveMessage(final String code,
                                 final Locale locale,
                                 final String provider,
                                 final Object... args) {

        return messageSourceMap.get(provider).getMessage(code, locale, args);
    }

    public List<CodeDto> resolveMessages(final List<CodeDto> codes,
                                         final Locale locale) {

        final var providerMap = codes.stream()
                .collect(Collectors.groupingBy(CodeDto::getProvider));

        return providerMap.entrySet().stream()
                .map(entry -> {
                    final var messageSource = messageSourceMap.get(entry.getKey());
                    return messageSource.getMessages(entry.getValue(), locale);
                })
                .reduce((codeDtos, codeDtos2) -> {
                    codeDtos.addAll(codeDtos2);
                    return codeDtos;
                })
                .orElse(Collections.emptyList());
    }
}
