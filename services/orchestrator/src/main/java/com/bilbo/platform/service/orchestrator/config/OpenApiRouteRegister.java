package com.bilbo.platform.service.orchestrator.config;

import com.bilbo.platform.service.orchestrator.processes.OpenApiRoute;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class OpenApiRouteRegister implements BeanDefinitionRegistryPostProcessor {

    private final List<OpenApiRoute> routes;
    public OpenApiRouteRegister(Environment environment) {
        final var bindable = Bindable.mapOf(String.class, OpenApiRouteProperties.Route.class);
        final var properties = Binder.get(environment).bind("camel.openapi.routes", bindable).get();
        routes = properties.entrySet().stream()
                .map(entry -> new OpenApiRoute(entry.getValue(), entry.getKey()))
                .toList();
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        routes.forEach(route -> {
            final var beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(OpenApiRoute.class, () -> route)
                    .getBeanDefinition();
            registry.registerBeanDefinition(route.getName(), beanDefinition);
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final var beans = beanFactory.getBeanNamesForType(OpenApiRoute.class);
        log.info("Registered openapi routes: {}", Arrays.toString(beans));
    }
}
