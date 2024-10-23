package com.bilbo.platform.starter.localization.components;

import com.bilbo.platform.starter.localization.annotations.Localized;
import com.bilbo.platform.starter.localization.annotations.Rewrote;
import com.bilbo.platform.starter.localization.models.CodeDto;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class InternationalizationAdvice implements ResponseBodyAdvice<Object> {

    private final MessageResolver messageResolver;
    private final FieldArgumentsResolver argumentsResolver;

    @Override
    public boolean supports(@Nullable MethodParameter returnType,
                            @Nullable Class<? extends HttpMessageConverter<?>> converterType) {

        return Optional.ofNullable(returnType)
                .map(methodParameter -> methodParameter.getMethodAnnotation(Localized.class))
                .isPresent();
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @Nullable MethodParameter returnType,
                                  @Nullable MediaType selectedContentType,
                                  @Nullable Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @Nullable ServerHttpRequest request,
                                  @Nullable ServerHttpResponse response) {

        final var argsMap = argumentsResolver.getArgsMap();

        if (Objects.isNull(returnType)) {
            return body;
        }

        final var locale = Optional.ofNullable(request)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::getContentLanguage)
                .orElse(Locale.getDefault());

        final List<CodeDto> messages;
        if (Iterable.class.isAssignableFrom(body.getClass())) {

            final var codeDtos = Streamable.of((Iterable<?>) body).stream()
                    .map(o -> buildCodeDtos(o, argsMap))
                    .flatMap(Collection::stream)
                    .toList();
            messages = messageResolver.resolveMessages(codeDtos, locale);
        } else {

            final var codeDtos = buildCodeDtos(body, argsMap);
            messages = messageResolver.resolveMessages(codeDtos, locale);
        }

        messages.forEach(codeDto -> {
            final var field = ReflectionUtils.findField(body.getClass(), codeDto.getFieldName());
            if (Objects.nonNull(field)) {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, body, codeDto.getMessage());
            } else {
                log.warn("can't rewrite, field {} not found", codeDto.getFieldName());
            }
        });

        return body;
    }

    private List<CodeDto> buildCodeDtos(final Object body,
                                        final Map<String, List<Object>> argsMap) {

        return Arrays.stream(body.getClass().getDeclaredFields())
                .filter(field ->
                        field.isAnnotationPresent(Rewrote.class) && String.class.isAssignableFrom(field.getType()))
                .peek(ReflectionUtils::makeAccessible)
                .map(field -> {
                    final var args = argsMap.get(field.getName());
                    return buildCodeDto(field, body, args);
                })
                .toList();
    }

    private CodeDto buildCodeDto(final Field field,
                                 final Object object,
                                 final List<Object> args) {

        final var code = (String) ReflectionUtils.getField(field, object);
        final var rewrote = field.getAnnotation(Rewrote.class);
        final var codeDto = new CodeDto();
        codeDto.setCode(code);
        codeDto.setArgs(args);
        codeDto.setFieldName(field.getName());
        codeDto.setProvider(rewrote.provider());

        return codeDto;
    }
}
