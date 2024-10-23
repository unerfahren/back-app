package com.bilbo.platform.starter.localization.components;

import com.bilbo.platform.starter.localization.annotations.MessageProvider;
import com.bilbo.platform.starter.localization.configuration.LocalizationProperties;
import com.bilbo.platform.starter.localization.configuration.ResourceProvider;
import com.bilbo.platform.starter.localization.entities.MessageCode;
import com.bilbo.platform.starter.localization.models.CodeDto;
import com.bilbo.platform.starter.localization.repositories.MessageCodeRepository;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.support.AbstractMessageSource;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@MessageProvider(ResourceProvider.DB)
public class DbMessageSource extends AbstractMessageSource implements PlatformMessageSource {

    private final MessageCodeRepository repository;
    private final LocalizationProperties properties;

    public DbMessageSource(MessageCodeRepository repository,
                           LocalizationProperties properties) {
        this.repository = repository;
        this.properties = properties;
    }

    @Override
    public String getMessage(final String code,
                             final Locale locale,
                             final Object... args) {

        return Optional.ofNullable(resolveCode(code, locale))
                .map(messageFormat -> messageFormat.format(args))
                .orElseThrow(() -> new NullPointerException(
                        "Not found message for code %s and locale %s".formatted(code, locale)));
    }

    @Override
    public List<CodeDto> getMessages(List<CodeDto> codes, Locale locale) {

        final var codesMap = codes.stream()
                .map(CodeDto::getCode)
                .collect(Collectors.toSet());
        final var messagesMap = resolveCodes(codesMap, locale);

        codes.forEach(codeDto -> {
            final var message = messagesMap.get(codeDto.getCode()).format(codeDto.getArgs());
            codeDto.setMessage(message);
        });
        return codes;
    }

    @Override
    protected MessageFormat resolveCode(@Nullable String code, @Nullable Locale locale) {

        if (Strings.isBlank(code)) {
            return null;
        }

        final var defLocale = locale == null
                ? properties.getDefaultLocale()
                : locale;

        return repository.findByCodeAndLocale(code, defLocale.toLanguageTag())
                .map(MessageCode::getMessage)
                .map(message -> new MessageFormat(message, defLocale))
                .orElse(null);
    }

    protected Map<String, MessageFormat> resolveCodes(Set<String> codes, @Nullable Locale locale) {

        if (Objects.isNull(codes)) {
            return null;
        }

        final var defLocale = locale == null
                ? properties.getDefaultLocale()
                : locale;

        return repository.findAllByCodeInAndLocale(codes, defLocale.toLanguageTag()).stream()
                .collect(Collectors.toMap(
                        MessageCode::getCode,
                        messageCode -> new MessageFormat(messageCode.getMessage(), defLocale)));
    }

}