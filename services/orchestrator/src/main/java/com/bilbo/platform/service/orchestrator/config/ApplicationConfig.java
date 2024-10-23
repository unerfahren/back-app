package com.bilbo.platform.service.orchestrator.config;

import org.apache.camel.CamelContext;
import org.apache.camel.saga.CamelSagaService;
import org.apache.camel.saga.InMemorySagaService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableConfigurationProperties(OpenApiRouteProperties.class)
public class ApplicationConfig {

    @Bean
    public CamelSagaService camelSagaService(CamelContext context) throws Exception {
        final var inMemorySagaService = new InMemorySagaService();
        inMemorySagaService.setCamelContext(context);
        context.addService(inMemorySagaService);
        return inMemorySagaService;
    }

    @Bean
    public static OpenApiRouteRegister openApiRouteRegister(Environment environment) {
        return new OpenApiRouteRegister(environment);
    }

//    @Bean
//    public StateMachineConfig dbStateMachineConfig(DataSource dataSource) {
//        final var config = new DbStateMachineConfig();
//        config.setResources(new String[] {"classpath:processes/*.json"});
//        config.setDataSource(dataSource);
//        config.setApplicationId("orchestrator");
//        config.setTxServiceGroup("orchestrator_group");
//        return config;
//    }
//
//    @Bean
//    public StateMachineEngine stateMachineEngine(StateMachineConfig config) {
//        final var engine = new ProcessCtrlStateMachineEngine();
//        engine.setStateMachineConfig(config);
//        return engine;
//    }
//
//    @Bean
//    public StateMachineEngineHolder stateMachineEngineHolder(StateMachineEngine engine) {
//        final var holder = new StateMachineEngineHolder();
//        holder.setStateMachineEngine(engine);
//        return holder;
//    }
}
