package com.bilbo.platform.starter.localization.components;

import com.bilbo.platform.starter.localization.annotations.MessageProvider;
import com.bilbo.platform.starter.localization.configuration.ResourceProvider;
import com.bilbo.platform.starter.localization.models.CodeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;

@Slf4j
@MessageProvider(ResourceProvider.BUNDLE)
public class PropertyMessageSource implements PlatformMessageSource {

    private final MessageSource messageSource;

    public PropertyMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(final String code,
                             final Locale locale,
                             final Object... args) {

        return messageSource.getMessage(code, args, locale);
    }

    @Override
    public List<CodeDto> getMessages(final List<CodeDto> codes,
                                     final Locale locale) {

        codes.forEach(codeDto -> codeDto.setMessage(getMessage(codeDto.getCode(), locale, codeDto.getArgs())));
        return codes;
    }

}
