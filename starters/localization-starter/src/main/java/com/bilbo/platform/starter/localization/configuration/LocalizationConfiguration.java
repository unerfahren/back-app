package com.bilbo.platform.starter.localization.configuration;

import com.bilbo.platform.starter.localization.components.MessageResolver;
import com.bilbo.platform.starter.localization.components.PlatformMessageSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Map;

@AutoConfiguration
@ComponentScan(basePackages = "com.bilbo.platform.starter.localization")
@ConditionalOnProperty(prefix = "platform.localization", name = "enabled", havingValue = "true")
@EnableJpaRepositories(basePackages = {"com.bilbo.platform.starter.localization"})
@EnableConfigurationProperties(LocalizationProperties.class)
public class LocalizationConfiguration {

    @Bean
    public MessageResolver messageResolver(Map<String, PlatformMessageSource> messageSourceMap) {
        return new MessageResolver(messageSourceMap);
    }
}
