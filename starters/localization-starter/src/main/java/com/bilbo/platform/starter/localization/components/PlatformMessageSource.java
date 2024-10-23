package com.bilbo.platform.starter.localization.components;

import com.bilbo.platform.starter.localization.models.CodeDto;

import java.util.List;
import java.util.Locale;

public interface PlatformMessageSource {

    String getMessage(String code,
                      Locale locale,
                      Object... args);

    List<CodeDto> getMessages(List<CodeDto> codes, Locale locale);

}
