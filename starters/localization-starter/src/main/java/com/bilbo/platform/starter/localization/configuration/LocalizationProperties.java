package com.bilbo.platform.starter.localization.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Locale;

@Data
@ConfigurationProperties(prefix = "platform.locale")
public class LocalizationProperties {

    private boolean enable;
    private Locale defaultLocale = Locale.ENGLISH;
    private List<ResourceProvider> resourceProviders;

}
